/*
 * Created by minmin_tranova on 20.11.2025
 */

package cz.cvut.fel.cinetrack.mapper;

import cz.cvut.fel.cinetrack.dto.media.request.MovieCreateRequestDTO;
import cz.cvut.fel.cinetrack.dto.media.request.SeriesCreateRequestDTO;
import cz.cvut.fel.cinetrack.model.Movie;
import cz.cvut.fel.cinetrack.model.Series;
import cz.cvut.fel.cinetrack.security.SecurityUtils;
import cz.cvut.fel.cinetrack.service.CountryService;
import cz.cvut.fel.cinetrack.service.GenreService;
import cz.cvut.fel.cinetrack.service.LanguageService;

import static cz.cvut.fel.cinetrack.util.MediaUtils.*;
import static cz.cvut.fel.cinetrack.util.MediaUtils.parseStringToList;
import static cz.cvut.fel.cinetrack.validator.MediaValidator.validateRating;

public class MediaMapper {

    public static void mapMovieDTOToEntity(
            MovieCreateRequestDTO dto,
            Movie movie,
            LanguageService languageService,
            CountryService countryService,
            GenreService genreService
    ) {
        float rating = parseStringToFloat(dto.getRating());
        validateRating(rating);

        movie.setImdbId(dto.getImdbID());
        movie.setTitle(dto.getTitle());
        movie.setReleaseYear(parseYear(dto.getYear()));
        movie.setRuntime(parseRuntime(dto.getRuntime()));
        movie.setPoster(dto.getPoster());

        movie.setStatus(parseStatus(dto.getStatus()));
        movie.setRating(rating);
        movie.setNotes(dto.getNotes());
        movie.setWatchStartDate(dto.getWatchStartDate());
        movie.setWatchEndDate(dto.getWatchEndDate());

        movie.setLanguages(languageService.getOrCreateLanguage(parseStringToList(dto.getLanguage())));
        movie.setCountries(countryService.getOrCreateCountries(parseStringToList(dto.getCountry())));
        movie.setGenres(genreService.getOrCreateGenres(parseStringToList(dto.getGenre())));

        movie.setUser(SecurityUtils.getCurrentUser());
    }

    public static void mapSeriesDTOToEntity(
            SeriesCreateRequestDTO dto,
            Series series,
            LanguageService languageService,
            CountryService countryService,
            GenreService genreService
    ) {
        float rating = parseStringToFloat(dto.getRating());
        validateRating(rating);

        series.setImdbId(dto.getImdbID());
        series.setTitle(dto.getTitle() + " " + parseStringToInt(dto.getSeason()));
        //series.setReleaseYear(parseYear(dto.getYear()));
        series.setPoster(dto.getPoster());
        series.setSeason(parseStringToInt(dto.getSeason()));
        //series.setEpisodes(parseStringToInt(dto.getEpisodes()));

        series.setStatus(parseStatus(dto.getStatus()));
        series.setRating(rating);
        series.setNotes(dto.getNotes());
        series.setWatchStartDate(dto.getWatchStartDate());
        series.setWatchEndDate(dto.getWatchEndDate());

        series.setLanguages(languageService.getOrCreateLanguage(parseStringToList(dto.getLanguage())));
        series.setCountries(countryService.getOrCreateCountries(parseStringToList(dto.getCountry())));
        series.setGenres(genreService.getOrCreateGenres(parseStringToList(dto.getGenre())));

        series.setUser(SecurityUtils.getCurrentUser());
    }
}
