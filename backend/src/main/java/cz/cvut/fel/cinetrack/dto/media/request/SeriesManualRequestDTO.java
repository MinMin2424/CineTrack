/*
 * Created by minmin_tranova on 01.12.2025
 */

package cz.cvut.fel.cinetrack.dto.media.request;

import cz.cvut.fel.cinetrack.model.enums.StatusEnum;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public class SeriesManualRequestDTO {

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Release year is required")
    @Min(value = 1888, message = "Release year must be at least 1888")
    @Max(value = 2100, message = "Release year must be reasonable")
    private int releaseYear;

    @NotNull(message = "Runtime is required")
    @Min(value = 1, message = "Season must be at least 1")
    private int season;

    @NotBlank(message = "Poster URL is required")
    private String posterUrl;

    private LocalDate watchStartDate;
    private LocalDate watchEndDate;

    @NotNull(message = "Status is required")
    private String status;

    @DecimalMin(value = "0.0", message = "Rating must be at least 0")
    @DecimalMax(value = "10.0", message = "Rating must be at most 10")
    private float rating;

    private String notes;

    @NotEmpty(message = "At least one genre is required")
    private List<String> genres;

    @NotEmpty(message = "At least one language is required")
    private List<String> languages;

    @NotEmpty(message = "At least one country is required")
    private List<String> countries;

    public @NotBlank(message = "Title is required") String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank(message = "Title is required") String title) {
        this.title = title;
    }

    @NotNull(message = "Release year is required")
    @Min(value = 1888, message = "Release year must be at least 1888")
    @Max(value = 2100, message = "Release year must be reasonable")
    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(@NotNull(message = "Release year is required") @Min(value = 1888, message = "Release year must be at least 1888") @Max(value = 2100, message = "Release year must be reasonable") int releaseYear) {
        this.releaseYear = releaseYear;
    }

    @NotNull(message = "Runtime is required")
    @Min(value = 1, message = "Season must be at least 1")
    public int getSeason() {
        return season;
    }

    public void setSeason(@NotNull(message = "Runtime is required") @Min(value = 1, message = "Season must be at least 1") int season) {
        this.season = season;
    }

    public @NotBlank(message = "Poster URL is required") String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(@NotBlank(message = "Poster URL is required") String posterUrl) {
        this.posterUrl = posterUrl;
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

    public @NotNull(message = "Status is required") String getStatus() {
        return status;
    }

    public void setStatus(@NotNull(message = "Status is required") String status) {
        this.status = status;
    }

    @DecimalMin(value = "0.0", message = "Rating must be at least 0")
    @DecimalMax(value = "10.0", message = "Rating must be at most 10")
    public float getRating() {
        return rating;
    }

    public void setRating(@DecimalMin(value = "0.0", message = "Rating must be at least 0") @DecimalMax(value = "10.0", message = "Rating must be at most 10") float rating) {
        this.rating = rating;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public @NotEmpty(message = "At least one genre is required") List<String> getGenres() {
        return genres;
    }

    public void setGenres(@NotEmpty(message = "At least one genre is required") List<String> genres) {
        this.genres = genres;
    }

    public @NotEmpty(message = "At least one language is required") List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(@NotEmpty(message = "At least one language is required") List<String> languages) {
        this.languages = languages;
    }

    public @NotEmpty(message = "At least one country is required") List<String> getCountries() {
        return countries;
    }

    public void setCountries(@NotEmpty(message = "At least one country is required") List<String> countries) {
        this.countries = countries;
    }
}
