/*
 * Created by minmin_tranova on 20.11.2025
 */

package cz.cvut.fel.cinetrack.controller;

import cz.cvut.fel.cinetrack.dto.media.request.ChangeStatusRequestDTO;
import cz.cvut.fel.cinetrack.dto.media.request.EditMediaRequestDTO;
import cz.cvut.fel.cinetrack.dto.media.response.SeriesResponseDTO;
import cz.cvut.fel.cinetrack.exception.media.SeriesNotFoundException;
import cz.cvut.fel.cinetrack.model.User;
import cz.cvut.fel.cinetrack.security.SecurityUtils;
import cz.cvut.fel.cinetrack.service.SeriesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/series")
public class SeriesController {

    private final SeriesService seriesService;

    public SeriesController(SeriesService seriesService) {
        this.seriesService = seriesService;
    }

    @GetMapping("/{seriesId}")
    public ResponseEntity<?> getSeries(@PathVariable Long seriesId) {
        try {
            User user = SecurityUtils.getCurrentUser();
            SeriesResponseDTO series = seriesService.getSeriesById(seriesId, user.getId());
            return ResponseEntity.ok(series);
        } catch (SeriesNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("{seriesId}/status")
    public ResponseEntity<?> changeSeriesStatus(
            @PathVariable("seriesId") Long seriesId,
            @RequestBody ChangeStatusRequestDTO request
    ) {
        try {
            User user = SecurityUtils.getCurrentUser();
            SeriesResponseDTO series = seriesService.changeSeriesStatus(seriesId, user.getId(), request);
            return ResponseEntity.ok(series);
        } catch (SeriesNotFoundException e) {
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

    @PutMapping("{seriesId}")
    public ResponseEntity<?> editSeries(
            @PathVariable("seriesId") Long seriesId,
            @RequestBody EditMediaRequestDTO request
    ) {
        try {
            User user = SecurityUtils.getCurrentUser();
            SeriesResponseDTO series = seriesService.editSeries(seriesId, user.getId(), request);
            return ResponseEntity.ok(series);
        } catch (SeriesNotFoundException e) {
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

    @DeleteMapping("{seriesId}")
    public ResponseEntity<?> deleteSeries(@PathVariable("seriesId") Long seriesId) {
        try {
            User user = SecurityUtils.getCurrentUser();
            seriesService.deleteSeries(seriesId, user.getId());
            return ResponseEntity.ok().build();
        } catch (SeriesNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
