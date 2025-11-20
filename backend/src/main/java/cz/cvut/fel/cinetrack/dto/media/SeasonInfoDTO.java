/*
 * Created by minmin_tranova on 10.11.2025
 */

package cz.cvut.fel.cinetrack.dto.media;

import java.util.List;

public class SeasonInfoDTO {

    private String title;
    private int season;
    private int episodes;
    private List<EpisodeInfoDTO> episodeList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public int getEpisodes() {
        return episodes;
    }

    public void setEpisodes(int episodes) {
        this.episodes = episodes;
    }

    public List<EpisodeInfoDTO> getEpisodeList() {
        return episodeList;
    }

    public void setEpisodeList(List<EpisodeInfoDTO> episodeList) {
        this.episodeList = episodeList;
    }
}
