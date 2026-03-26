/*
 * Created by minmin_tranova on 24.03.2026
 */

package cz.cvut.fel.cinetrack.dto.statistics;

public class StatusOverviewDTO {

    private StatusCountDTO completed;
    private StatusCountDTO watching;
    private StatusCountDTO planToWatch;
    private StatusCountDTO dropped;

    public StatusOverviewDTO(
            StatusCountDTO completed,
            StatusCountDTO watching,
            StatusCountDTO planToWatch,
            StatusCountDTO dropped
    ) {
        this.completed = completed;
        this.watching = watching;
        this.planToWatch = planToWatch;
        this.dropped = dropped;
    }

    public StatusCountDTO getCompleted() {
        return completed;
    }

    public StatusCountDTO getWatching() {
        return watching;
    }

    public StatusCountDTO getPlanToWatch() {
        return planToWatch;
    }

    public StatusCountDTO getDropped() {
        return dropped;
    }
}
