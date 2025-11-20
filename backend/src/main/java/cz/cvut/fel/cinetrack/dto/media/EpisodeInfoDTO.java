/*
 * Created by minmin_tranova on 11.11.2025
 */

package cz.cvut.fel.cinetrack.dto.media;

import cz.cvut.fel.cinetrack.model.enums.EpisodeStatusEnum;
import cz.cvut.fel.cinetrack.model.enums.StatusEnum;

import java.time.LocalDate;

public class EpisodeInfoDTO {

    private String title;
    private int episode;
    private String releaseDate;

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

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
