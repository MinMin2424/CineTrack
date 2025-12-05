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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static cz.cvut.fel.cinetrack.environment.SetMovieParameters.createBaseMovieRequest;
import static cz.cvut.fel.cinetrack.environment.SetMovieParameters.setMovieParameters;
import static cz.cvut.fel.cinetrack.environment.SetSeriesParameters.createBaseSeriesRequest;
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
    private final String SORT_BY = "created_at_desc";

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
        List<MediaItemDTO> response = mediaService.getUserMedia(user.getId(), SORT_BY);

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

        List<MediaItemDTO> response = mediaService.getUserMedia(otherUser.getId(), SORT_BY);
        assertNotNull(response);
        assertEquals(2, response.size());
        assertTrue(response.stream().allMatch(item -> item.getType().equals(MediaType.MOVIE)));
    }

    @Test
    void getUserMedia_WhenUserHasOnlySeries_ReturnsSeries() {
        Series series1 = new Series(); setSeriesParameters(series1, otherUser);
        Series series2 = new Series(); setSeriesParameters(series2, otherUser);
        seriesRepository.save(series1); seriesRepository.save(series2);

        List<MediaItemDTO> response = mediaService.getUserMedia(otherUser.getId(), SORT_BY);
        assertNotNull(response);
        assertEquals(2, response.size());
        assertTrue(response.stream().allMatch(item -> item.getType().equals(MediaType.SERIES)));
    }

    @Test
    void getUserMedia_WhenUserHasNoMedia_ReturnsEmptyList() {
        List<MediaItemDTO> response = mediaService.getUserMedia(otherUser.getId(), SORT_BY);
        assertNotNull(response);
        assertEquals(0, response.size());
        assertTrue(response.isEmpty());
    }

    @Test
    void getUserMedia_WhenMultipleUser_ReturnsOnlyRequestedUserMedia() {
        Movie movie1 = new Movie(); setMovieParameters(movie1, otherUser);
        movieRepository.save(movie1);

        List<MediaItemDTO> userResponse = mediaService.getUserMedia(user.getId(), SORT_BY);
        List<MediaItemDTO> otherUserResponse = mediaService.getUserMedia(otherUser.getId(), SORT_BY);

        assertEquals(2, userResponse.size());
        assertEquals(1, otherUserResponse.size());
    }

    @Test
    void getUserMedia_WhenDeletedMedia_ExcludesDeletedItems() {
        Movie deletedMovie = new Movie(); setMovieParameters(deletedMovie, true, user);
        movieRepository.save(deletedMovie);
        Series deletedSeries = new Series(); setSeriesParameters(deletedSeries, true, user);
        seriesRepository.save(deletedSeries);

        List<MediaItemDTO> response = mediaService.getUserMedia(user.getId(), SORT_BY);
        assertEquals(2, response.size());
    }

    @Test
    void createMovie_WhenMovieAlreadyExists_ThrowsException() {
        Movie existingMovie = new Movie(); setMovieParameters(existingMovie, "tt1234567", otherUser);
        movieRepository.save(existingMovie);
        MovieCreateRequestDTO request = createBaseMovieRequest();
        assertThrows(MovieAlreadyExistsException.class, () ->
                mediaService.createMovie(request, otherUser.getId()));
    }

    @ParameterizedTest
    @MethodSource("provideMovieNullEmptyFields")
    void createMovie_WhenFieldIsNullOrEmpty_ThrowsException(
            String fieldName, String value, String methodCall
    ) {
        MovieCreateRequestDTO request = createBaseMovieRequest();
        switch(methodCall) {
            case "setTitle" -> request.setTitle(value);
            case "setRuntime" -> request.setRuntime(value);
            case "setYear" -> request.setYear(value);
            case "setPoster" -> request.setPoster(value);
            case "setStatus" -> request.setStatus(value);
        }
        assertThrows(MediaInputCannotBeNullException.class, () ->
                mediaService.createMovie(request, otherUser.getId()));
    }

    private static Stream<Arguments> provideMovieNullEmptyFields() {
        return Stream.of(
                Arguments.of("title", null, "setTitle"),
                Arguments.of("title", "", "setTitle"),
                Arguments.of("runtime", null, "setRuntime"),
                Arguments.of("runtime", "", "setRuntime"),
                Arguments.of("year", null, "setYear"),
                Arguments.of("year", "", "setYear"),
                Arguments.of("poster", null, "setPoster"),
                Arguments.of("poster", "", "setPoster"),
                Arguments.of("status", null, "setStatus"),
                Arguments.of("status", "", "setStatus")
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"11", "-1", "10.5", "-0.1"})
    void createMovie_WhenRatingIsInvalid_ThrowsException(String invalidRating) {
        MovieCreateRequestDTO request = createBaseMovieRequest();
        request.setRating(invalidRating);
        assertThrows(InvalidRatingException.class, () ->
                mediaService.createMovie(request, otherUser.getId()));
    }

    @ParameterizedTest
    @MethodSource("provideMediaDateScenarios")
    void createMovie_WhenDatesAreInvalid_ThrowsException(
            LocalDate startDate,
            LocalDate endDate,
            String status,
            Class<? extends Exception> expectedException
    ) {
        MovieCreateRequestDTO request = createBaseMovieRequest();
        request.setStatus(status);
        request.setWatchStartDate(startDate);
        request.setWatchEndDate(endDate);
        assertThrows(expectedException, () ->
                mediaService.createMovie(request, otherUser.getId()));
    }

    private static Stream<Arguments> provideMediaDateScenarios() {
        LocalDate today = LocalDate.now();
        return Stream.of(
                Arguments.of(null, today, "completed", DatesCannotBeNullException.class),
                Arguments.of(today, null, "completed", DatesCannotBeNullException.class),
                Arguments.of(today, today.minusDays(1), "completed", InvalidDatesException.class)
        );
    }

    @Test
    void createSeries_WhenSeriesAlreadyExists_ThrowsException() {
        Series existingSeries = new Series(); setSeriesParameters(existingSeries, "tt1234567", otherUser);
        seriesRepository.save(existingSeries);
        SeriesCreateRequestDTO request = createBaseSeriesRequest();
        assertThrows(SeriesAlreadyExistsException.class, () ->
                mediaService.createSeries(request, otherUser.getId()));
    }

    @ParameterizedTest
    @MethodSource("provideSeriesNullEmptyFields")
    void createSeries_WhenFieldIsNullOrEmpty_ThrowsException(
            String fieldName, String value, String methodCall
    ) {
        SeriesCreateRequestDTO request = createBaseSeriesRequest();
        switch(methodCall) {
            case "setTitle" -> request.setTitle(value);
            case "setSeason" -> request.setSeason(value);
            case "setPoster" -> request.setPoster(value);
            case "setStatus" -> request.setStatus(value);
        }
        assertThrows(MediaInputCannotBeNullException.class, () ->
                mediaService.createSeries(request, otherUser.getId()));
    }

    private static Stream<Arguments> provideSeriesNullEmptyFields() {
        return Stream.of(
                Arguments.of("title", null, "setTitle"),
                Arguments.of("title", "", "setTitle"),
                Arguments.of("season", null, "setSeason"),
                Arguments.of("season", "", "setSeason"),
                Arguments.of("poster", null, "setPoster"),
                Arguments.of("poster", "", "setPoster"),
                Arguments.of("status", null, "setStatus"),
                Arguments.of("status", "", "setStatus")
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"11", "-1", "10.5", "-0.1"})
    void createSeries_WhenRatingIsInvalid_ThrowsException(String invalidRating) {
        SeriesCreateRequestDTO request = createBaseSeriesRequest();
        request.setRating(invalidRating);
        assertThrows(InvalidRatingException.class, () ->
                mediaService.createSeries(request, otherUser.getId()));
    }

    @ParameterizedTest
    @MethodSource("provideMediaDateScenarios")
    void createSeries_WhenDatesAreInvalid_ThrowsException(
            LocalDate startDate,
            LocalDate endDate,
            String status,
            Class<? extends Exception> expectedException
    ) {
        SeriesCreateRequestDTO request = createBaseSeriesRequest();
        request.setStatus(status);
        request.setWatchStartDate(startDate);
        request.setWatchEndDate(endDate);
        assertThrows(expectedException, () ->
                mediaService.createSeries(request, otherUser.getId()));
    }

}
