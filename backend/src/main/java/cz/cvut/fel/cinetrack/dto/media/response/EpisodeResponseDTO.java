/*
 * Created by minmin_tranova on 20.11.2025
 */

package cz.cvut.fel.cinetrack.dto.media.response;

import cz.cvut.fel.cinetrack.model.Episode;
import cz.cvut.fel.cinetrack.model.enums.EpisodeStatusEnum;

public class EpisodeResponseDTO {

    private String title;
    private int episode;
    private EpisodeStatusEnum status;
    private float rating;
    private String notes;

    public EpisodeResponseDTO(Episode episode) {
        this.title = episode.getTitle();
        this.episode = episode.getEpisode();
        this.status = episode.getStatus();
        this.rating = episode.getRating();
        this.notes = episode.getNotes();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getEpisode() {
        return episode;
    }

    public void setEpisode(int episode) {
        this.episode = episode;
    }

    public EpisodeStatusEnum getStatus() {
        return status;
    }

    public void setStatus(EpisodeStatusEnum status) {
        this.status = status;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
