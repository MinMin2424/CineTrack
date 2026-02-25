/*
 * Created by minmin_tranova on 06.12.2025
 */

package cz.cvut.fel.cinetrack.dto.media;

import cz.cvut.fel.cinetrack.model.Genre;
import cz.cvut.fel.cinetrack.model.enums.MediaType;
import cz.cvut.fel.cinetrack.model.enums.StatusEnum;

import java.util.List;

public class FilterOptionsDTO {

    private List<MediaType> types;
    private List<StatusEnum> statuses;
    private List<GenreOptionDTO> genres;
    private List<Integer> releaseYears;
    private List<CountryOptionDTO> countries;

    public List<MediaType> getTypes() {
        return types;
    }

    public void setTypes(List<MediaType> types) {
        this.types = types;
    }

    public List<StatusEnum> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<StatusEnum> statuses) {
        this.statuses = statuses;
    }

    public List<GenreOptionDTO> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreOptionDTO> genres) {
        this.genres = genres;
    }

    public List<Integer> getReleaseYears() {
        return releaseYears;
    }

    public void setReleaseYears(List<Integer> releaseYears) {
        this.releaseYears = releaseYears;
    }

    public List<CountryOptionDTO> getCountries() {
        return countries;
    }

    public void setCountries(List<CountryOptionDTO> countries) {
        this.countries = countries;
    }
}
