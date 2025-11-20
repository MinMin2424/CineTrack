/*
 * Created by minmin_tranova on 19.11.2025
 */

package cz.cvut.fel.cinetrack.dto.media.response;

import cz.cvut.fel.cinetrack.dto.media.SeasonInfoDTO;
import cz.cvut.fel.cinetrack.dto.media.response.omdb.OMDBResponseDTO;

import java.util.List;

public class SeriesSearchResponseDTO {

    private OMDBResponseDTO seriesInfo;
    private List<SeasonInfoDTO> availableSeasons;

    public SeriesSearchResponseDTO(
            OMDBResponseDTO seriesInfo,
            List<SeasonInfoDTO> availableSeasons
    ) {
        this.seriesInfo = seriesInfo;
        this.availableSeasons = availableSeasons;
    }

    public OMDBResponseDTO getSeriesInfo() {
        return seriesInfo;
    }

    public void setSeriesInfo(OMDBResponseDTO seriesInfo) {
        this.seriesInfo = seriesInfo;
    }

    public List<SeasonInfoDTO> getAvailableSeasons() {
        return availableSeasons;
    }

    public void setAvailableSeasons(List<SeasonInfoDTO> availableSeasons) {
        this.availableSeasons = availableSeasons;
    }
}
