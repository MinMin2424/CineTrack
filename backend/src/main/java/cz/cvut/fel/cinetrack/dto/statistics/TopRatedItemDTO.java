/*
 * Created by minmin_tranova on 26.03.2026
 */

package cz.cvut.fel.cinetrack.dto.statistics;

public class TopRatedItemDTO {

    private String title;
    private Float rating;

    public TopRatedItemDTO(String title, Float rating) {
        this.title = title;
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public Float getRating() {
        return rating;
    }
}
