/*
 * Created by minmin_tranova on 18.03.2026
 */

package cz.cvut.fel.cinetrack.dto.media.response.omdb;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class OMDBSearchResultDTO {

    @JsonProperty("Search")
    private List<OMDBSearchItemDTO> search;
    @JsonProperty("totalResults")
    private String totalResults;
    @JsonProperty("Response")
    private String response;

    public List<OMDBSearchItemDTO> getSearch() {
        return search;
    }

    public void setSearch(List<OMDBSearchItemDTO> search) {
        this.search = search;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
