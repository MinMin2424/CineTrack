/*
 * Created by minmin_tranova on 10.11.2025
 */

package cz.cvut.fel.cinetrack.repository;

import cz.cvut.fel.cinetrack.model.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EpisodeRepository extends JpaRepository<Episode, Long> {

    List<Episode> findBySeriesId(Long seriesId);
    Optional<Episode> findByEpisodeId(Long episodeId);
    void deleteBySeriesId(Long seriesId);

    @Query("SELECT e FROM Episode e WHERE e.series.id = :seriesId AND e.episode = :episode AND e.series.deleted = false")
    Optional<Episode> findBySeriesIdAndEpisode(@Param("seriesId") Long seriesId, @Param("episode") int episode);

    @Query("SELECT e FROM Episode e WHERE e.series.id = :seriesId AND e.series.deleted = false ORDER BY e.episode")
    List<Episode> findBySeriesIdOrderByEpisode(@Param("seriesId") Long seriesId);

    @Query("SELECT e FROM Episode e WHERE e.id = :id AND e.series.user.id = :userId AND e.series.deleted = false")
    Optional<Episode> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);
}
