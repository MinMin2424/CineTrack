/*
 * Created by minmin_tranova on 29.11.2025
 */

package cz.cvut.fel.cinetrack.service;

import cz.cvut.fel.cinetrack.dto.media.request.ChangeStatusRequestDTO;
import cz.cvut.fel.cinetrack.dto.media.request.EditMediaRequestDTO;
import cz.cvut.fel.cinetrack.dto.media.response.SeriesResponseDTO;
import cz.cvut.fel.cinetrack.exception.media.InvalidDatesException;
import cz.cvut.fel.cinetrack.exception.media.InvalidRatingException;
import cz.cvut.fel.cinetrack.exception.media.nonNullData.MediaInputCannotBeNullException;
import cz.cvut.fel.cinetrack.exception.media.notFoundObj.SeriesNotFoundException;
import cz.cvut.fel.cinetrack.model.Series;
import cz.cvut.fel.cinetrack.model.User;
import cz.cvut.fel.cinetrack.model.enums.StatusEnum;
import cz.cvut.fel.cinetrack.repository.SeriesRepository;
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

import static cz.cvut.fel.cinetrack.environment.SetSeriesParameters.setSeriesParameters;
import static cz.cvut.fel.cinetrack.environment.SetUserParameters.setUserParameters;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@ComponentScan(basePackages = "cz.cvut.fel.cinetrack")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SeriesServiceTest {

    @Autowired
    private SeriesService seriesService;

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private UserRepository userRepository;

    private Series series;
    private User user;
    private User otherUser;

    @BeforeEach
    void setUp() {
        user = new User(); setUserParameters(user, "test@test.com", "mainUser");
        userRepository.save(user);

        otherUser = new User(); setUserParameters(otherUser, "test1@test.com", "otherUser");
        userRepository.save(otherUser);

        series = new Series(); setSeriesParameters(series, user);
        seriesRepository.save(series);
    }

    @Test
    void getSeriesById_WhenSeriesExistsAndUserOwnsIt_ReturnsSeries() {
        SeriesResponseDTO response = seriesService.getSeriesById(series.getId(), user.getId());
        assertNotNull(response);
        assertEquals(series.getId(), response.getId());
        assertEquals(series.getTitle(), response.getTitle());
        assertEquals(series.getReleaseYear(), response.getReleaseYear());
        assertEquals(series.getSeason(), response.getSeason());
        assertEquals((series.getPoster()), response.getPoster());
    }

    @Test
    void getSeriesById_WhenSeriesDoesNotExist_ThrowsSeriesNotFoundException() {
        Long nonExistingSeriesId = 999L;
        assertThrows(SeriesNotFoundException.class, () ->
                seriesService.getSeriesById(nonExistingSeriesId, user.getId()));
    }

    @Test
    void getSeriesById_WhenSeriesDeleted_ThrowsSeriesNotFoundException() {
        Series deletedSeries = new Series(); setSeriesParameters(deletedSeries, true, otherUser);
        seriesRepository.save(deletedSeries);
        assertThrows(SeriesNotFoundException.class, () ->
                seriesService.getSeriesById(deletedSeries.getId(), otherUser.getId()));
    }

    @Test
    void changeSeriesStatus_WhenValidStatus_UpdateStatus() {
        ChangeStatusRequestDTO request = new ChangeStatusRequestDTO();
        request.setStatus("dropped");

        SeriesResponseDTO response = seriesService.changeSeriesStatus(series.getId(), user.getId(), request);
        assertNotNull(response);
        assertEquals(StatusEnum.DROPPED, response.getStatus());

        Series updatedSeries = seriesRepository.findById(series.getId()).orElseThrow();
        assertEquals(StatusEnum.DROPPED, updatedSeries.getStatus());
    }

    @Test
    void changeSeriesStatus_WhenSeriesNotFound_ThrowsSeriesNotFoundException() {
        Long nonExistingSeriesId = 999L;
        ChangeStatusRequestDTO request = new ChangeStatusRequestDTO();
        request.setStatus("dropped");

        assertThrows(SeriesNotFoundException.class, () ->
                seriesService.changeSeriesStatus(nonExistingSeriesId, otherUser.getId(), request));
    }

    @Test
    void editSeries_WhenValidRequest_UpdateAllFields() {
        EditMediaRequestDTO request = new EditMediaRequestDTO();
        request.setNotes("Update notes");
        request.setRating("9.5");
        request.setWatchStartDate(LocalDate.now().plusDays(2));
        request.setWatchEndDate(LocalDate.now().plusDays(2));

        SeriesResponseDTO response = seriesService.editSeries(series.getId(), user.getId(), request);
        assertNotNull(response);
        assertEquals(series.getNotes(), response.getNotes());
        assertEquals(series.getRating(), response.getRating());
        assertEquals(series.getWatchStartDate(), response.getWatchStartDate());
        assertEquals(series.getWatchEndDate(), response.getWatchEndDate());

        Series updatedSeries = seriesRepository.findById(series.getId()).orElseThrow();
        assertEquals("Update notes", updatedSeries.getNotes());
        assertEquals(9.5, updatedSeries.getRating());
        assertEquals(LocalDate.now().plusDays(2), updatedSeries.getWatchStartDate());
        assertEquals(LocalDate.now().plusDays(2), updatedSeries.getWatchEndDate());
    }

    @Test
    void editSeries_WhenWatchEndDateIsBeforeStartDate_ThrowsException() {
        EditMediaRequestDTO request = new EditMediaRequestDTO();
        request.setNotes("Update notes");
        request.setRating("9.5");
        request.setWatchStartDate(LocalDate.now().plusDays(2));
        request.setWatchEndDate(LocalDate.now().plusDays(1));

        assertThrows(InvalidDatesException.class, () ->
                seriesService.editSeries(series.getId(), user.getId(), request));
    }

    @Test
    void editSeries_WhenRatingIsGreaterThan10_ThrowsException() {
        EditMediaRequestDTO request = new EditMediaRequestDTO();
        request.setNotes("Update notes");
        request.setRating("12");
        request.setWatchStartDate(LocalDate.now().plusDays(2));
        request.setWatchStartDate(LocalDate.now().plusDays(2));

        assertThrows(InvalidRatingException.class, () ->
                seriesService.editSeries(series.getId(), user.getId(), request));
    }

    @Test
    void editSeries_WhenRatingIsLessThan0_ThrowsException() {
        EditMediaRequestDTO request = new EditMediaRequestDTO();
        request.setNotes("Update notes");
        request.setRating("-12");
        request.setWatchStartDate(LocalDate.now().plusDays(2));
        request.setWatchStartDate(LocalDate.now().plusDays(2));

        assertThrows(InvalidRatingException.class, () ->
                seriesService.editSeries(series.getId(), user.getId(), request));
    }

    @Test
    void editSeries_WhenUserNotOwner_ThrowsAccessDeniedException() {
        EditMediaRequestDTO request = new EditMediaRequestDTO();
        request.setNotes("Update notes");
        request.setRating("2");
        request.setWatchStartDate(LocalDate.now().plusDays(2));
        request.setWatchStartDate(LocalDate.now().plusDays(2));

        assertThrows(AccessDeniedException.class, () ->
                seriesService.editSeries(series.getId(), otherUser.getId(), request));
    }

    @Test
    void deleteSeries_WhenMovieExists_MarksAsDeleted() {
        seriesService.deleteSeries(series.getId(), user.getId());
        Optional<Series> deletedSeries = seriesRepository.findById(series.getId());

        assertTrue(deletedSeries.isPresent());
        assertTrue(deletedSeries.get().isDeleted());
        assertNotNull(deletedSeries.get().getDeletedAt());

        assertThrows(SeriesNotFoundException.class, () ->
                seriesService.getSeriesById(series.getId(), user.getId()));
    }

    @Test
    void deleteSeries_WhenSeriesDoesNotExist_ThrowsSeriesNotFoundException() {
        Long nonExistingMovieId = 999L;
        assertThrows(SeriesNotFoundException.class, () ->
                seriesService.deleteSeries(nonExistingMovieId, user.getId()));
    }

    @Test
    void deleteSeries_WhenUserNotOwner_ThrowsAccessDeniedException() {
        assertThrows(AccessDeniedException.class, () ->
                seriesService.deleteSeries(series.getId(), otherUser.getId()));
    }

    @Test
    void deleteSeries_WhenAlreadyDeleted_ThrowsSeriesNotFoundException() {
        series.setDeleted(true);
        seriesRepository.save(series);
        assertThrows(SeriesNotFoundException.class, () ->
                seriesService.deleteSeries(series.getId(), user.getId()));
    }

    @Test
    void checkSeriesExistence_WhenSeriesExistsAndUserOwnsIt_ReturnsSeries() {
        SeriesResponseDTO response = seriesService.getSeriesById(series.getId(), user.getId());
        assertNotNull(response);
        assertEquals(series.getId(), response.getId());
    }

    @Test
    void checkSeriesExistence_WhenSeriesDoesNotExist_ThrowsSeriesNotFoundException() {
        Long nonExistingSeriesId = 999L;
        assertThrows(SeriesNotFoundException.class, () ->
                seriesService.getSeriesById(nonExistingSeriesId, user.getId()));
    }

    @Test
    void checkSeriesExistence_WhenSeriesExistsButUserDoesNotOwn_ThrowsAccessDeniedException() {
        assertThrows(AccessDeniedException.class, () ->
                seriesService.getSeriesById(series.getId(), otherUser.getId()));
    }

    @Test
    void checkSeriesExistence_WhenSeriesDeleted_ThrowsSeriesNotFoundException() {
        series.setDeleted(true); seriesRepository.save(series);
        assertThrows(SeriesNotFoundException.class, () ->
                seriesService.getSeriesById(series.getId(), user.getId()));
    }

}
