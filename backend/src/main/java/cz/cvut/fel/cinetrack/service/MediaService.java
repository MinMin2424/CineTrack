/*
 * Created by minmin_tranova on 10.11.2025
 */

package cz.cvut.fel.cinetrack.service;

import cz.cvut.fel.cinetrack.dto.media.EpisodeInfoDTO;
import cz.cvut.fel.cinetrack.dto.media.MediaItemDTO;
import cz.cvut.fel.cinetrack.dto.media.request.MovieCreateRequestDTO;
import cz.cvut.fel.cinetrack.dto.media.response.MovieResponseDTO;
import cz.cvut.fel.cinetrack.dto.media.response.omdb.OMDBResponseDTO;
import cz.cvut.fel.cinetrack.dto.media.request.SearchRequestDTO;
import cz.cvut.fel.cinetrack.dto.media.SeasonInfoDTO;
import cz.cvut.fel.cinetrack.dto.media.request.SeriesCreateRequestDTO;
import cz.cvut.fel.cinetrack.dto.media.response.SeriesSearchResponseDTO;
import cz.cvut.fel.cinetrack.exception.media.InvalidMediaTypeException;
import cz.cvut.fel.cinetrack.exception.media.existingData.MovieAlreadyExistsException;
import cz.cvut.fel.cinetrack.exception.media.existingData.SeriesAlreadyExistsException;
import cz.cvut.fel.cinetrack.model.Episode;
import cz.cvut.fel.cinetrack.model.Movie;
import cz.cvut.fel.cinetrack.model.Series;
import cz.cvut.fel.cinetrack.model.enums.EpisodeStatusEnum;
import cz.cvut.fel.cinetrack.model.enums.StatusEnum;
import cz.cvut.fel.cinetrack.model.enums.ValidationMessage;
import cz.cvut.fel.cinetrack.repository.EpisodeRepository;
import cz.cvut.fel.cinetrack.repository.MovieRepository;
import cz.cvut.fel.cinetrack.repository.SeriesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static cz.cvut.fel.cinetrack.mapper.MediaMapper.mapMovieDTOToEntity;
import static cz.cvut.fel.cinetrack.mapper.MediaMapper.mapSeriesDTOToEntity;
import static cz.cvut.fel.cinetrack.util.MediaUtils.*;
import static cz.cvut.fel.cinetrack.validator.MediaValidator.validateStatusDates;

@Service
@Transactional
public class MediaService {

    private final MovieRepository movieRepository;
    private final SeriesRepository seriesRepository;
    private final EpisodeRepository episodeRepository;
    private final CountryService countryService;
    private final LanguageService languageService;
    private final GenreService genreService;
    private final OMDBService omdbService;

    public MediaService(MovieRepository movieRepository,
                        SeriesRepository seriesRepository,
                        EpisodeRepository episodeRepository,
                        CountryService countryService,
                        LanguageService languageService,
                        GenreService genreService,
                        OMDBService omdbService) {
        this.movieRepository = movieRepository;
        this.seriesRepository = seriesRepository;
        this.episodeRepository = episodeRepository;
        this.countryService = countryService;
        this.languageService = languageService;
        this.genreService = genreService;
        this.omdbService = omdbService;
    }

    public List<MediaItemDTO> getUserMedia(Long userId) {
        List<Movie> movies = movieRepository.findNotDeletedMoviesByUserIdOrderByCreatedAtDesc(userId);
        List<Series> series = seriesRepository.findNotDeletedSeriesByUserIdOrderByCreatedAtDesc(userId);

        List<MediaItemDTO> mediaItems = new ArrayList<>();

        mediaItems.addAll(movies.stream()
                .map(MediaItemDTO::new)
                .toList());

        mediaItems.addAll(series.stream()
                .map(MediaItemDTO::new)
                .toList());

        mediaItems.sort(Comparator.comparing(MediaItemDTO::getCreatedAt).reversed());
        return mediaItems;
    }

    public OMDBResponseDTO searchMovie(SearchRequestDTO request) {
        OMDBResponseDTO response = omdbService.searchMedia(request.getTitle());
        if (!"movie".equalsIgnoreCase(request.getType())) {
            throw new InvalidMediaTypeException(ValidationMessage.INVALID_MEDIA_TYPE.getMessage());
        }
        return response;
    }

    public SeriesSearchResponseDTO searchSeriesWithSeason(SearchRequestDTO request) {
        OMDBResponseDTO seriesInfo = omdbService.searchMedia(request.getTitle());
        if (!"series".equals(seriesInfo.getType())) {
            throw new InvalidMediaTypeException(ValidationMessage.INVALID_MEDIA_TYPE.getMessage());
        }
        List<SeasonInfoDTO> availableSeasons = omdbService.getSeasonsInfo(
                seriesInfo.getImdbID(),
                seriesInfo.getTitle()
        );
        return new SeriesSearchResponseDTO(seriesInfo, availableSeasons);
    }

    public MovieResponseDTO createMovie(MovieCreateRequestDTO movieDTO, Long userId) {
        validateStatusDates(
                parseStatus(movieDTO.getStatus()),
                movieDTO.getWatchStartDate(),
                movieDTO.getWatchEndDate()
        );
        if (movieRepository.existsByImdbIdAndUserId(movieDTO.getImdbID(), userId)) {
            throw new MovieAlreadyExistsException(ValidationMessage.MOVIE_ALREADY_EXISTS.getMessage());
        }
        Movie movie = new Movie();
        mapMovieDTOToEntity(movieDTO, movie, languageService, countryService, genreService);
        Movie savedMovie = movieRepository.save(movie);
        return new MovieResponseDTO(savedMovie);
    }

    public Series createSeries(SeriesCreateRequestDTO seriesDTO, Long userId) {
        validateStatusDates(
                parseStatus(seriesDTO.getStatus()),
                seriesDTO.getWatchStartDate(),
                seriesDTO.getWatchEndDate()
        );
        if (seriesRepository.existsByImdbIdAndSeasonAndUserId(seriesDTO.getImdbID(), parseStringToInt(seriesDTO.getSeason()), userId)) {
            throw new SeriesAlreadyExistsException(ValidationMessage.SERIES_ALREADY_EXISTS.getMessage());
        }
        Series series = new Series();
        mapSeriesDTOToEntity(seriesDTO, series, languageService, countryService, genreService);
        Series savedSeries = seriesRepository.save(series);
        createEpisodesForSeries(series, seriesDTO.getImdbID(), parseStringToInt(seriesDTO.getSeason()));
        return savedSeries;
    }

    private void createEpisodesForSeries(Series series, String imdbId, int season) {
        try {
            List<EpisodeInfoDTO> episodeInfos = omdbService.getEpisodesForSeason(imdbId, season);

            List<Episode> episodes = new ArrayList<>();
            for (EpisodeInfoDTO episodeInfo : episodeInfos) {
                Episode episode = new Episode();
                episode.setTitle(episodeInfo.getTitle());
                episode.setEpisode(episodeInfo.getEpisode());

                if (series.getStatus() == StatusEnum.COMPLETED) {
                    episode.setStatus(EpisodeStatusEnum.COMPLETED);
                } else {
                    episode.setStatus(EpisodeStatusEnum.NONE);
                }

                episode.setRating(0);
                episode.setNotes("");
                episode.setSeries(series);
                episodes.add(episode);
            }
            episodeRepository.saveAll(episodes);

            String dateString = episodeInfos.getFirst().getReleaseDate();
            LocalDate date = LocalDate.parse(dateString);
            int year = date.getYear();
            series.setReleaseYear(year);

            series.setEpisodes(episodes.size());

            seriesRepository.save(series);
        } catch (Exception e) {
            createFallbackEpisodes(series, series.getEpisodes());
        }
    }

    private void createFallbackEpisodes(Series series, int episodeCount) {
        List<Episode> episodes = new ArrayList<>();
        for(int i = 1; i <= episodeCount; i++) {
            Episode episode = new Episode();
            episode.setTitle("Episode :" + i);
            episode.setEpisode(i);
            episode.setStatus(series.getStatus() == StatusEnum.COMPLETED ? EpisodeStatusEnum.COMPLETED : EpisodeStatusEnum.NONE);
            episode.setRating(0);
            episode.setNotes("");
            episode.setSeries(series);
            episodes.add(episode);
        }
        episodeRepository.saveAll(episodes);
    }

}
