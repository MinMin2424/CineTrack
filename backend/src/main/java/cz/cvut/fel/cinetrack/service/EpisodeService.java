/*
 * Created by minmin_tranova on 20.11.2025
 */

package cz.cvut.fel.cinetrack.service;

import cz.cvut.fel.cinetrack.dto.media.request.ChangeEpisodeStatusRequestDTO;
import cz.cvut.fel.cinetrack.dto.media.request.EditEpisodeRequestDTO;
import cz.cvut.fel.cinetrack.dto.media.response.EpisodeResponseDTO;
import cz.cvut.fel.cinetrack.exception.media.notFoundObj.EpisodeNotFoundException;
import cz.cvut.fel.cinetrack.exception.media.notFoundObj.SeriesNotFoundException;
import cz.cvut.fel.cinetrack.model.Episode;
import cz.cvut.fel.cinetrack.model.Series;
import cz.cvut.fel.cinetrack.repository.EpisodeRepository;
import cz.cvut.fel.cinetrack.repository.SeriesRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static cz.cvut.fel.cinetrack.util.MediaUtils.parseEpisodeStatus;
import static cz.cvut.fel.cinetrack.util.MediaUtils.parseStringToFloat;
import static cz.cvut.fel.cinetrack.validator.MediaValidator.validateRating;

@Service
@Transactional
public class EpisodeService {

    private final EpisodeRepository episodeRepository;
    private final SeriesRepository seriesRepository;

    public EpisodeService(EpisodeRepository episodeRepository, SeriesRepository seriesRepository) {
        this.episodeRepository = episodeRepository;
        this.seriesRepository = seriesRepository;
    }

    public EpisodeResponseDTO getEpisodeByNumber(Long seriesId, int episodeNumber, Long userId) {
        Episode episode = checkEpisodeExistence(seriesId, episodeNumber, userId);
        return new EpisodeResponseDTO(episode);
    }

    public EpisodeResponseDTO changeEpisodeStatus(Long seriesId, int episodeNumber, Long userId, ChangeEpisodeStatusRequestDTO request) {
        Episode episode = checkEpisodeExistence(seriesId, episodeNumber, userId);
        episode.setStatus(parseEpisodeStatus(request.getStatus()));
        Episode savedEpisode = episodeRepository.save(episode);
        return new EpisodeResponseDTO(savedEpisode);
    }

    public EpisodeResponseDTO editEpisode(Long seriesId, int episodeNumber, Long userId, EditEpisodeRequestDTO request) {
        Episode episode = checkEpisodeExistence(seriesId, episodeNumber, userId);
        validateRating(parseStringToFloat(request.getRating()));
        episode.setRating(parseStringToFloat(request.getRating()));
        episode.setNotes(request.getNotes());
        Episode savedEpisode = episodeRepository.save(episode);
        return new EpisodeResponseDTO(savedEpisode);
    }

    public List<EpisodeResponseDTO> getEpisodesBySeriesId(Long seriesId, Long userId) {
        if (!seriesRepository.existsById(seriesId)) {
            throw new SeriesNotFoundException("Series not found");
        }
        Series series = seriesRepository.findByIdAndUserIdAndNotDeleted(seriesId, userId)
                .orElseThrow(() -> new AccessDeniedException("You do not have access to this series!"));
        List<Episode> episodes = episodeRepository.findBySeriesIdOrderByEpisode(seriesId);
        return episodes.stream()
                .map(EpisodeResponseDTO::new)
                .toList();
    }

    private Episode checkEpisodeExistence(Long seriesId, int episodeNumber, Long userId) {
        if (!seriesRepository.existsById(seriesId)) {
            throw new SeriesNotFoundException("Series not found!");
        }
        Series series = seriesRepository.findByIdAndUserIdAndNotDeleted(seriesId, userId)
                .orElseThrow(() -> new AccessDeniedException("You do not have access to series " + seriesId + "!"));
        return episodeRepository.findBySeriesIdAndEpisode(seriesId, episodeNumber)
                .orElseThrow(() -> new EpisodeNotFoundException(String.format("Episode %d not found in series!", episodeNumber)));
    }
}
