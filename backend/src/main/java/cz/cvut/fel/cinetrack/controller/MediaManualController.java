/*
 * Created by minmin_tranova on 01.12.2025
 */

package cz.cvut.fel.cinetrack.controller;

import cz.cvut.fel.cinetrack.dto.media.request.MovieManualRequestDTO;
import cz.cvut.fel.cinetrack.dto.media.request.SeriesManualRequestDTO;
import cz.cvut.fel.cinetrack.dto.media.response.MovieResponseDTO;
import cz.cvut.fel.cinetrack.dto.media.response.SeriesResponseDTO;
import cz.cvut.fel.cinetrack.model.User;
import cz.cvut.fel.cinetrack.security.SecurityUtils;
import cz.cvut.fel.cinetrack.service.MediaManualService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/media/manual")
@CrossOrigin(origins = "http://localhost:3000")
public class MediaManualController {

    private final MediaManualService mediaManualService;

    public MediaManualController(MediaManualService mediaManualService) {
        this.mediaManualService = mediaManualService;
    }

    @PostMapping("/movie")
    public ResponseEntity<MovieResponseDTO> addMovieManually(
            @Valid @RequestBody MovieManualRequestDTO request
    ) {
        User user = SecurityUtils.getCurrentUser();
        MovieResponseDTO response = mediaManualService.addMovieManually(request, user.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/series")
    public ResponseEntity<SeriesResponseDTO> addSeriesManually(
            @Valid @RequestBody SeriesManualRequestDTO request
    ) {
        User user = SecurityUtils.getCurrentUser();
        SeriesResponseDTO response = mediaManualService.addSeriesManually(request, user.getId());
        return ResponseEntity.ok(response);
    }
}
