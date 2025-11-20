/*
 * Created by minmin_tranova on 11.11.2025
 */

package cz.cvut.fel.cinetrack.dto.media.response.omdb;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OMDBEpisodeResponseDTO {

    @JsonProperty("Title")
    private String title;
    @JsonProperty("Released")
    private String released;
    @JsonProperty("Episode")
    private String episode;
    @JsonProperty("imdbRating")
    private String imdbRating;
    @JsonProperty("imdbID")
    private String imdbID;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }
}
