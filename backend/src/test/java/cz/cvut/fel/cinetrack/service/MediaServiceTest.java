/*
 * Created by minmin_tranova on 29.11.2025
 */

package cz.cvut.fel.cinetrack.service;

import cz.cvut.fel.cinetrack.dto.media.FilterOptionsDTO;
import cz.cvut.fel.cinetrack.dto.media.MediaItemDTO;
import cz.cvut.fel.cinetrack.dto.media.request.FilterRequestDTO;
import cz.cvut.fel.cinetrack.dto.media.request.MovieCreateRequestDTO;
import cz.cvut.fel.cinetrack.dto.media.request.SeriesCreateRequestDTO;
import cz.cvut.fel.cinetrack.exception.media.InvalidDatesException;
import cz.cvut.fel.cinetrack.exception.media.InvalidRatingException;
import cz.cvut.fel.cinetrack.exception.media.existingData.MovieAlreadyExistsException;
import cz.cvut.fel.cinetrack.exception.media.existingData.SeriesAlreadyExistsException;
import cz.cvut.fel.cinetrack.exception.media.nonNullData.DatesCannotBeNullException;
import cz.cvut.fel.cinetrack.exception.media.nonNullData.MediaInputCannotBeNullException;
import cz.cvut.fel.cinetrack.model.Country;
import cz.cvut.fel.cinetrack.model.Genre;
import cz.cvut.fel.cinetrack.model.Movie;
import cz.cvut.fel.cinetrack.model.Series;
import cz.cvut.fel.cinetrack.model.User;
import cz.cvut.fel.cinetrack.model.enums.MediaType;
import cz.cvut.fel.cinetrack.model.enums.StatusEnum;
import cz.cvut.fel.cinetrack.repository.CountryRepository;
import cz.cvut.fel.cinetrack.repository.GenreRepository;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private CountryRepository countryRepository;

    private User user;
    private User otherUser;
    private Movie movie;
    private Series series;
    private Genre genre1, genre2;
    private Country country1, country2;
    private final String SORT_BY = "created_at_desc";

    @BeforeEach
    void setUp() {
        user = new User(); setUserParameters(user, "test@test.com", "mainUser");
        userRepository.save(user);

        otherUser = new User(); setUserParameters(otherUser, "test1@test.com", "otherUser");
        userRepository.save(otherUser);

        genre1 = new Genre(); genre1.setType("Action"); genreRepository.save(genre1);
        genre2 = new Genre(); genre2.setType("Drama"); genreRepository.save(genre2);

        country1 = new Country(); country1.setCountryName("United States"); countryRepository.save(country1);
        country2 = new Country(); country2.setCountryName("United Kingdom"); countryRepository.save(country2);

        movie = new Movie(); setMovieParameters(movie, user);
        movie.getGenres().add(genre1); movie.getCountries().add(country1);
        movie.setReleaseYear(2020);
        movie.setStatus(StatusEnum.WATCHING);
        movieRepository.save(movie);

        series = new Series(); setSeriesParameters(series, user);
        series.getGenres().add(genre2); series.getCountries().add(country2);
        series.setReleaseYear(2022);
        series.setStatus(StatusEnum.COMPLETED);
        series.setWatchEndDate(LocalDate.now());
        seriesRepository.save(series);
    }

    @Test
    void getUserMedia_WhenUserHasMovieAndSeries_ReturnsMixedList() {
        List<MediaItemDTO> response = mediaService.getUserMedia(user.getId(), SORT_BY, null);

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

        List<MediaItemDTO> response = mediaService.getUserMedia(otherUser.getId(), SORT_BY, null);
        assertNotNull(response);
        assertEquals(2, response.size());
        assertTrue(response.stream().allMatch(item -> item.getType().equals(MediaType.MOVIE)));
    }

    @Test
    void getUserMedia_WhenUserHasOnlySeries_ReturnsSeries() {
        Series series1 = new Series(); setSeriesParameters(series1, otherUser);
        Series series2 = new Series(); setSeriesParameters(series2, otherUser);
        seriesRepository.save(series1); seriesRepository.save(series2);

        List<MediaItemDTO> response = mediaService.getUserMedia(otherUser.getId(), SORT_BY, null);
        assertNotNull(response);
        assertEquals(2, response.size());
        assertTrue(response.stream().allMatch(item -> item.getType().equals(MediaType.SERIES)));
    }

    @Test
    void getUserMedia_WhenUserHasNoMedia_ReturnsEmptyList() {
        List<MediaItemDTO> response = mediaService.getUserMedia(otherUser.getId(), SORT_BY, null);
        assertNotNull(response);
        assertEquals(0, response.size());
        assertTrue(response.isEmpty());
    }

    @Test
    void getUserMedia_WhenMultipleUser_ReturnsOnlyRequestedUserMedia() {
        Movie movie1 = new Movie(); setMovieParameters(movie1, otherUser);
        movieRepository.save(movie1);

        List<MediaItemDTO> userResponse = mediaService.getUserMedia(user.getId(), SORT_BY, null);
        List<MediaItemDTO> otherUserResponse = mediaService.getUserMedia(otherUser.getId(), SORT_BY, null);

        assertEquals(2, userResponse.size());
        assertEquals(1, otherUserResponse.size());
    }

    @Test
    void getUserMedia_WhenDeletedMedia_ExcludesDeletedItems() {
        Movie deletedMovie = new Movie(); setMovieParameters(deletedMovie, true, user);
        movieRepository.save(deletedMovie);
        Series deletedSeries = new Series(); setSeriesParameters(deletedSeries, true, user);
        seriesRepository.save(deletedSeries);

        List<MediaItemDTO> response = mediaService.getUserMedia(user.getId(), SORT_BY, null);
        assertEquals(2, response.size());
    }

    @ParameterizedTest
    @MethodSource("provideTypeFilterScenarios")
    void getUserMedia_WithTypeFilter_ReturnsCorrectMedia(
            MediaType filterType, int expectedCount, MediaType expectedType
    ) {
        FilterRequestDTO filter = new FilterRequestDTO();
        filter.setTypes(Collections.singletonList(filterType));
        List<MediaItemDTO> response = mediaService.getUserMedia(user.getId(), SORT_BY, filter);

        assertEquals(expectedCount, response.size());
        if (expectedCount > 0) {
            assertEquals(expectedType, response.getFirst().getType());
        }
    }

    private static Stream<Arguments> provideTypeFilterScenarios() {
        return Stream.of(
                Arguments.of(MediaType.MOVIE, 1, MediaType.MOVIE),
                Arguments.of(MediaType.SERIES, 1, MediaType.SERIES)
        );
    }

    @ParameterizedTest
    @MethodSource("provideStatusFilterScenarios")
    void getUserMedia_WithStatusFilter_ReturnsCorrectMedia(
            StatusEnum filterStatus, int expectedCount, StatusEnum expectedStatus
    ) {
        FilterRequestDTO filter = new FilterRequestDTO();
        filter.setStatuses(Collections.singletonList(filterStatus));
        List<MediaItemDTO> response = mediaService.getUserMedia(user.getId(), SORT_BY, filter);

        assertEquals(expectedCount, response.size());
        if (expectedCount > 0) {
            assertEquals(expectedStatus, response.getFirst().getStatus());
        }
    }

    private static Stream<Arguments> provideStatusFilterScenarios() {
        return Stream.of(
                Arguments.of(StatusEnum.WATCHING, 1, StatusEnum.WATCHING),
                Arguments.of(StatusEnum.COMPLETED, 1, StatusEnum.COMPLETED),
                Arguments.of(StatusEnum.PAUSED, 0, null),
                Arguments.of(StatusEnum.DROPPED, 0, null),
                Arguments.of(StatusEnum.PLAN_TO_WATCH, 0, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideGenreFilterScenarios")
    void getUserMedia_WithGenreFilter_ReturnsCorrectMedia(
            Long genreId, int expectedCount, String expectedGenre
    ) {
        FilterRequestDTO filter = new FilterRequestDTO();
        filter.setGenreIds(Collections.singletonList(genreId));
        List<MediaItemDTO> response = mediaService.getUserMedia(user.getId(), SORT_BY, filter);

        assertEquals(expectedCount, response.size());
        if (expectedCount > 0) {
            assertTrue(response.getFirst().getGenres().contains(expectedGenre));
        }
    }

    private static Stream<Arguments> provideGenreFilterScenarios() {
        return Stream.of(
                Arguments.of(1L, 1, "Action"),
                Arguments.of(2L, 1, "Drama")
        );
    }

    @ParameterizedTest
    @MethodSource("provideReleaseYearFilterScenarios")
    void getUserMedia_WithReleaseYearFilter_ReturnsCorrectMedia(
            Integer releaseYear, int expectedCount, Integer expectedReleaseYear
    ) {
        FilterRequestDTO filter = new FilterRequestDTO();
        filter.setReleaseYears(Collections.singletonList(releaseYear));
        List<MediaItemDTO> response = mediaService.getUserMedia(user.getId(), SORT_BY, filter);

        assertEquals(expectedCount, response.size());
        if (expectedCount > 0) {
            assertEquals(expectedReleaseYear, response.getFirst().getReleaseYear());
        }
    }

    private static Stream<Arguments> provideReleaseYearFilterScenarios() {
        return Stream.of(
                Arguments.of(2020, 1, 2020),
                Arguments.of(2022, 1, 2022),
                Arguments.of(2021, 0, null),
                Arguments.of(2019, 0, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideCountryFilterScenarios")
    void getUserMedia_WithCountryFilter_ReturnsCorrectMedia(
            Long countryId, int expectedCount
    ) {
        FilterRequestDTO filter = new FilterRequestDTO();
        filter.setCountryIds(Collections.singletonList(countryId));
        List<MediaItemDTO> response = mediaService.getUserMedia(user.getId(), SORT_BY, filter);

        assertEquals(expectedCount, response.size());
    }

    private static Stream<Arguments> provideCountryFilterScenarios() {
        return Stream.of(
                Arguments.of(1L, 1),
                Arguments.of(2L, 1),
                Arguments.of(999L, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideMultipleValuesFilterScenarios")
    void getUserMedia_WithMultipleValuesFilter_ReturnsCorrectMedia(
            List<MediaType> types,
            List<StatusEnum> statuses,
            List<Long> genreIds,
            List<Integer> years,
            List<Long> countryIds,
            int expectedCount
    ) {
        FilterRequestDTO filter = new FilterRequestDTO();
        filter.setTypes(types);
        filter.setStatuses(statuses);
        filter.setGenreIds(genreIds);
        filter.setReleaseYears(years);
        filter.setCountryIds(countryIds);

        List<MediaItemDTO> response = mediaService.getUserMedia(user.getId(), SORT_BY, filter);
        assertEquals(expectedCount, response.size());
    }

    private static Stream<Arguments> provideMultipleValuesFilterScenarios() {
        return Stream.of(
                Arguments.of(Arrays.asList(MediaType.MOVIE, MediaType.SERIES), null, null, null, null, 2),
                Arguments.of(null, Arrays.asList(StatusEnum.WATCHING, StatusEnum.COMPLETED), null, null, null, 2),
                Arguments.of(null, null, Arrays.asList(1L, 2L), null, null, 2),
                Arguments.of(null, null, null, Arrays.asList(2020, 2022), null, 2),
                Arguments.of(null, null, null, null, Arrays.asList(1L, 2L), 2),
                Arguments.of(
                        List.of(MediaType.MOVIE),
                        List.of(StatusEnum.WATCHING),
                        List.of(1L),
                        List.of(2020),
                        List.of(1L),
                        1
                ),
                Arguments.of(null, null, List.of(1L), null, List.of(2L), 0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideEmptyNullFilterScenarios")
    void getUserMedia_WithEmptyNullFilter_ReturnsCorrectMedia(FilterRequestDTO filter) {
        List<MediaItemDTO> response = mediaService.getUserMedia(user.getId(), SORT_BY, filter);
        assertEquals(2, response.size());
    }

    private static Stream<Arguments> provideEmptyNullFilterScenarios() {
        FilterRequestDTO filter = new FilterRequestDTO();
        filter.setTypes(new ArrayList<>());
        filter.setStatuses(new ArrayList<>());
        filter.setGenreIds(new ArrayList<>());
        filter.setReleaseYears(new ArrayList<>());
        filter.setCountryIds(new ArrayList<>());
        return Stream.of(
                Arguments.of((FilterRequestDTO) null),
                Arguments.of(filter),
                Arguments.of(new FilterRequestDTO())
        );
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

    @Test
    void getFilterOptions_ReturnsAllAvailableOptions() {
        FilterOptionsDTO options = mediaService.getFilterOptions(user.getId());

        assertNotNull(options);

        // types
        assertEquals(2, options.getTypes().size());
        assertTrue(options.getTypes().contains(MediaType.MOVIE));
        assertTrue(options.getTypes().contains(MediaType.SERIES));

        // statuses
        assertEquals(5, options.getStatuses().size());

        // genres
        assertEquals(2, options.getGenres().size());
        assertTrue(options.getGenres().stream().anyMatch(g -> g.getType().equals("Action")));
        assertTrue(options.getGenres().stream().anyMatch(g -> g.getType().equals("Drama")));

        // released years
        assertEquals(2, options.getReleaseYears().size());
        assertTrue(options.getReleaseYears().contains(2020));
        assertTrue(options.getReleaseYears().contains(2022));

        // countries
        assertEquals(2, options.getCountries().size());
        assertTrue(options.getCountries().stream().anyMatch(c -> c.getCountryName().equals("United States")));
        assertTrue(options.getCountries().stream().anyMatch(c -> c.getCountryName().equals("United Kingdom")));
    }

    @Test
    void getFilterOptions_ForDifferentUsers_ReturnsDifferentOptions() {
        FilterOptionsDTO options = mediaService.getFilterOptions(otherUser.getId());

        assertNotNull(options);
        assertEquals(2, options.getTypes().size());
        assertEquals(5, options.getStatuses().size());

        assertTrue(options.getGenres().isEmpty());
        assertTrue(options.getReleaseYears().isEmpty());
        assertTrue(options.getCountries().isEmpty());
    }

}
