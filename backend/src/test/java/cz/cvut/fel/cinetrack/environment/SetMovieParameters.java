/*
 * Created by minmin_tranova on 29.11.2025
 */

package cz.cvut.fel.cinetrack.environment;

import cz.cvut.fel.cinetrack.dto.media.request.MovieCreateRequestDTO;
import cz.cvut.fel.cinetrack.model.Movie;
import cz.cvut.fel.cinetrack.model.User;
import cz.cvut.fel.cinetrack.model.enums.StatusEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SetMovieParameters {

    public static void setMovieParameters(Movie movie, User user) {
        movie.setTitle("Test Movie");
        movie.setRuntime("215 min");
        movie.setReleaseYear(2025);
        movie.setPoster("...");
        movie.setStatus(StatusEnum.WATCHING);
        movie.setWatchStartDate(LocalDate.now());
        movie.setRating(10);
        movie.setNotes("Notes");
        movie.setCreatedAt(LocalDateTime.now());
        movie.setDeleted(false);
        movie.setUser(user);
    }

    public static void setMovieParameters(Movie movie, boolean deleted, User user) {
        movie.setTitle("Test Movie");
        movie.setRuntime("215 min");
        movie.setReleaseYear(2025);
        movie.setPoster("...");
        movie.setStatus(StatusEnum.WATCHING);
        movie.setWatchStartDate(LocalDate.now());
        movie.setRating(10);
        movie.setNotes("Notes");
        movie.setCreatedAt(LocalDateTime.now());
        movie.setDeleted(deleted);
        movie.setUser(user);
    }

    public static void setMovieParameters(Movie movie, String imdbId, User user) {
        movie.setImdbId(imdbId);
        movie.setTitle("Test Movie");
        movie.setRuntime("215 min");
        movie.setReleaseYear(2025);
        movie.setPoster("...");
        movie.setStatus(StatusEnum.WATCHING);
        movie.setWatchStartDate(LocalDate.now());
        movie.setRating(10);
        movie.setNotes("Notes");
        movie.setCreatedAt(LocalDateTime.now());
        movie.setDeleted(false);
        movie.setUser(user);
    }

    public static MovieCreateRequestDTO createBaseMovieRequest() {
        MovieCreateRequestDTO request = new MovieCreateRequestDTO();
        request.setImdbID("tt1234567");
        request.setTitle("New Movie");
        request.setYear("2025");
        request.setRuntime("250 min");
        request.setCountry("United States, France");
        request.setLanguage("English");
        request.setGenre("Action");
        request.setPoster("...");
        request.setStatus("completed");
        request.setNotes("Notes");
        request.setRating("2.0");
        request.setWatchStartDate(LocalDate.now());
        request.setWatchEndDate(LocalDate.now().plusDays(1));
        return request;
    }
}
