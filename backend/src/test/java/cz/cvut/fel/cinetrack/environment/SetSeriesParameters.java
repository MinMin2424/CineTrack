/*
 * Created by minmin_tranova on 29.11.2025
 */

package cz.cvut.fel.cinetrack.environment;

import cz.cvut.fel.cinetrack.dto.media.request.SeriesCreateRequestDTO;
import cz.cvut.fel.cinetrack.model.Series;
import cz.cvut.fel.cinetrack.model.User;
import cz.cvut.fel.cinetrack.model.enums.StatusEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SetSeriesParameters {

    public static void setSeriesParameters(Series series, User user) {
        series.setImdbId(null);
        series.setTitle("Test Series");
        series.setSeason(1);
        series.setReleaseYear(2025);
        series.setPoster("...");
        series.setEpisodes(1);
        series.setStatus(StatusEnum.WATCHING);
        series.setWatchStartDate(LocalDate.now());
        series.setRating(10);
        series.setNotes("Good");
        series.setCreatedAt(LocalDateTime.now());
        series.setDeleted(false);
        series.setUser(user);
    }

    public static void setSeriesParameters(Series series, boolean deleted, User user) {
        series.setImdbId(null);
        series.setTitle("Test Series");
        series.setSeason(1);
        series.setReleaseYear(2025);
        series.setPoster("...");
        series.setEpisodes(1);
        series.setStatus(StatusEnum.WATCHING);
        series.setWatchStartDate(LocalDate.now());
        series.setRating(10);
        series.setNotes("Good");
        series.setCreatedAt(LocalDateTime.now());
        series.setDeleted(deleted);
        series.setUser(user);
    }

    public static void setSeriesParameters(Series series, String imdbId, User user) {
        series.setImdbId(imdbId);
        series.setTitle("Test Series");
        series.setSeason(1);
        series.setReleaseYear(2025);
        series.setPoster("...");
        series.setEpisodes(1);
        series.setStatus(StatusEnum.WATCHING);
        series.setWatchStartDate(LocalDate.now());
        series.setRating(10);
        series.setNotes("Good");
        series.setCreatedAt(LocalDateTime.now());
        series.setDeleted(false);
        series.setUser(user);
    }

    public static SeriesCreateRequestDTO createBaseSeriesRequest() {
        SeriesCreateRequestDTO request = new SeriesCreateRequestDTO();
        request.setImdbID("tt1234567");
        request.setTitle("Test Series");
        request.setCountry("United States, France");
        request.setLanguage("English");
        request.setGenre("Action");
        request.setPoster("...");
        request.setSeason("1");
        request.setStatus("completed");
        request.setNotes("Notes");
        request.setRating("2.0");
        request.setWatchStartDate(LocalDate.now());
        request.setWatchEndDate(LocalDate.now().plusDays(1));
        return request;
    }
}
