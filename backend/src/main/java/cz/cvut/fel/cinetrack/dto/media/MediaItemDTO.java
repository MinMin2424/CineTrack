/*
 * Created by minmin_tranova on 18.11.2025
 */

package cz.cvut.fel.cinetrack.dto.media;

import cz.cvut.fel.cinetrack.model.Country;
import cz.cvut.fel.cinetrack.model.Genre;
import cz.cvut.fel.cinetrack.model.Language;
import cz.cvut.fel.cinetrack.model.Movie;
import cz.cvut.fel.cinetrack.model.Series;
import cz.cvut.fel.cinetrack.model.enums.MediaType;
import cz.cvut.fel.cinetrack.model.enums.StatusEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MediaItemDTO {

    private Long id;
    private MediaType type;
    private String title;
    private int releaseYear;
    private String poster;
    private StatusEnum status;
    private LocalDate watchStartDate;
    private LocalDate watchEndDate;
    private float rating;
    private String notes;
    private LocalDateTime createdAt;
    private List<String> languages;
    private List<String> countries;
    private List<String> genres;

    // for movies
    private String runtime;

    // for series
    private int season;
    private int episodes;

    public MediaItemDTO(Movie movie) {
        this.id = movie.getId();
        this.type = MediaType.MOVIE;
        this.title = movie.getTitle();
        this.releaseYear = movie.getReleaseYear();
        this.poster = movie.getPoster();
        this.status = movie.getStatus();
        this.watchStartDate = movie.getWatchStartDate();
        this.watchEndDate = movie.getWatchEndDate();
        this.rating = movie.getRating();
        this.notes = movie.getNotes();
        this.createdAt = movie.getCreatedAt();
        this.languages = movie.getLanguages().stream()
                .map(Language::getLang)
                .toList();
        this.countries = movie.getCountries().stream()
                .map(Country::getCountryName)
                .toList();
        this.genres = movie.getGenres().stream()
                .map(Genre::getType)
                .toList();
        this.runtime = movie.getRuntime();
    }

    public MediaItemDTO(Series series) {
        this.id = series.getId();
        this.type = MediaType.SERIES;
        this.title = series.getTitle();
        this.releaseYear = series.getReleaseYear();
        this.poster = series.getPoster();
        this.status = series.getStatus();
        this.watchStartDate = series.getWatchStartDate();
        this.watchEndDate = series.getWatchEndDate();
        this.rating = series.getRating();
        this.notes = series.getNotes();
        this.createdAt = series.getCreatedAt();
        this.languages = series.getLanguages().stream()
                .map(Language::getLang)
                .toList();
        this.countries = series.getCountries().stream()
                .map(Country::getCountryName)
                .toList();
        this.genres = series.getGenres().stream()
                .map(Genre::getType)
                .toList();
        this.season = series.getSeason();
        this.episodes = series.getEpisodes();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MediaType getType() {
        return type;
    }

    public void setType(MediaType type) {
        this.type = type;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
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
}
