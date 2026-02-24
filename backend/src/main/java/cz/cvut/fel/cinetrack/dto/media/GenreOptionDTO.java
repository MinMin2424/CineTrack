/*
 * Created by minmin_tranova on 24.02.2026
 */

package cz.cvut.fel.cinetrack.dto.media;

import cz.cvut.fel.cinetrack.model.Genre;

public class GenreOptionDTO {
    private Long id;
    private String type;

    public GenreOptionDTO(Genre genre) {
        this.id = genre.getId();
        this.type = genre.getType();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
