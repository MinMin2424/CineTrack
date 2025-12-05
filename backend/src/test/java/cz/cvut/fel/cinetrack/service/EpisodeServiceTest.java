/*
 * Created by minmin_tranova on 29.11.2025
 */

package cz.cvut.fel.cinetrack.service;
import cz.cvut.fel.cinetrack.dto.media.request.ChangeEpisodeStatusRequestDTO;
import cz.cvut.fel.cinetrack.dto.media.request.EditEpisodeRequestDTO;
import cz.cvut.fel.cinetrack.dto.media.response.EpisodeResponseDTO;
import cz.cvut.fel.cinetrack.exception.media.InvalidRatingException;
import cz.cvut.fel.cinetrack.exception.media.notFoundObj.EpisodeNotFoundException;
import cz.cvut.fel.cinetrack.exception.media.notFoundObj.SeriesNotFoundException;
import cz.cvut.fel.cinetrack.model.Episode;
import cz.cvut.fel.cinetrack.model.Series;
import cz.cvut.fel.cinetrack.model.User;
import cz.cvut.fel.cinetrack.model.enums.EpisodeStatusEnum;
import cz.cvut.fel.cinetrack.repository.EpisodeRepository;
import cz.cvut.fel.cinetrack.repository.SeriesRepository;
import cz.cvut.fel.cinetrack.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static cz.cvut.fel.cinetrack.environment.SetSeriesParameters.setSeriesParameters;
import static cz.cvut.fel.cinetrack.environment.SetUserParameters.setUserParameters;
import static cz.cvut.fel.cinetrack.environment.SetEpisodeParameters.setEpisodeParameters;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@ComponentScan(basePackages = "cz.cvut.fel.cinetrack")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class EpisodeServiceTest {

    @Autowired
    private EpisodeService episodeService;

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private UserRepository userRepository;

    private Series testSeries;
    private Episode testEpisode;
    private User user;
    private User otherUser;

    @BeforeEach
    void setUp() {
        user = new User(); setUserParameters(user, "test@test.com", "mainUser");
        user = userRepository.save(user);

        otherUser = new User(); setUserParameters(otherUser, "test1@test.com", "otherUser");
        otherUser = userRepository.save(otherUser);

        testSeries = new Series(); setSeriesParameters(testSeries, user);
        testSeries = seriesRepository.save(testSeries);

        testEpisode = new Episode(); setEpisodeParameters(testEpisode, 1, testSeries);
        testEpisode = episodeRepository.save(testEpisode);
    }

    @Test
    void getEpisodeByNumber_WhenEpisodeExists_ReturnEpisode() {
        EpisodeResponseDTO response = episodeService.getEpisodeByNumber(testSeries.getId(), 1, user.getId());

        assertNotNull(response);
        assertEquals(testEpisode.getEpisode(), response.getEpisode());
        assertEquals(testEpisode.getStatus(), response.getStatus());
        assertEquals(testEpisode.getTitle(), response.getTitle());
        assertEquals(testEpisode.getRating(), response.getRating());
        assertEquals(testEpisode.getTitle(), response.getTitle());
    }

    @Test
    void getEpisodeByNumber_WhenSeriesNotFound_ThrowsSeriesNotFoundException() {
        Long nonExistingSeriesId = 999L;
        assertThrows(SeriesNotFoundException.class, () ->
                episodeService.getEpisodeByNumber(nonExistingSeriesId, 1, user.getId()));
    }

    @Test
    void getEpisodeByNumber_WhenUserNotOwner_ThrowsAccessDeniedException() {
        assertThrows(AccessDeniedException.class, () ->
                episodeService.getEpisodeByNumber(testEpisode.getId(), 1, otherUser.getId()));
    }

    @Test
    void getEpisodeByNumber_WhenEpisodeNotFound_ThrowsEpisodeNotFoundException() {
        assertThrows(EpisodeNotFoundException.class, () ->
                episodeService.getEpisodeByNumber(testSeries.getId(), 2, user.getId()));
    }

    @ParameterizedTest
    @CsvSource({
            "completed, COMPLETED",
            "watching, WATCHING",
            "plan to watch, PLAN_TO_WATCH",
            "dropped, DROPPED",
            "paused, PAUSED"
    })
    void changeEpisodeStatus_WhenValidRequest_UpdateStatus(
            String statusInput, EpisodeStatusEnum expectedStatus
    ) {
        ChangeEpisodeStatusRequestDTO request = new ChangeEpisodeStatusRequestDTO();
        request.setStatus(statusInput);

        EpisodeResponseDTO response = episodeService.changeEpisodeStatus(testSeries.getId(), 1, user.getId(), request);

        assertNotNull(response);
        assertEquals(expectedStatus, response.getStatus());

        Episode updatedEpisode = episodeRepository.findById(testEpisode.getId()).orElseThrow();
        assertEquals(expectedStatus, updatedEpisode.getStatus());
    }

    @Test
    void editEpisode_WhenValidRequest_UpdateEpisode() {
        EditEpisodeRequestDTO request = new EditEpisodeRequestDTO();
        request.setNotes("Updated notes");
        request.setRating("9.0");

        EpisodeResponseDTO response = episodeService.editEpisode(testSeries.getId(), 1, user.getId(), request);

        assertNotNull(response);
        assertEquals(9.0, response.getRating());
        assertEquals("Updated notes", response.getNotes());

        Episode updatedEpisode = episodeRepository.findById(testEpisode.getId()).orElseThrow();
        assertEquals(9.0, updatedEpisode.getRating());
        assertEquals("Updated notes", updatedEpisode.getNotes());
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1.0", "-0.5", "10.5", "11.0", "100.0", "-10.0"})
    void editEpisode_whenRatingIsInvalid_ThrowsException(String invalidRating) {
        EditEpisodeRequestDTO request = new EditEpisodeRequestDTO();
        request.setRating(invalidRating);
        request.setNotes("Updated notes");

        assertThrows(InvalidRatingException.class, () ->
                episodeService.editEpisode(testSeries.getId(), 1, user.getId(), request));
    }

    @Test
    void editEpisode_WhenRatingLessThan0_ThrowsException() {
        EditEpisodeRequestDTO request = new EditEpisodeRequestDTO();
        request.setRating("-1.0");
        request.setNotes("Updated notes");

        assertThrows(InvalidRatingException.class, () ->
                episodeService.editEpisode(testSeries.getId(), 1, user.getId(), request));
    }

    @Test
    void getEpisodesBySeriesId_WhenSeriesExists_ReturnEpisodes() {
        Episode secondEpisode = new Episode();
        setEpisodeParameters(secondEpisode, 2, testSeries);
        episodeRepository.save(secondEpisode);

        List<EpisodeResponseDTO> response = episodeService.getEpisodesBySeriesId(testSeries.getId(), user.getId());
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(1, response.getFirst().getEpisode());
        assertEquals(2, response.getLast().getEpisode());
    }

    @Test
    void getEpisodesBySeriesId_WhenSeriesNotFound_ThrowsSeriesNotFoundException() {
        Long nonExistingSeriesId = 999L;

        assertThrows(SeriesNotFoundException.class, () ->
                episodeService.getEpisodesBySeriesId(nonExistingSeriesId, user.getId()));
    }

    @Test
    void getEpisodesBySeriesId_WhenUserNotOwner_ThrowsAccessDeniedException() {
        assertThrows(AccessDeniedException.class, () ->
                episodeService.getEpisodesBySeriesId(testSeries.getId(), otherUser.getId()));
    }

    @Test
    void getEpisodesBySeriesId_WhenNoEpisodes_ReturnEmptyList() {
        Series newSeries = new Series();
        setSeriesParameters(newSeries, otherUser);
        newSeries = seriesRepository.save(newSeries);

        List<EpisodeResponseDTO> response = episodeService.getEpisodesBySeriesId(newSeries.getId(), otherUser.getId());
        assertNotNull(response);
        assertEquals(0, response.size());
        assertTrue(response.isEmpty());
    }

    @Test
    void getEpisodeBySeriesId_WhenSeriesDeleted_ThrowsAccessDeniedException() {
        Series deletedSeries = new Series();
        setSeriesParameters(deletedSeries,true, otherUser);
        Series savedSeries = seriesRepository.save(deletedSeries);

        assertThrows(AccessDeniedException.class, () ->
                episodeService.getEpisodesBySeriesId(savedSeries.getId(), otherUser.getId()));
    }

    @Test
    void getEpisodeBySeriesId_ReturnsEpisodesInOrder() {
        Episode thirdEpisode = new Episode();
        setEpisodeParameters(thirdEpisode, 3, testSeries);
        episodeRepository.save(thirdEpisode);

        Episode secondEpisode = new Episode();
        setEpisodeParameters(secondEpisode, 2, testSeries);
        episodeRepository.save(secondEpisode);

        List<EpisodeResponseDTO> response = episodeService.getEpisodesBySeriesId(testSeries.getId(), user.getId());

        assertNotNull(response);
        assertEquals(3, response.size());
        assertEquals(1, response.getFirst().getEpisode());
        assertEquals(2, response.get(1).getEpisode());
        assertEquals(3, response.get(2).getEpisode());
    }


}
