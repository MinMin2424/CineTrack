/*
 * Created by minmin_tranova on 01.12.2025
 */

package cz.cvut.fel.cinetrack.service;

import cz.cvut.fel.cinetrack.dto.media.request.MovieManualRequestDTO;
import cz.cvut.fel.cinetrack.dto.media.request.SeriesManualRequestDTO;
import cz.cvut.fel.cinetrack.dto.media.response.MovieResponseDTO;
import cz.cvut.fel.cinetrack.dto.media.response.SeriesResponseDTO;
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
import cz.cvut.fel.cinetrack.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static cz.cvut.fel.cinetrack.util.MediaUtils.parseStatus;
import static cz.cvut.fel.cinetrack.validator.MediaValidator.*;

@Service
@Transactional
public class MediaManualService {

    private final MovieRepository movieRepository;
    private final SeriesRepository seriesRepository;
    private final EpisodeRepository episodeRepository;
    private final LanguageService languageService;
    private final GenreService genreService;
    private final CountryService countryService;

    public MediaManualService(MovieRepository movieRepository,
                              SeriesRepository seriesRepository,
                              EpisodeRepository episodeRepository,
                              LanguageService languageService,
                              GenreService genreService,
                              CountryService countryService
    ) {
        this.movieRepository = movieRepository;
        this.seriesRepository = seriesRepository;
        this.episodeRepository = episodeRepository;
        this.languageService = languageService;
        this.genreService = genreService;
        this.countryService = countryService;
    }

    public MovieResponseDTO addMovieManually(MovieManualRequestDTO request, Long userId) {
        if (movieRepository.existsByTitleAndUserId(request.getTitle(), userId)) {
            throw new MovieAlreadyExistsException(ValidationMessage.MOVIE_ALREADY_EXISTS.getMessage());
        }
        validateStatusDates(parseStatus(request.getStatus()), request.getWatchStartDate(), request.getWatchEndDate());
        validateRating(request.getRating());

        Movie movie = createMovie(request);
        return new MovieResponseDTO(movie);
    }

    public SeriesResponseDTO addSeriesManually(SeriesManualRequestDTO request, Long userId) {
        if (seriesRepository.existsByTitleAndUserId(request.getTitle(), userId)) {
            throw new SeriesAlreadyExistsException(ValidationMessage.SERIES_ALREADY_EXISTS.getMessage());
        }
        validateStatusDates(parseStatus(request.getStatus()), request.getWatchStartDate(), request.getWatchEndDate());
        validateRating(request.getRating());

        Series series = createSeries(request);
        createFallbackEpisodes(series, request.getEpisodeCount());
        return new SeriesResponseDTO(series);
    }

    private Movie createMovie(MovieManualRequestDTO request) {
        Movie movie = new Movie();
        movie.setImdbId(null);
        movie.setTitle(request.getTitle());
        movie.setRuntime(request.getRuntime() + " min");
        movie.setReleaseYear(request.getReleaseYear());
        movie.setPoster(request.getPosterUrl());

        movie.setStatus(parseStatus(request.getStatus()));
        movie.setWatchStartDate(request.getWatchStartDate());
        movie.setWatchEndDate(request.getWatchEndDate());
        movie.setRating(request.getRating());
        movie.setNotes(request.getNotes());
        movie.setCreatedAt(LocalDateTime.now());

        movie.setGenres(genreService.getOrCreateGenres(request.getGenre()));
        movie.setLanguages(languageService.getOrCreateLanguage(request.getLanguage()));
        movie.setCountries(countryService.getOrCreateCountries(request.getCountry()));

        movie.setUser(SecurityUtils.getCurrentUser());

        return movieRepository.save(movie);
    }

    private Series createSeries(SeriesManualRequestDTO request) {
        Series series = new Series();
        series.setImdbId(null);
        series.setTitle(request.getTitle());
        series.setSeason(request.getSeason());
        series.setReleaseYear(request.getReleaseYear());
        series.setPoster(request.getPosterUrl());

        series.setStatus(parseStatus(request.getStatus()));
        series.setWatchStartDate(request.getWatchStartDate());
        series.setWatchEndDate(request.getWatchEndDate());
        series.setRating(request.getRating());
        series.setNotes(request.getNotes());
        series.setCreatedAt(LocalDateTime.now());

        series.setGenres(genreService.getOrCreateGenres(request.getGenre()));
        series.setLanguages(languageService.getOrCreateLanguage(request.getLanguage()));
        series.setCountries(countryService.getOrCreateCountries(request.getCountry()));

        series.setUser(SecurityUtils.getCurrentUser());

        return seriesRepository.save(series);
    }

    private void createFallbackEpisodes(Series series, int episodeCount) {
        List<Episode> episodes = new ArrayList<>();
        for(int i = 1; i <= episodeCount; i++) {
            Episode episode = new Episode();
            episode.setTitle("Episode: " + i);
            episode.setEpisode(i);
            episode.setStatus(series.getStatus() == StatusEnum.COMPLETED ? EpisodeStatusEnum.COMPLETED : EpisodeStatusEnum.NONE);
            episode.setRating(0);
            episode.setNotes("");
            episode.setSeries(series);
            episodes.add(episode);
        }
        episodeRepository.saveAll(episodes);
        series.setEpisodeList(episodes);
    }
}
