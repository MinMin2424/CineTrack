/*
 * Created by minmin_tranova on 18.11.2025
 */

package cz.cvut.fel.cinetrack.dto.media.response;

import cz.cvut.fel.cinetrack.model.Country;
import cz.cvut.fel.cinetrack.model.Genre;
import cz.cvut.fel.cinetrack.model.Language;
import cz.cvut.fel.cinetrack.model.Series;
import cz.cvut.fel.cinetrack.model.enums.StatusEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class SeriesResponseDTO {

    private Long id;
    private String imdbID;
    private String title;
    private int releaseYear;
    private List<String> genres;
    private List<String> languages;
    private List<String> countries;
    private String poster;
    private int season;
    private int episodes;
    private List<EpisodeResponseDTO> episodeList;

    private StatusEnum status;
    private float rating;
    private String notes;
    private LocalDate watchStartDate;
    private LocalDate watchEndDate;
    private LocalDateTime createdAt;

    public SeriesResponseDTO(Series series) {
        this.id = series.getId();
        this.imdbID = series.getImdbId();
        this.title = series.getTitle();
        this.releaseYear = series.getReleaseYear();
        this.genres = series.getGenres().stream()
                .map(Genre::getType)
                .toList();
        this.languages = series.getLanguages().stream()
                .map(Language::getLang)
                .toList();
        this.countries = series.getCountries().stream()
                .map(Country::getCountryName)
                .toList();
        this.poster = series.getPoster();
        this.season = series.getSeason();
        this.episodes = series.getEpisodes();
        this.episodeList = series.getEpisodeList().stream()
                .map(EpisodeResponseDTO::new)
                .toList();
        this.status = series.getStatus();
        this.rating = series.getRating();
        this.notes = series.getNotes();
        this.watchStartDate = series.getWatchStartDate();
        this.watchEndDate = series.getWatchEndDate();
        this.createdAt = series.getCreatedAt();
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

    public List<EpisodeResponseDTO> getEpisodeList() {
        return episodeList;
    }

    public void setEpisodeList(List<EpisodeResponseDTO> episodeList) {
        this.episodeList = episodeList;
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
