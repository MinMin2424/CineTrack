/*
 * Created by minmin_tranova on 18.11.2025
 */

package cz.cvut.fel.cinetrack.dto.media.response;

import cz.cvut.fel.cinetrack.model.Country;
import cz.cvut.fel.cinetrack.model.Genre;
import cz.cvut.fel.cinetrack.model.Language;
import cz.cvut.fel.cinetrack.model.Movie;
import cz.cvut.fel.cinetrack.model.enums.StatusEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MovieResponseDTO {

    private Long id;
    private String imdbID;
    private String title;
    private int releaseYear;
    private String runtime;
    private List<String> genres;
    private List<String> languages;
    private List<String> countries;
    private String poster;

    private StatusEnum status;
    private float rating;
    private String notes;
    private LocalDate watchStartDate;
    private LocalDate watchEndDate;
    private LocalDateTime createdAt;

    public MovieResponseDTO(Movie movie) {
        this.id = movie.getId();
        this.imdbID = movie.getImdbId();
        this.title = movie.getTitle();
        this.releaseYear = movie.getReleaseYear();
        this.runtime = movie.getRuntime();
        this.genres = movie.getGenres().stream()
                .map(Genre::getType)
                .toList();
        this.languages = movie.getLanguages().stream()
                .map(Language::getLang)
                .toList();
        this.countries = movie.getCountries().stream()
                .map(Country::getCountryName)
                .toList();
        this.poster = movie.getPoster();
        this.status = movie.getStatus();
        this.rating = movie.getRating();
        this.notes = movie.getNotes();
        this.watchStartDate = movie.getWatchStartDate();
        this.watchEndDate = movie.getWatchEndDate();
        this.createdAt = movie.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
