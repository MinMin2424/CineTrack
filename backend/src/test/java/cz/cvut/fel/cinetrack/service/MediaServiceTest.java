/*
 * Created by minmin_tranova on 29.11.2025
 */

package cz.cvut.fel.cinetrack.service;

import cz.cvut.fel.cinetrack.dto.media.MediaItemDTO;
import cz.cvut.fel.cinetrack.dto.media.request.MovieCreateRequestDTO;
import cz.cvut.fel.cinetrack.dto.media.request.SeriesCreateRequestDTO;
import cz.cvut.fel.cinetrack.exception.media.InvalidDatesException;
import cz.cvut.fel.cinetrack.exception.media.InvalidRatingException;
import cz.cvut.fel.cinetrack.exception.media.existingData.MovieAlreadyExistsException;
import cz.cvut.fel.cinetrack.exception.media.existingData.SeriesAlreadyExistsException;
import cz.cvut.fel.cinetrack.exception.media.nonNullData.DatesCannotBeNullException;
import cz.cvut.fel.cinetrack.exception.media.nonNullData.MediaInputCannotBeNullException;
import cz.cvut.fel.cinetrack.model.Movie;
import cz.cvut.fel.cinetrack.model.Series;
import cz.cvut.fel.cinetrack.model.User;
import cz.cvut.fel.cinetrack.model.enums.MediaType;
import cz.cvut.fel.cinetrack.repository.MovieRepository;
import cz.cvut.fel.cinetrack.repository.SeriesRepository;
import cz.cvut.fel.cinetrack.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static cz.cvut.fel.cinetrack.environment.SetMovieParameters.setMovieParameters;
import static cz.cvut.fel.cinetrack.environment.SetSeriesParameters.setSeriesParameters;
import static cz.cvut.fel.cinetrack.environment.SetUserParameters.setUserParameters;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@ComponentScan(basePackages = "cz.cvut.fel.cinetrack")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MediaServiceTest {

    @Autowired
    private MediaService mediaService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private User otherUser;
    private Movie movie;
    private Series series;

    @BeforeEach
    void setUp() {
        user = new User(); setUserParameters(user, "test@test.com", "mainUser");
        userRepository.save(user);

        otherUser = new User(); setUserParameters(otherUser, "test1@test.com", "otherUser");
        userRepository.save(otherUser);

        movie = new Movie(); setMovieParameters(movie, user);
        movieRepository.save(movie);

        series = new Series(); setSeriesParameters(series, user);
        seriesRepository.save(series);
    }

    @Test
    void getUserMedia_WhenUserHasMovieAndSeries_ReturnsMixedList() {
        List<MediaItemDTO> response = mediaService.getUserMedia(user.getId());

        assertNotNull(response);
        assertEquals(2, response.size());
        assertTrue(response.stream().anyMatch(item -> item.getTitle().equals("Test Movie")));
        assertTrue(response.stream().anyMatch(item -> item.getTitle().equals("Test Series")));
    }

    @Test
    void getUserMedia_WhenUserHasOnlyMovies_ReturnsMovies() {
        Movie movie1 = new Movie(); setMovieParameters(movie1, otherUser);
        Movie movie2 = new Movie(); setMovieParameters(movie2, otherUser);
        movieRepository.save(movie1); movieRepository.save(movie2);

        List<MediaItemDTO> response = mediaService.getUserMedia(otherUser.getId());
        assertNotNull(response);
        assertEquals(2, response.size());
        assertTrue(response.stream().allMatch(item -> item.getType().equals(MediaType.MOVIE)));
    }

    @Test
    void getUserMedia_WhenUserHasOnlySeries_ReturnsSeries() {
        Series series1 = new Series(); setSeriesParameters(series1, otherUser);
        Series series2 = new Series(); setSeriesParameters(series2, otherUser);
        seriesRepository.save(series1); seriesRepository.save(series2);

        List<MediaItemDTO> response = mediaService.getUserMedia(otherUser.getId());
        assertNotNull(response);
        assertEquals(2, response.size());
        assertTrue(response.stream().allMatch(item -> item.getType().equals(MediaType.SERIES)));
    }

    @Test
    void getUserMedia_WhenUserHasNoMedia_ReturnsEmptyList() {
        List<MediaItemDTO> response = mediaService.getUserMedia(otherUser.getId());
        assertNotNull(response);
        assertEquals(0, response.size());
        assertTrue(response.isEmpty());
    }

    @Test
    void getUserMedia_WhenMultipleUser_ReturnsOnlyRequestedUserMedia() {
        Movie movie1 = new Movie(); setMovieParameters(movie1, otherUser);
        movieRepository.save(movie1);

        List<MediaItemDTO> userResponse = mediaService.getUserMedia(user.getId());
        List<MediaItemDTO> otherUserResponse = mediaService.getUserMedia(otherUser.getId());

        assertEquals(2, userResponse.size());
        assertEquals(1, otherUserResponse.size());
    }

    @Test
    void getUserMedia_WhenDeletedMedia_ExcludesDeletedItems() {
        Movie deletedMovie = new Movie(); setMovieParameters(deletedMovie, true, user);
        movieRepository.save(deletedMovie);
        Series deletedSeries = new Series(); setSeriesParameters(deletedSeries, true, user);
        seriesRepository.save(deletedSeries);

        List<MediaItemDTO> response = mediaService.getUserMedia(user.getId());
        assertEquals(2, response.size());
    }

    @Test
    void createMovie_WhenMovieAlreadyExists_ThrowsException() {
        Movie existingMovie = new Movie(); setMovieParameters(existingMovie, "tt1234567", otherUser);
        movieRepository.save(existingMovie);
        MovieCreateRequestDTO request = new MovieCreateRequestDTO();
        request.setImdbID("tt1234567");
        request.setTitle("New Movie");
        request.setYear("2025");
        request.setRuntime("250 min");
        request.setCountry("United States, France");
        request.setLanguage("English");
        request.setGenre("Action");
        request.setPoster("...");
        request.setStatus("plan to watch");
        request.setNotes("Notes");
        request.setRating("2.0");
        request.setWatchStartDate(LocalDate.now());
        request.setWatchEndDate(null);

        assertThrows(MovieAlreadyExistsException.class, () ->
                mediaService.createMovie(request, otherUser.getId()));
    }

    @Test
    void createMovie_WhenStatusIsCompletedAndStartDateIsNull_ThrowsException() {
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
        request.setWatchStartDate(null);
        request.setWatchEndDate(LocalDate.now());

        assertThrows(DatesCannotBeNullException.class, () ->
                mediaService.createMovie(request, otherUser.getId()));
    }

    @Test
    void createMovie_WhenStatusIsCompletedAndEndDateIsNull_ThrowsException() {
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
        request.setWatchEndDate(null);

        assertThrows(DatesCannotBeNullException.class, () ->
                mediaService.createMovie(request, otherUser.getId()));
    }

    @Test
    void createMovie_WhenEndDateIsBeforeStartDate_ThrowsException() {
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
        request.setWatchEndDate(LocalDate.now().minusDays(1));

        assertThrows(InvalidDatesException.class, () ->
                mediaService.createMovie(request, otherUser.getId()));
    }

    @Test
    void createMovie_WhenRatingIsGreaterThan10_ThrowsException() {
        MovieCreateRequestDTO request = new MovieCreateRequestDTO();
        request.setImdbID("tt1234567");
        request.setTitle("New Movie");
        request.setYear("2025");
        request.setRuntime("250 min");
        request.setCountry("United States, France");
        request.setLanguage("English");
        request.setGenre("Action");
        request.setPoster("...");
        request.setStatus("watching");
        request.setNotes("Notes");
        request.setRating("11");
        request.setWatchStartDate(LocalDate.now());
        request.setWatchEndDate(null);

        assertThrows(InvalidRatingException.class, () ->
                mediaService.createMovie(request, otherUser.getId()));
    }

    @Test
    void createMovie_WhenRatingIsLessThan0_ThrowsException() {
        MovieCreateRequestDTO request = new MovieCreateRequestDTO();
        request.setImdbID("tt1234567");
        request.setTitle("New Movie");
        request.setYear("2025");
        request.setRuntime("250 min");
        request.setCountry("United States, France");
        request.setLanguage("English");
        request.setGenre("Action");
        request.setPoster("...");
        request.setStatus("watching");
        request.setNotes("Notes");
        request.setRating("-11");
        request.setWatchStartDate(LocalDate.now());
        request.setWatchEndDate(null);

        assertThrows(InvalidRatingException.class, () ->
                mediaService.createMovie(request, otherUser.getId()));
    }

    @Test
    void createMovie_WhenTitleIsNull_ThrowsException() {
        MovieCreateRequestDTO request = new MovieCreateRequestDTO();
        request.setTitle(null);

        assertThrows(MediaInputCannotBeNullException.class, () ->
                mediaService.createMovie(request, otherUser.getId()));

        request.setTitle("");

        assertThrows(MediaInputCannotBeNullException.class, () ->
                mediaService.createMovie(request, otherUser.getId()));
    }

    @Test
    void createMovie_WhenRuntimeIsNull_ThrowsException() {
        MovieCreateRequestDTO request = new MovieCreateRequestDTO();
        request.setRuntime(null);

        assertThrows(MediaInputCannotBeNullException.class, () ->
                mediaService.createMovie(request, otherUser.getId()));

        request.setRuntime("");

        assertThrows(MediaInputCannotBeNullException.class, () ->
                mediaService.createMovie(request, otherUser.getId()));
    }

    @Test
    void createMovie_WhenYearIsNull_ThrowsException() {
        MovieCreateRequestDTO request = new MovieCreateRequestDTO();
        request.setYear(null);

        assertThrows(MediaInputCannotBeNullException.class, () ->
                mediaService.createMovie(request, otherUser.getId()));

        request.setYear("");

        assertThrows(MediaInputCannotBeNullException.class, () ->
                mediaService.createMovie(request, otherUser.getId()));
    }

    @Test
    void createMovie_WhenPosterIsNull_ThrowsException() {
        MovieCreateRequestDTO request = new MovieCreateRequestDTO();
        request.setPoster(null);

        assertThrows(MediaInputCannotBeNullException.class, () ->
                mediaService.createMovie(request, otherUser.getId()));

        request.setPoster("");

        assertThrows(MediaInputCannotBeNullException.class, () ->
                mediaService.createMovie(request, otherUser.getId()));
    }

    @Test
    void createMovie_WhenStatusIsNull_ThrowsException() {
        MovieCreateRequestDTO request = new MovieCreateRequestDTO();
        request.setStatus(null);

        assertThrows(MediaInputCannotBeNullException.class, () ->
                mediaService.createMovie(request, otherUser.getId()));

        request.setStatus("");

        assertThrows(MediaInputCannotBeNullException.class, () ->
                mediaService.createMovie(request, otherUser.getId()));
    }

    @Test
    void createSeries_WhenSeriesAlreadyExists_ThrowsException() {
        Series existingSeries = new Series(); setSeriesParameters(existingSeries, "tt1234567", otherUser);
        seriesRepository.save(existingSeries);
        SeriesCreateRequestDTO request = new SeriesCreateRequestDTO();
        request.setImdbID("tt1234567");
        request.setTitle("New Series");
        request.setCountry("United States, France");
        request.setLanguage("English");
        request.setGenre("Action");
        request.setPoster("...");
        request.setSeason("1");
        request.setStatus("plan to watch");
        request.setNotes("Notes");
        request.setRating("2.0");
        request.setWatchStartDate(LocalDate.now());
        request.setWatchEndDate(null);

        assertThrows(SeriesAlreadyExistsException.class, () ->
                mediaService.createSeries(request, otherUser.getId()));
    }

    @Test
    void createSeries_WhenStatusIsCompletedAndStartDateIsNull_ThrowsException() {
        SeriesCreateRequestDTO request = new SeriesCreateRequestDTO();
        request.setImdbID("tt1234567");
        request.setTitle("New Series");
        request.setCountry("United States, France");
        request.setLanguage("English");
        request.setGenre("Action");
        request.setPoster("...");
        request.setSeason("2");
        request.setStatus("completed");
        request.setNotes("Notes");
        request.setRating("2.0");
        request.setWatchStartDate(null);
        request.setWatchEndDate(LocalDate.now());

        assertThrows(DatesCannotBeNullException.class, () ->
                mediaService.createSeries(request, otherUser.getId()));
    }


    @Test
    void createSeries_WhenStatusIsCompletedAndEndDateIsNull_ThrowsException() {
        SeriesCreateRequestDTO request = new SeriesCreateRequestDTO();
        request.setImdbID("tt1234567");
        request.setTitle("New Series");
        request.setCountry("United States, France");
        request.setLanguage("English");
        request.setGenre("Action");
        request.setPoster("...");
        request.setSeason("2");
        request.setStatus("completed");
        request.setNotes("Notes");
        request.setRating("2.0");
        request.setWatchStartDate(LocalDate.now());
        request.setWatchEndDate(null);

        assertThrows(DatesCannotBeNullException.class, () ->
                mediaService.createSeries(request, otherUser.getId()));
    }

    @Test
    void createSeries_WhenRatingIsGreaterThan10_ThrowsException() {
        SeriesCreateRequestDTO request = new SeriesCreateRequestDTO();
        request.setImdbID("tt1234567");
        request.setTitle("New Series");
        request.setCountry("United States, France");
        request.setLanguage("English");
        request.setGenre("Action");
        request.setPoster("...");
        request.setSeason("2");
        request.setStatus("watching");
        request.setNotes("Notes");
        request.setRating("11");
        request.setWatchStartDate(LocalDate.now());
        request.setWatchEndDate(null);

        assertThrows(InvalidRatingException.class, () ->
                mediaService.createSeries(request, otherUser.getId()));
    }

    @Test
    void createSeries_WhenRatingIsLessThan0_ThrowsException() {
        SeriesCreateRequestDTO request = new SeriesCreateRequestDTO();
        request.setImdbID("tt1234567");
        request.setTitle("New Series");
        request.setCountry("United States, France");
        request.setLanguage("English");
        request.setGenre("Action");
        request.setPoster("...");
        request.setSeason("2");
        request.setStatus("watching");
        request.setNotes("Notes");
        request.setRating("-11");
        request.setWatchStartDate(LocalDate.now());
        request.setWatchEndDate(null);

        assertThrows(InvalidRatingException.class, () ->
                mediaService.createSeries(request, otherUser.getId()));
    }

    @Test
    void createSeries_WhenTitleIsNull_ThrowsException() {
        SeriesCreateRequestDTO request = new SeriesCreateRequestDTO();
        request.setTitle(null);

        assertThrows(MediaInputCannotBeNullException.class, () ->
                mediaService.createSeries(request, otherUser.getId()));

        request.setTitle("");

        assertThrows(MediaInputCannotBeNullException.class, () ->
                mediaService.createSeries(request, otherUser.getId()));
    }

    @Test
    void createSeries_WhenSeasonIsNull_ThrowsException() {
        SeriesCreateRequestDTO request = new SeriesCreateRequestDTO();
        request.setSeason(null);

        assertThrows(MediaInputCannotBeNullException.class, () ->
                mediaService.createSeries(request, otherUser.getId()));

        request.setSeason("");

        assertThrows(MediaInputCannotBeNullException.class, () ->
                mediaService.createSeries(request, otherUser.getId()));
    }

    @Test
    void createSeries_WhenStatusIsNull_ThrowsException() {
        SeriesCreateRequestDTO request = new SeriesCreateRequestDTO();
        request.setStatus(null);

        assertThrows(MediaInputCannotBeNullException.class, () ->
                mediaService.createSeries(request, otherUser.getId()));

        request.setStatus("");

        assertThrows(MediaInputCannotBeNullException.class, () ->
                mediaService.createSeries(request, otherUser.getId()));
    }
}
