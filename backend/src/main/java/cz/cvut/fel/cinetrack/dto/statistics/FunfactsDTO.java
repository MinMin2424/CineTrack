/*
 * Created by minmin_tranova on 24.03.2026
 */

package cz.cvut.fel.cinetrack.dto.statistics;

public class FunfactsDTO {

    private Integer longestSeriesEpisodes;
    private String longestSeriesTitle;
    private Integer longestMovieRuntime;
    private String longestMovieTitle;
    private Integer uniqueCountriesCount;

    public FunfactsDTO(
            Integer longestSeriesEpisodes,
            String longestSeriesTitle,
            Integer longestMovieRuntime,
            String longestMovieTitle,
            Integer uniqueCountriesCount
    ) {
        this.longestSeriesEpisodes = longestSeriesEpisodes;
        this.longestSeriesTitle = longestSeriesTitle;
        this.longestMovieRuntime = longestMovieRuntime;
        this.longestMovieTitle = longestMovieTitle;
        this.uniqueCountriesCount = uniqueCountriesCount;
    }

    public Integer getLongestSeriesEpisodes() {
        return longestSeriesEpisodes;
    }

    public String getLongestSeriesTitle() {
        return longestSeriesTitle;
    }

    public Integer getLongestMovieRuntime() {
        return longestMovieRuntime;
    }

    public String getLongestMovieTitle() {
        return longestMovieTitle;
    }

    public Integer getUniqueCountriesCount() {
        return uniqueCountriesCount;
    }
}
