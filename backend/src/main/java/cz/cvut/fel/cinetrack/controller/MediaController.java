/*
 * Created by minmin_tranova on 11.11.2025
 */

package cz.cvut.fel.cinetrack.controller;

import cz.cvut.fel.cinetrack.dto.media.MediaItemDTO;
import cz.cvut.fel.cinetrack.dto.media.request.MovieCreateRequestDTO;
import cz.cvut.fel.cinetrack.dto.media.response.MovieResponseDTO;
import cz.cvut.fel.cinetrack.dto.media.response.omdb.OMDBResponseDTO;
import cz.cvut.fel.cinetrack.dto.media.request.SearchRequestDTO;
import cz.cvut.fel.cinetrack.dto.media.request.SeriesCreateRequestDTO;
import cz.cvut.fel.cinetrack.dto.media.response.SeriesResponseDTO;
import cz.cvut.fel.cinetrack.dto.media.response.SeriesSearchResponseDTO;
import cz.cvut.fel.cinetrack.exception.media.existingData.MovieAlreadyExistsException;
import cz.cvut.fel.cinetrack.exception.media.existingData.SeriesAlreadyExistsException;
import cz.cvut.fel.cinetrack.model.Series;
import cz.cvut.fel.cinetrack.model.User;
import cz.cvut.fel.cinetrack.security.SecurityUtils;
import cz.cvut.fel.cinetrack.service.MediaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/media")
public class MediaController {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @GetMapping("/overview")
    public ResponseEntity<?> getUserMediaOverview() {
        try {
            User user = SecurityUtils.getCurrentUser();
            List<MediaItemDTO> mediaItems = mediaService.getUserMedia(user.getId());
            return ResponseEntity.ok(mediaItems);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchMedia(
            @RequestBody SearchRequestDTO request
    ) {
        try {
            if ("series".equalsIgnoreCase(request.getType())) {
                SeriesSearchResponseDTO result = mediaService.searchSeriesWithSeason(request);
                return ResponseEntity.ok(result);
            } else {
                OMDBResponseDTO result = mediaService.searchMovie(request);
                return ResponseEntity.ok(result);
            }
        } catch (InvalidMediaTypeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Search failed: " + e.getMessage()));
        }
    }

    @PostMapping("/movie")
    public ResponseEntity<?> createMovie(
            @RequestBody MovieCreateRequestDTO request
    ) {
        try {
            User user = SecurityUtils.getCurrentUser();
            MovieResponseDTO movie = mediaService.createMovie(request, user.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(movie);
        } catch (MovieAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create movie: " + e.getMessage()));
        }
    }

    @PostMapping("/series")
    public ResponseEntity<?> createSeries(
            @RequestBody SeriesCreateRequestDTO request
    ) {
        try {
            User user = SecurityUtils.getCurrentUser();
            Series series = mediaService.createSeries(request, user.getId());
            SeriesResponseDTO response = new SeriesResponseDTO(series);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (SeriesAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create series: " + e.getMessage()));
        }
    }


}
