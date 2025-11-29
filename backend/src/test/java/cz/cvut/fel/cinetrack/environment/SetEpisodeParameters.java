/*
 * Created by minmin_tranova on 29.11.2025
 */

package cz.cvut.fel.cinetrack.environment;

import cz.cvut.fel.cinetrack.model.Episode;
import cz.cvut.fel.cinetrack.model.Series;
import cz.cvut.fel.cinetrack.model.enums.EpisodeStatusEnum;

public class SetEpisodeParameters {

    public static void setEpisodeParameters(Episode episode, int episodeNumber, Series series) {
        episode.setTitle("Test Episode");
        episode.setSeries(series);
        episode.setEpisode(episodeNumber);
        episode.setStatus(EpisodeStatusEnum.WATCHING);
        episode.setRating(0);
        episode.setNotes(null);
    }
}
