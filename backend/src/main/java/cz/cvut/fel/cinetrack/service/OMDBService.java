/*
 * Created by minmin_tranova on 10.11.2025
 */

package cz.cvut.fel.cinetrack.service;

import cz.cvut.fel.cinetrack.config.OMDBConfig;
import cz.cvut.fel.cinetrack.dto.media.EpisodeInfoDTO;
import cz.cvut.fel.cinetrack.dto.media.response.omdb.OMDBEpisodeResponseDTO;
import cz.cvut.fel.cinetrack.dto.media.response.omdb.OMDBResponseDTO;
import cz.cvut.fel.cinetrack.dto.media.response.omdb.OMDBSearchItemDTO;
import cz.cvut.fel.cinetrack.dto.media.response.omdb.OMDBSearchResultDTO;
import cz.cvut.fel.cinetrack.dto.media.response.omdb.OMDBSeasonResponseDTO;
import cz.cvut.fel.cinetrack.dto.media.SeasonInfoDTO;
import cz.cvut.fel.cinetrack.exception.media.notFoundObj.MediaNotFoundException;
import cz.cvut.fel.cinetrack.exception.media.RequestFailedException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class OMDBService {


    private final String apiKey;
    private final RestTemplate restTemplate;

    public OMDBService(OMDBConfig omdbConfig, RestTemplateBuilder restTemplateBuilder) {
        this.apiKey = omdbConfig.getApiKey();
        this.restTemplate = restTemplateBuilder.build();
    }

    public OMDBResponseDTO searchMedia(String title) {
        String url = String.format(
                "https://www.omdbapi.com/?apikey=%s&t=%s",
                apiKey, encode(title)
        );
        System.out.println("OMDB API URL: " + url);
        try {
            ResponseEntity<OMDBResponseDTO> response = restTemplate.getForEntity(url, OMDBResponseDTO.class);
            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                throw new RequestFailedException(String.format("OMDB API request failed with status %s ", response.getStatusCode()));
            }
            OMDBResponseDTO responseBody = response.getBody();
            if (responseBody.getResponse() != null && "False".equalsIgnoreCase(responseBody.getResponse())) {
                throw new MediaNotFoundException(String.format("Media with title %s not found!", title));
            }
            if (responseBody.getImdbID() == null) {
                throw new MediaNotFoundException(String.format("Media with title %s not found!", title));
            }
            return responseBody;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RequestFailedException("OMDB API error: " + e.getMessage());
        } catch (Exception e) {
            throw new RequestFailedException("Failed to search media: " + e.getMessage());
        }
    }

    public List<OMDBResponseDTO> searchMediaList(String query, int limit) {
        String url = String.format(
                "https://www.omdbapi.com/?apikey=%s&s=%s",
                apiKey, encode(query)
        );
        try {
            OMDBSearchResultDTO searchResult = restTemplate.getForObject(url, OMDBSearchResultDTO.class);
            if (searchResult == null || searchResult.getSearch() == null || "False".equalsIgnoreCase(searchResult.getResponse())) {
                return List.of();
            }
            List<OMDBSearchItemDTO> items = searchResult.getSearch()
                    .stream()
                    .limit(limit)
                    .toList();
            List<CompletableFuture<OMDBResponseDTO>> futures = items.stream()
                    .map(item -> CompletableFuture.supplyAsync(() -> fetchDetailById(item.getImdbID())))
                    .toList();
            return futures.stream()
                    .map(CompletableFuture::join)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RequestFailedException("Failed to search media list: " + e.getMessage());
        }
    }

    private OMDBResponseDTO fetchDetailById(String imdbID) {
        try {
            String url = String.format(
                    "https://www.omdbapi.com/?apikey=%s&i=%s",
                    apiKey, imdbID
            );
            return restTemplate.getForObject(url, OMDBResponseDTO.class);
        } catch (Exception e) {
            return null;
        }
    }

    public List<SeasonInfoDTO> getSeasonsInfo(String imdbId, String title) {
        List<SeasonInfoDTO> seasons = new ArrayList<>();
        try {
            String seriesURL = String.format(
                    "https://www.omdbapi.com/?apikey=%s&i=%s",
                    apiKey, imdbId
            );
            OMDBResponseDTO response = restTemplate.getForObject(seriesURL, OMDBResponseDTO.class);

            if (response != null && response.getTotalSeasons() != null) {
                int totalSeasons = Integer.parseInt(response.getTotalSeasons());

                for (int season = 1; season <= totalSeasons; season++) {
                    SeasonInfoDTO seasonInfoDTO = getSeasonInfo(imdbId, title, season);

                    if (seasonInfoDTO != null) {
                        seasons.add(seasonInfoDTO);
                    }
                }
            }
        } catch (Exception e) {
            throw new RequestFailedException("Error fetching seasons info");
        }
        return seasons;
    }

    private SeasonInfoDTO getSeasonInfo(String imdbId, String title, int season) {
        try {
            String url = String.format(
                    "https://www.omdbapi.com/?apikey=%s&i=%s&Season=%d",
                    apiKey, imdbId, season
            );
            OMDBSeasonResponseDTO seasonResponse = restTemplate.getForObject(url, OMDBSeasonResponseDTO.class);
            if (seasonResponse != null && "True".equals(seasonResponse.getResponse())) {
                SeasonInfoDTO seasonInfoDTO = new SeasonInfoDTO();
                seasonInfoDTO.setTitle(title + " Season " + season);
                seasonInfoDTO.setSeason(season);
                seasonInfoDTO.setEpisodes(seasonResponse.getEpisodes() != null ? seasonResponse.getEpisodes().size() : 0);

                List<EpisodeInfoDTO> episodes = new ArrayList<>();
                if (seasonResponse.getEpisodes() != null) {
                    for (OMDBEpisodeResponseDTO omdbEpisodeResponseDTO : seasonResponse.getEpisodes()) {
                        EpisodeInfoDTO episodeInfo = new EpisodeInfoDTO();
                        episodeInfo.setTitle(omdbEpisodeResponseDTO.getTitle());
                        episodeInfo.setEpisode(Integer.parseInt(omdbEpisodeResponseDTO.getEpisode()));
                        episodes.add(episodeInfo);
                    }
                }
                seasonInfoDTO.setEpisodeList(episodes);
                return seasonInfoDTO;
            }
        } catch (Exception e) {
            throw new RequestFailedException("Error fetching seasons info");
        }
        return null;
    }

    public List<EpisodeInfoDTO> getEpisodesForSeason(String imdbId, int season) {
        try {
            String url = String.format(
                    "https://www.omdbapi.com/?apikey=%s&i=%s&Season=%d",
                    apiKey, imdbId, season
            );
            OMDBSeasonResponseDTO seasonResponse = restTemplate.getForObject(url, OMDBSeasonResponseDTO.class);
            if (seasonResponse != null && "True".equals(seasonResponse.getResponse()) && seasonResponse.getEpisodes() != null) {
                List<EpisodeInfoDTO> episodeList = new ArrayList<>();
                for(OMDBEpisodeResponseDTO omdbEpisode : seasonResponse.getEpisodes()) {
                    EpisodeInfoDTO episodeInfo = new EpisodeInfoDTO();
                    episodeInfo.setTitle(omdbEpisode.getTitle());
                    episodeInfo.setEpisode(Integer.parseInt(omdbEpisode.getEpisode()));
                    episodeInfo.setReleaseDate(omdbEpisode.getReleased());
                    episodeList.add(episodeInfo);
                }
                return episodeList;
            }
        } catch (Exception e) {
            throw new RequestFailedException("Error fetching episodes info");
        }
        return new ArrayList<>();
    }

    private String encode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }
}
