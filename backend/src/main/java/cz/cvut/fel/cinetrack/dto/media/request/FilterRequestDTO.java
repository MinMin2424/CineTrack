/*
 * Created by minmin_tranova on 06.12.2025
 */

package cz.cvut.fel.cinetrack.dto.media.request;

import cz.cvut.fel.cinetrack.model.enums.MediaType;
import cz.cvut.fel.cinetrack.model.enums.StatusEnum;

import java.util.List;

public class FilterRequestDTO {

    private List<MediaType> types;
    private List<StatusEnum> statuses;
    private List<Long> genreIds;
    private List<Integer> releaseYears;
    private List<Long> countryIds;

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

    public List<Long> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Long> genreIds) {
        this.genreIds = genreIds;
    }

    public List<Integer> getReleaseYears() {
        return releaseYears;
    }

    public void setReleaseYears(List<Integer> releaseYears) {
        this.releaseYears = releaseYears;
    }

    public List<Long> getCountryIds() {
        return countryIds;
    }

    public void setCountryIds(List<Long> countryIds) {
        this.countryIds = countryIds;
    }
}
