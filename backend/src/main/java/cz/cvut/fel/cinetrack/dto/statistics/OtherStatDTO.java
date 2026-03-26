/*
 * Created by minmin_tranova on 24.03.2026
 */

package cz.cvut.fel.cinetrack.dto.statistics;

public class OtherStatDTO {

    private int completedThisYear;
    private double averageRating;

    public OtherStatDTO(int completedThisYear, double averageRating) {
        this.completedThisYear = completedThisYear;
        this.averageRating = averageRating;
    }

    public int getCompletedThisYear() {
        return completedThisYear;
    }

    public double getAverageRating() {
        return averageRating;
    }
}
