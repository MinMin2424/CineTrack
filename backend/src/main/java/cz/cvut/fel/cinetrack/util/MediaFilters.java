/*
 * Created by minmin_tranova on 06.12.2025
 */

package cz.cvut.fel.cinetrack.util;

import cz.cvut.fel.cinetrack.dto.media.request.FilterRequestDTO;
import cz.cvut.fel.cinetrack.model.Country;
import cz.cvut.fel.cinetrack.model.Genre;
import cz.cvut.fel.cinetrack.model.Movie;
import cz.cvut.fel.cinetrack.model.Series;
import cz.cvut.fel.cinetrack.model.enums.MediaType;
import cz.cvut.fel.cinetrack.model.enums.StatusEnum;

import java.util.List;

public class MediaFilters {

    public static List<Movie> applyFiltersToMovies(List<Movie> movies, FilterRequestDTO filters) {
        return movies.stream()
                .filter(movie -> filterByType(movie, filters))
                .filter(movie -> filterByStatus(movie, filters))
                .filter(movie -> filterByGenres(movie, filters))
                .filter(movie -> filterByReleaseYear(movie, filters))
                .filter(movie -> filterByCountry(movie, filters))
                .toList();
    }

    public static List<Series> applyFiltersToSeries(List<Series> series, FilterRequestDTO filters) {
        return series.stream()
                .filter(s -> filterByType(s, filters))
                .filter(s -> filterByStatus(s, filters))
                .filter(s-> filterByGenres(s, filters))
                .filter(s -> filterByReleaseYear(s, filters))
                .filter(s -> filterByCountry(s, filters))
                .toList();
    }

    private static boolean filterByType(Object media, FilterRequestDTO filters) {
        if (filters == null || filters.getTypes() == null || filters.getTypes().isEmpty()) {
            return true;
        }
        if (media instanceof Movie) {
            return filters.getTypes().contains(MediaType.MOVIE);
        } else if (media instanceof Series) {
            return filters.getTypes().contains(MediaType.SERIES);
        }
        return false;
    }

    private static boolean filterByStatus(Object media, FilterRequestDTO filters) {
        if (filters == null || filters.getStatuses() == null || filters.getStatuses().isEmpty()) {
            return true;
        }
        StatusEnum mediaStatus = null;
        if (media instanceof Movie) {
            mediaStatus = ((Movie) media).getStatus();
        } else if (media instanceof Series) {
            mediaStatus = ((Series) media).getStatus();
        }
        return mediaStatus != null && filters.getStatuses().contains(mediaStatus);
    }

    private static boolean filterByGenres(Object media, FilterRequestDTO filters) {
        if (filters == null || filters.getGenreIds() == null || filters.getGenreIds().isEmpty()) {
            return true;
        }
        List<Long> mediaGenreIds = null;
        if (media instanceof Movie) {
            mediaGenreIds = ((Movie) media).getGenres().stream()
                    .map(Genre::getId)
                    .toList();
        } else if (media instanceof Series) {
            mediaGenreIds = ((Series) media).getGenres().stream()
                    .map(Genre::getId)
                    .toList();
        }
        if (mediaGenreIds == null) {
            return false;
        }
        return mediaGenreIds.stream().anyMatch(filters.getGenreIds()::contains);
    }

    private static boolean filterByReleaseYear(Object media, FilterRequestDTO filters) {
        if (filters == null || filters.getReleaseYears() == null || filters.getReleaseYears().isEmpty()) {
            return true;
        }
        Integer mediaYear = null;
        if (media instanceof Movie) {
            mediaYear = ((Movie) media).getReleaseYear();
        } else if (media instanceof Series) {
            mediaYear = ((Series) media).getReleaseYear();
        }
        return mediaYear != null && filters.getReleaseYears().contains(mediaYear);
    }

    private static boolean filterByCountry(Object media, FilterRequestDTO filters) {
        if (filters == null || filters.getCountryIds() == null || filters.getCountryIds().isEmpty()) {
            return true;
        }
        List<Long> mediaCountryIds = null;
        if (media instanceof Movie) {
            mediaCountryIds = ((Movie) media).getCountries().stream()
                    .map(Country::getId)
                    .toList();
        } else if (media instanceof Series) {
            mediaCountryIds = ((Series) media).getCountries().stream()
                    .map((Country::getId))
                    .toList();
        }
        if (mediaCountryIds == null) {
            return false;
        }
        return mediaCountryIds.stream().anyMatch(filters.getCountryIds()::contains);
    }
}
