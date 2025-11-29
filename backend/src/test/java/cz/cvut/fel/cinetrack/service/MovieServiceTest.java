/*
 * Created by minmin_tranova on 29.11.2025
 */

package cz.cvut.fel.cinetrack.service;

import cz.cvut.fel.cinetrack.dto.media.request.ChangeStatusRequestDTO;
import cz.cvut.fel.cinetrack.dto.media.request.EditMediaRequestDTO;
import cz.cvut.fel.cinetrack.dto.media.response.MovieResponseDTO;
import cz.cvut.fel.cinetrack.exception.media.InvalidDatesException;
import cz.cvut.fel.cinetrack.exception.media.InvalidRatingException;
import cz.cvut.fel.cinetrack.exception.media.nonNullData.MediaInputCannotBeNullException;
import cz.cvut.fel.cinetrack.exception.media.notFoundObj.MovieNotFoundException;
import cz.cvut.fel.cinetrack.model.Movie;
import cz.cvut.fel.cinetrack.model.User;
import cz.cvut.fel.cinetrack.model.enums.StatusEnum;
import cz.cvut.fel.cinetrack.repository.MovieRepository;
import cz.cvut.fel.cinetrack.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static cz.cvut.fel.cinetrack.environment.SetMovieParameters.setMovieParameters;
import static cz.cvut.fel.cinetrack.environment.SetUserParameters.setUserParameters;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@ComponentScan(basePackages = "cz.cvut.fel.cinetrack")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MovieServiceTest {

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;

    private Movie movie;
    private User user;
    private User otherUser;

    @BeforeEach
    void setUp() {
        user = new User(); setUserParameters(user, "test@test.com", "mainUser");
        userRepository.save(user);

        otherUser = new User(); setUserParameters(otherUser, "test1@test.com", "otherUser");
        userRepository.save(otherUser);

        movie = new Movie(); setMovieParameters(movie, user);
        movieRepository.save(movie);
    }

    @Test
    void getMovieById_WhenMovieExistsAndUserOwnsIt_ReturnsMovie() {
        MovieResponseDTO response = movieService.getMovieById(movie.getId(), user.getId());
        assertNotNull(response);
        assertEquals(movie.getId(), response.getId());
        assertEquals(movie.getTitle(), response.getTitle());
        assertEquals(movie.getReleaseYear(), response.getReleaseYear());
        assertEquals(movie.getRuntime(), response.getRuntime());
        assertEquals((movie.getPoster()), response.getPoster());
    }

    @Test
    void getMovieById_WhenMovieDoesNotExist_ThrowsMovieNotFoundException() {
        Long nonExistingMovieId = 999L;
        assertThrows(MovieNotFoundException.class, () ->
                movieService.getMovieById(nonExistingMovieId, user.getId()));
    }

    @Test
    void getMovieById_WhenMovieDeleted_ThrowsMovieNotFoundException() {
        Movie deletedMovie = new Movie(); setMovieParameters(deletedMovie, true, otherUser);
        movieRepository.save(deletedMovie);
        assertThrows(MovieNotFoundException.class, () ->
                movieService.getMovieById(deletedMovie.getId(), otherUser.getId()));
    }

    @Test
    void changeMovieStatus_WhenValidStatus_UpdateStatus() {
        ChangeStatusRequestDTO request = new ChangeStatusRequestDTO();
        request.setStatus("dropped");

        MovieResponseDTO response = movieService.changeMovieStatus(movie.getId(), user.getId(), request);
        assertNotNull(response);
        assertEquals(StatusEnum.DROPPED, response.getStatus());

        Movie updatedMovie = movieRepository.findById(movie.getId()).orElseThrow();
        assertEquals(StatusEnum.DROPPED, updatedMovie.getStatus());
    }

    @Test
    void changeMovieStatus_WhenMovieNotFound_ThrowsMovieNotFoundException() {
        Long nonExistingMovieId = 999L;
        ChangeStatusRequestDTO request = new ChangeStatusRequestDTO();
        request.setStatus("dropped");

        assertThrows(MovieNotFoundException.class, () ->
                movieService.changeMovieStatus(nonExistingMovieId, otherUser.getId(), request));
    }

    @Test
    void editMovie_WhenValidRequest_UpdateAllFields() {
        EditMediaRequestDTO request = new EditMediaRequestDTO();
        request.setNotes("Update notes");
        request.setRating("9.5");
        request.setWatchStartDate(LocalDate.now().plusDays(2));
        request.setWatchEndDate(LocalDate.now().plusDays(2));

        MovieResponseDTO response = movieService.editMovie(movie.getId(), user.getId(), request);
        assertNotNull(response);
        assertEquals(movie.getNotes(), response.getNotes());
        assertEquals(movie.getRating(), response.getRating());
        assertEquals(movie.getWatchStartDate(), response.getWatchStartDate());
        assertEquals(movie.getWatchEndDate(), response.getWatchEndDate());

        Movie updateMovie = movieRepository.findById(movie.getId()).orElseThrow();
        assertEquals("Update notes", updateMovie.getNotes());
        assertEquals(9.5, updateMovie.getRating());
        assertEquals(LocalDate.now().plusDays(2), updateMovie.getWatchStartDate());
        assertEquals(LocalDate.now().plusDays(2), updateMovie.getWatchEndDate());
    }

    @Test
    void editMovie_WhenNotesIsNull_ThrowsException() {
        EditMediaRequestDTO request = new EditMediaRequestDTO();
        request.setNotes(null);
        request.setRating("9.5");
        request.setWatchStartDate(LocalDate.now().plusDays(2));

        assertThrows(MediaInputCannotBeNullException.class, () ->
                movieService.editMovie(movie.getId(), user.getId(), request));

        request.setNotes("");

        assertThrows(MediaInputCannotBeNullException.class, () ->
                movieService.editMovie(movie.getId(), user.getId(), request));
    }

    @Test
    void editMovie_WhenRatingIsNull_ThrowsException() {
        EditMediaRequestDTO request = new EditMediaRequestDTO();
        request.setNotes("Update notes");
        request.setRating(null);
        request.setWatchStartDate(LocalDate.now().plusDays(2));

        assertThrows(MediaInputCannotBeNullException.class, () ->
                movieService.editMovie(movie.getId(), user.getId(), request));

        request.setRating("");

        assertThrows(MediaInputCannotBeNullException.class, () ->
                movieService.editMovie(movie.getId(), user.getId(), request));
    }

    @Test
    void editMovie_WhenWatchStartDateIsNull_ThrowsException() {
        EditMediaRequestDTO request = new EditMediaRequestDTO();
        request.setNotes("Update notes");
        request.setRating("9.5");
        request.setWatchStartDate(null);

        assertThrows(MediaInputCannotBeNullException.class, () ->
                movieService.editMovie(movie.getId(), user.getId(), request));
    }

    @Test
    void editMovie_WhenWatchEndDateIsBeforeStartDate_ThrowsException() {
        EditMediaRequestDTO request = new EditMediaRequestDTO();
        request.setNotes("Update notes");
        request.setRating("9.5");
        request.setWatchStartDate(LocalDate.now().plusDays(2));
        request.setWatchEndDate(LocalDate.now().plusDays(1));

        assertThrows(InvalidDatesException.class, () ->
                movieService.editMovie(movie.getId(), user.getId(), request));
    }

    @Test
    void editMovie_WhenRatingIsGreaterThan10_ThrowsException() {
        EditMediaRequestDTO request = new EditMediaRequestDTO();
        request.setNotes("Update notes");
        request.setRating("12");
        request.setWatchStartDate(LocalDate.now().plusDays(2));
        request.setWatchStartDate(LocalDate.now().plusDays(2));

        assertThrows(InvalidRatingException.class, () ->
                movieService.editMovie(movie.getId(), user.getId(), request));
    }

    @Test
    void editMovie_WhenRatingIsLessThan0_ThrowsException() {
        EditMediaRequestDTO request = new EditMediaRequestDTO();
        request.setNotes("Update notes");
        request.setRating("-12");
        request.setWatchStartDate(LocalDate.now().plusDays(2));
        request.setWatchStartDate(LocalDate.now().plusDays(2));

        assertThrows(InvalidRatingException.class, () ->
                movieService.editMovie(movie.getId(), user.getId(), request));
    }

    @Test
    void editMovie_WhenUserNotOwner_ThrowsAccessDeniedException() {
        EditMediaRequestDTO request = new EditMediaRequestDTO();
        request.setNotes("Update notes");
        request.setRating("2");
        request.setWatchStartDate(LocalDate.now().plusDays(2));
        request.setWatchStartDate(LocalDate.now().plusDays(2));

        assertThrows(AccessDeniedException.class, () ->
                movieService.editMovie(movie.getId(), otherUser.getId(), request));
    }

    @Test
    void deleteMovie_WhenMovieExists_MarksAsDeleted() {
        movieService.deleteMovie(movie.getId(), user.getId());
        Optional<Movie> deletedMovie = movieRepository.findById(movie.getId());

        assertTrue(deletedMovie.isPresent());
        assertTrue(deletedMovie.get().isDeleted());
        assertNotNull(deletedMovie.get().getDeletedAt());

        assertThrows(MovieNotFoundException.class, () ->
                movieService.getMovieById(movie.getId(), user.getId()));
    }

    @Test
    void deleteMovie_WhenMovieDoesNotExist_ThrowsMovieNotFoundException() {
        Long nonExistingMovieId = 999L;
        assertThrows(MovieNotFoundException.class, () ->
                movieService.deleteMovie(nonExistingMovieId, user.getId()));
    }

    @Test
    void deleteMovie_WhenUserNotOwner_ThrowsAccessDeniedException() {
        assertThrows(AccessDeniedException.class, () ->
                movieService.deleteMovie(movie.getId(), otherUser.getId()));
    }

    @Test
    void deleteMovie_WhenAlreadyDeleted_ThrowsMovieNotFoundException() {
        movie.setDeleted(true);
        movieRepository.save(movie);
        assertThrows(MovieNotFoundException.class, () ->
                movieService.deleteMovie(movie.getId(), user.getId()));
    }

    @Test
    void checkMovieExistence_WhenMovieExistsAndUserOwnsIt_ReturnsMovie() {
        MovieResponseDTO response = movieService.getMovieById(movie.getId(), user.getId());
        assertNotNull(response);
        assertEquals(movie.getId(), response.getId());
    }

    @Test
    void checkMovieExistence_WhenMovieDoesNotExist_ThrowsMovieNotFoundException() {
        Long nonExistingMovieId = 999L;
        assertThrows(MovieNotFoundException.class, () ->
                movieService.getMovieById(nonExistingMovieId, user.getId()));
    }

    @Test
    void checkMovieExistence_WhenMovieExistsButUserDoesNotOwn_ThrowsAccessDeniedException() {
        assertThrows(AccessDeniedException.class, () ->
                movieService.getMovieById(movie.getId(), otherUser.getId()));
    }

    @Test
    void checkMovieExistence_WhenMovieDeleted_ThrowsMovieNotFoundException() {
        movie.setDeleted(true); movieRepository.save(movie);
        assertThrows(MovieNotFoundException.class, () ->
                movieService.getMovieById(movie.getId(), user.getId()));
    }
}
