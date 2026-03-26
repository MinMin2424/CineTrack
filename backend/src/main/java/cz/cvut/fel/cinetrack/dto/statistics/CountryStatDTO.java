/*
 * Created by minmin_tranova on 24.03.2026
 */

package cz.cvut.fel.cinetrack.dto.statistics;

public class CountryStatDTO {

    private String country;
    private int movies;
    private int series;
    private int total;

    public CountryStatDTO(String country, int movies, int series) {
        this.country = country;
        this.movies = movies;
        this.series = series;
        this.total = movies + series;
    }

    public String getCountry() {
        return country;
    }

    public int getMovies() {
        return movies;
    }

    public int getSeries() {
        return series;
    }

    public int getTotal() {
        return total;
    }
}
