/*
 * Created by minmin_tranova on 20.11.2025
 */

package cz.cvut.fel.cinetrack.controller;

import cz.cvut.fel.cinetrack.dto.media.request.ChangeEpisodeStatusRequestDTO;
import cz.cvut.fel.cinetrack.dto.media.request.EditEpisodeRequestDTO;
import cz.cvut.fel.cinetrack.dto.media.response.EpisodeResponseDTO;
import cz.cvut.fel.cinetrack.exception.media.notFoundObj.EpisodeNotFoundException;
import cz.cvut.fel.cinetrack.model.User;
import cz.cvut.fel.cinetrack.security.SecurityUtils;
import cz.cvut.fel.cinetrack.service.EpisodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.AccessDeniedException;

import java.util.Map;

@RestController
@RequestMapping("/series/{seriesId}/episodes")
public class EpisodeController {

    private final EpisodeService episodeService;

    public EpisodeController(EpisodeService episodeService) {
        this.episodeService = episodeService;
    }

    @GetMapping("/{episodeNumber}")
    public ResponseEntity<?> getEpisode(
            @PathVariable("seriesId") Long seriesId,
            @PathVariable("episodeNumber") int episodeNumber
    ) {
        try {
            User user = SecurityUtils.getCurrentUser();
            EpisodeResponseDTO episode = episodeService.getEpisodeByNumber(seriesId, episodeNumber, user.getId());
            return ResponseEntity.ok(episode);
        } catch (EpisodeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{episodeNumber}/status")
    public ResponseEntity<?> changeEpisodeStatus(
            @PathVariable("seriesId") Long seriesId,
            @PathVariable("episodeNumber") int episodeNumber,
            @RequestBody ChangeEpisodeStatusRequestDTO request
    ) {
        try {
            User user = SecurityUtils.getCurrentUser();
            EpisodeResponseDTO episode = episodeService.changeEpisodeStatus(seriesId, episodeNumber, user.getId(), request);
            return ResponseEntity.ok(episode);
        } catch (EpisodeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{episodeNumber}")
    public ResponseEntity<?> editEpisode(
            @PathVariable("seriesId") Long seriesId,
            @PathVariable("episodeNumber") int episodeNumber,
            @RequestBody EditEpisodeRequestDTO request
    ) {
        try {
            User user = SecurityUtils.getCurrentUser();
            EpisodeResponseDTO episode = episodeService.editEpisode(seriesId, episodeNumber, user.getId(), request);
            return ResponseEntity.ok(episode);
        } catch (EpisodeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
