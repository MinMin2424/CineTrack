/*
 * Created by minmin_tranova on 10.11.2025
 */

package cz.cvut.fel.cinetrack.repository;

import cz.cvut.fel.cinetrack.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SeriesRepository extends JpaRepository<Series, Long> {

    boolean existsByImdbIdAndSeasonAndUserId(String imdbId, int season, Long userId);
    boolean existsById(Long id);
    boolean existsByIdAndUserId(Long id, Long userId);
    List<Series> findByUserId(Long userId);
    Optional<Series> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT s FROM Series s WHERE s.id = :id AND s.user.id = :userId AND s.deleted = false")
    Optional<Series> findByIdAndUserIdAndNotDeleted(Long id, Long userId);

    @Query("SELECT s FROM Series s WHERE s.user.id = :userId AND s.deleted = false ORDER BY s.createdAt DESC")
    List<Series> findNotDeletedSeriesByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);
}
