/*
 * Created by minmin_tranova on 24.03.2026
 */

package cz.cvut.fel.cinetrack.dto.statistics;

public class MonthlyActiveDTO {

    private int year;
    private int month;
    private int total;
    private int movies;
    private int series;
    private Integer previousMonthTotal;

    public MonthlyActiveDTO(
            int year,
            int month,
            int total,
            int movies,
            int series,
            Integer previousMonthTotal
    ) {
        this.year = year;
        this.month = month;
        this.total = total;
        this.movies = movies;
        this.series = series;
        this.previousMonthTotal = previousMonthTotal;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
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

    public Integer getPreviousMonthTotal() {
        return previousMonthTotal;
    }

    public int getDiffFromPreviousMonth() {
        if (previousMonthTotal == null) return 0;
        return total - previousMonthTotal;
    }
}
