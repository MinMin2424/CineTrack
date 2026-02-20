/*
 * Created by minmin_tranova on 20.11.2025
 */

package cz.cvut.fel.cinetrack.controller;

import cz.cvut.fel.cinetrack.dto.media.request.ChangeStatusRequestDTO;
import cz.cvut.fel.cinetrack.dto.media.request.EditMediaRequestDTO;
import cz.cvut.fel.cinetrack.dto.media.response.MovieResponseDTO;
import cz.cvut.fel.cinetrack.exception.media.notFoundObj.MovieNotFoundException;
import cz.cvut.fel.cinetrack.model.User;
import cz.cvut.fel.cinetrack.security.SecurityUtils;
import cz.cvut.fel.cinetrack.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/movies")
@CrossOrigin(origins = "http://localhost:3000")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<?> getMovie(@PathVariable("movieId") Long movieId) {
        try {
            User user = SecurityUtils.getCurrentUser();
            MovieResponseDTO movie = movieService.getMovieById(movieId, user.getId());
            return ResponseEntity.ok(movie);
        } catch (MovieNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("{movieId}/status")
    public ResponseEntity<?> changeMovieStatus(
            @PathVariable("movieId") Long movieId,
            @RequestBody ChangeStatusRequestDTO request
    ) {
        try {
            User user = SecurityUtils.getCurrentUser();
            MovieResponseDTO updatedMovie = movieService.changeMovieStatus(movieId, user.getId(), request);
            return ResponseEntity.ok(updatedMovie);
        } catch (MovieNotFoundException e) {
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

    @PutMapping("/{movieId}")
    public ResponseEntity<?> editMovie(
            @PathVariable("movieId") Long movieId,
            @RequestBody EditMediaRequestDTO request
    ) {
        try {
            User user = SecurityUtils.getCurrentUser();
            MovieResponseDTO updatedMovie = movieService.editMovie(movieId, user.getId(), request);
            return ResponseEntity.ok(updatedMovie);
        } catch (MovieNotFoundException e) {
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

    @DeleteMapping("/{movieId}")
    public ResponseEntity<?> deleteMovie(@PathVariable("movieId") Long movieId) {
        try {
            User user = SecurityUtils.getCurrentUser();
            movieService.deleteMovie(movieId, user.getId());
            return ResponseEntity.ok().build();
        } catch (MovieNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
