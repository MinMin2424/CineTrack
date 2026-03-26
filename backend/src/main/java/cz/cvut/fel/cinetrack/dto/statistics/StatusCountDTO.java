/*
 * Created by minmin_tranova on 24.03.2026
 */

package cz.cvut.fel.cinetrack.dto.statistics;

public class StatusCountDTO {

    private int total;
    private int movies;
    private int series;

    public StatusCountDTO(int total, int movies, int series) {
        this.total = total;
        this.movies = movies;
        this.series = series;
    }

    public int getTotal() {
        return total;
    }

    public int getMovies() {
        return movies;
    }

    public int getSeries() {
        return series;
    }
}
