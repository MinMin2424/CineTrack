/*
 * Created by minmin_tranova on 10.11.2025
 */

package cz.cvut.fel.cinetrack.repository;

import cz.cvut.fel.cinetrack.model.Movie;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    boolean existsByImdbIdAndUserId(String imdbId, Long userId);
    boolean existsById(Long id);
    boolean existsByIdAndUserId(Long id, Long userId);
    boolean existsByTitleAndUserId(String title, Long userId);
    Optional<Movie> findById(Long id);
    Optional<Movie> findByImdbIdAndUserId(String imdbId, Long userId);
    Optional<Movie> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT m FROM Movie m WHERE m.id = :id AND m.user.id = :userId AND m.deleted = false")
    Optional<Movie> findByIdAndUserIdAndNotDeleted(Long id, Long userId);

    @Query("SELECT m FROM Movie m WHERE m.user.id = :userId AND m.deleted = false ORDER BY m.createdAt DESC")
    List<Movie> findNotDeletedMoviesByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);

    @EntityGraph(attributePaths = {"genres", "countries", "languages"})
    @Query("SELECT m FROM Movie m WHERE m.user.id = :userId AND m.deleted = false")
    List<Movie> findNotDeletedMoviesByUserId(Long userId);
}
