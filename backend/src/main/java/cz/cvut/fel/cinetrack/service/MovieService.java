/*
 * Created by minmin_tranova on 20.11.2025
 */

package cz.cvut.fel.cinetrack.service;

import cz.cvut.fel.cinetrack.dto.media.request.ChangeStatusRequestDTO;
import cz.cvut.fel.cinetrack.dto.media.request.EditMediaRequestDTO;
import cz.cvut.fel.cinetrack.dto.media.response.MovieResponseDTO;
import cz.cvut.fel.cinetrack.exception.media.notFoundObj.MovieNotFoundException;
import cz.cvut.fel.cinetrack.model.Movie;
import cz.cvut.fel.cinetrack.repository.MovieRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static cz.cvut.fel.cinetrack.util.MediaUtils.parseStatus;
import static cz.cvut.fel.cinetrack.util.MediaUtils.parseStringToFloat;
import static cz.cvut.fel.cinetrack.validator.MediaValidator.validateDates;
import static cz.cvut.fel.cinetrack.validator.MediaValidator.validateRating;

@Service
@Transactional
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public MovieResponseDTO getMovieById(Long movieId, Long userId) {
        Movie movie = checkMovieExistence(movieId, userId);
        return new MovieResponseDTO(movie);
    }

    public MovieResponseDTO changeMovieStatus(Long movieId, Long userId, ChangeStatusRequestDTO request) {
        Movie movie = checkMovieExistence(movieId, userId);
        movie.setStatus(parseStatus(request.getStatus()));
        Movie savedMovie = movieRepository.save(movie);
        return new MovieResponseDTO(savedMovie);
    }

    public MovieResponseDTO editMovie(Long movieId, Long userId, EditMediaRequestDTO request) {
        Movie movie = checkMovieExistence(movieId, userId);
        validateDates(request.getWatchStartDate(), request.getWatchEndDate());
        validateRating(parseStringToFloat(request.getRating()));

        movie.setRating(parseStringToFloat(request.getRating()));
        movie.setNotes(request.getNotes());
        movie.setWatchStartDate(request.getWatchStartDate());
        movie.setWatchEndDate(request.getWatchEndDate());

        Movie savedMovie = movieRepository.save(movie);
        return new MovieResponseDTO(savedMovie);
    }

    public void deleteMovie(Long movieId, Long userId) {
        Movie movie = checkMovieExistence(movieId, userId);
        movie.setDeleted(true);
        movie.setDeletedAt(LocalDateTime.now());
        movieRepository.save(movie);
    }

    private Movie checkMovieExistence(Long movieId, Long userId) {
        return movieRepository.findByIdAndUserIdAndNotDeleted(movieId, userId)
                .orElseThrow(() -> {

                    boolean movieExists = movieRepository.existsById(movieId);
                    if (!movieExists) {
                        return new MovieNotFoundException(String.format("Movie with id %s does not exist!", movieId));
                    }

                    boolean hasAccess = movieRepository.existsByIdAndUserId(movieId, userId);
                    if (!hasAccess) {
                        throw new AccessDeniedException(String.format("You do not have access to movie %s!", movieId));
                    }

                    return new MovieNotFoundException(String.format("Movie with id %s was deleted!", movieId));
                });
    }
}
