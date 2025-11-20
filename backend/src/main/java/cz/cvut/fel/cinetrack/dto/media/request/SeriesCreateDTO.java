/*
 * Created by minmin_tranova on 10.11.2025
 */

package cz.cvut.fel.cinetrack.dto.media.request;

import java.time.LocalDate;

public class SeriesCreateDTO {

    private String imdbID;
    private String title;
    private String genre;
    private String language;
    private String country;
    private String poster;
    private String season;

    private String status;
    private String rating;
    private String notes;
    private LocalDate watchStartDate;
    private LocalDate watchEndDate;

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDate getWatchStartDate() {
        return watchStartDate;
    }

    public void setWatchStartDate(LocalDate watchStartDate) {
        this.watchStartDate = watchStartDate;
    }

    public LocalDate getWatchEndDate() {
        return watchEndDate;
    }

    public void setWatchEndDate(LocalDate watchEndDate) {
        this.watchEndDate = watchEndDate;
    }
}
