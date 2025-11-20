/*
 * Created by minmin_tranova on 11.11.2025
 */

package cz.cvut.fel.cinetrack.dto.media.response.omdb;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class OMDBSeasonResponseDTO {

    @JsonProperty("Title")
    private String title;
    @JsonProperty("Season")
    private String season;
    @JsonProperty("totalSeasons")
    private String totalSeasons;
    @JsonProperty("Episodes")
    private List<OMDBEpisodeResponseDTO> episodes;
    @JsonProperty("Response")
    private String response;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getTotalSeasons() {
        return totalSeasons;
    }

    public void setTotalSeasons(String totalSeasons) {
        this.totalSeasons = totalSeasons;
    }

    public List<OMDBEpisodeResponseDTO> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<OMDBEpisodeResponseDTO> episodes) {
        this.episodes = episodes;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
