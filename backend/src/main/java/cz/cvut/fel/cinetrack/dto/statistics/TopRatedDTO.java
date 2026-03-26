/*
 * Created by minmin_tranova on 26.03.2026
 */

package cz.cvut.fel.cinetrack.dto.statistics;

public class TopRatedDTO {

    private TopRatedItemDTO movie;
    private TopRatedItemDTO series;

    public TopRatedDTO(TopRatedItemDTO movie, TopRatedItemDTO series) {
        this.movie = movie;
        this.series = series;
    }

    public TopRatedItemDTO getMovie() {
        return movie;
    }

    public TopRatedItemDTO getSeries() {
        return series;
    }
}
