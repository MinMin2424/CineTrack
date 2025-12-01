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
import cz.cvut.fel.cinetrack.model.Movie;
import cz.cvut.fel.cinetrack.model.Series;
import cz.cvut.fel.cinetrack.model.enums.ValidationMessage;
import cz.cvut.fel.cinetrack.repository.MovieRepository;
import cz.cvut.fel.cinetrack.repository.SeriesRepository;
import cz.cvut.fel.cinetrack.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static cz.cvut.fel.cinetrack.util.MediaUtils.parseStatus;
import static cz.cvut.fel.cinetrack.validator.MediaValidator.*;

@Service
@Transactional
public class MediaManualService {

    private final MovieRepository movieRepository;
    private final SeriesRepository seriesRepository;
    private final LanguageService languageService;
    private final GenreService genreService;
    private final CountryService countryService;

    public MediaManualService(MovieRepository movieRepository,
                              SeriesRepository seriesRepository,
                              LanguageService languageService,
                              GenreService genreService,
                              CountryService countryService
    ) {
        this.movieRepository = movieRepository;
        this.seriesRepository = seriesRepository;
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

        series.setGenres(genreService.getOrCreateGenres(request.getGenres()));
        series.setLanguages(languageService.getOrCreateLanguage(request.getLanguages()));
        series.setCountries(countryService.getOrCreateCountries(request.getCountries()));

        series.setUser(SecurityUtils.getCurrentUser());

        return seriesRepository.save(series);
    }
}
