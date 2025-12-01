/*
 * Created by minmin_tranova on 20.11.2025
 */

package cz.cvut.fel.cinetrack.service;

import cz.cvut.fel.cinetrack.dto.media.request.ChangeStatusRequestDTO;
import cz.cvut.fel.cinetrack.dto.media.request.EditMediaRequestDTO;
import cz.cvut.fel.cinetrack.dto.media.response.SeriesResponseDTO;
import cz.cvut.fel.cinetrack.exception.media.notFoundObj.SeriesNotFoundException;
import cz.cvut.fel.cinetrack.model.Series;
import cz.cvut.fel.cinetrack.repository.SeriesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;

import static cz.cvut.fel.cinetrack.util.MediaUtils.parseStatus;
import static cz.cvut.fel.cinetrack.util.MediaUtils.parseStringToFloat;
import static cz.cvut.fel.cinetrack.validator.MediaValidator.*;

@Service
@Transactional
public class SeriesService {

    private final SeriesRepository seriesRepository;

    public SeriesService(SeriesRepository seriesRepository) {
        this.seriesRepository = seriesRepository;
    }

    public SeriesResponseDTO getSeriesById(Long seriesId, Long userId) {
        Series series = checkSeriesExistence(seriesId, userId);
        return new SeriesResponseDTO(series);
    }

    public SeriesResponseDTO changeSeriesStatus(Long seriesId, Long userId, ChangeStatusRequestDTO request) {
        Series series = checkSeriesExistence(seriesId, userId);
        series.setStatus(parseStatus(request.getStatus()));
        Series savedSeries = seriesRepository.save(series);
        return new SeriesResponseDTO(savedSeries);
    }

    public SeriesResponseDTO editSeries(Long seriesId, Long userId, EditMediaRequestDTO request) {
        Series series = checkSeriesExistence(seriesId, userId);
        validateDates(request.getWatchStartDate(), request.getWatchEndDate());
        validateRating(parseStringToFloat(request.getRating()));

        series.setRating(parseStringToFloat(request.getRating()));
        series.setNotes(request.getNotes());
        series.setWatchStartDate(request.getWatchStartDate());
        series.setWatchEndDate(request.getWatchEndDate());

        Series savedSeries = seriesRepository.save(series);
        return new SeriesResponseDTO(savedSeries);
    }

    public void deleteSeries(Long seriesId, Long userId) {
        Series series = checkSeriesExistence(seriesId, userId);
        series.setDeleted(true);
        series.setDeletedAt(LocalDateTime.now());
        seriesRepository.save(series);
    }

    private Series checkSeriesExistence(Long seriesId, Long userId) {
        return seriesRepository.findByIdAndUserIdAndNotDeleted(seriesId, userId)
                .orElseThrow(() -> {

                    boolean seriesExists = seriesRepository.existsById(seriesId);
                    if (!seriesExists) {
                        return new SeriesNotFoundException(String.format("Series with id %s does not exist!", seriesId));
                    }

                    boolean hasAccess = seriesRepository.existsByIdAndUserId(seriesId, userId);
                    if (!hasAccess) {
                        throw new AccessDeniedException(String.format("You do not have access to series with id %s!", seriesId));
                    }

                    throw new SeriesNotFoundException(String.format("Series with id %s was deleted!", seriesId));
                });
    }
}
