/*
 * Created by minmin_tranova on 01.12.2025
 */

package cz.cvut.fel.cinetrack.model.enums;

import org.springframework.data.domain.Sort;

public enum SortBy {
    TITLE_ASC("title", Sort.Direction.ASC),
    TITLE_DESC("title", Sort.Direction.DESC),
    RELEASE_YEAR_ASC("releaseYear", Sort.Direction.ASC),
    RELEASE_YEAR_DESC("releaseYear", Sort.Direction.DESC),
    CREATED_AT_ASC("createdAt", Sort.Direction.ASC),
    CREATED_AT_DESC("createdAt", Sort.Direction.DESC);

    private final String field;
    private final Sort.Direction direction;

    SortBy(String field, Sort.Direction direction) {
        this.field = field;
        this.direction = direction;
    }

    public String getField() {
        return field;
    }

    public Sort.Direction getDirection() {
        return direction;
    }

    public Sort toSort() {
        return Sort.by(direction, field);
    }

    public static SortBy fromString(String sortBy) {
        if (sortBy == null || sortBy.isEmpty()) {
            return CREATED_AT_DESC;
        }
        try {
            return SortBy.valueOf(sortBy.toUpperCase());
        } catch (IllegalArgumentException e) {
            return CREATED_AT_DESC;
        }
    }
}
