/*
 * Created by minmin_tranova on 24.03.2026
 */

package cz.cvut.fel.cinetrack.dto.statistics;

public class SummaryDTO {

    private int totalEntries;
    private int movies;
    private int series;

    public SummaryDTO(int totalEntries, int movies, int series) {
        this.totalEntries = totalEntries;
        this.movies = movies;
        this.series = series;
    }

    public int getTotalEntries() {
        return totalEntries;
    }

    public int getMovies() {
        return movies;
    }

    public int getSeries() {
        return series;
    }
}
