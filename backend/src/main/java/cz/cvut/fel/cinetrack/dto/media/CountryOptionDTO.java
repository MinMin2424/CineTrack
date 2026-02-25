/*
 * Created by minmin_tranova on 24.02.2026
 */

package cz.cvut.fel.cinetrack.dto.media;

import cz.cvut.fel.cinetrack.model.Country;

public class CountryOptionDTO {
    private Long id;
    private String countryName;

    public CountryOptionDTO(Country country) {
        this.id = country.getId();
        this.countryName = country.getCountryName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
