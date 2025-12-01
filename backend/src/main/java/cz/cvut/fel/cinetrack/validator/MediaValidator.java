/*
 * Created by minmin_tranova on 18.11.2025
 */

package cz.cvut.fel.cinetrack.validator;

import cz.cvut.fel.cinetrack.exception.media.InvalidDatesException;
import cz.cvut.fel.cinetrack.exception.media.InvalidRatingException;
import cz.cvut.fel.cinetrack.exception.media.nonNullData.DatesCannotBeNullException;
import cz.cvut.fel.cinetrack.exception.media.nonNullData.MediaInputCannotBeNullException;
import cz.cvut.fel.cinetrack.model.enums.MediaType;
import cz.cvut.fel.cinetrack.model.enums.StatusEnum;
import cz.cvut.fel.cinetrack.model.enums.ValidationMessage;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class MediaValidator {

    public static void validateInputs(
            String title,
            String runtime,
            String year,
            String poster,
            String status,
            String season,
            MediaType type
    ) {
        if (title == null || title.trim().isEmpty()) throw new MediaInputCannotBeNullException(ValidationMessage.TITLE_REQUIRED.getMessage());
        if (poster == null || poster.trim().isEmpty()) throw new MediaInputCannotBeNullException(ValidationMessage.POSTER_REQUIRED.getMessage());
        if (status == null || status.trim().isEmpty()) throw new MediaInputCannotBeNullException(ValidationMessage.STATUS_REQUIRED.getMessage());
        if (type == MediaType.MOVIE) {
            if (runtime == null || runtime.trim().isEmpty()) throw new MediaInputCannotBeNullException(ValidationMessage.RUNTIME_REQUIRED.getMessage());
            if (year == null || year.trim().isEmpty()) throw new MediaInputCannotBeNullException(ValidationMessage.YEAR_REQUIRED.getMessage());
        }
        if (type == MediaType.SERIES) {
            if (season == null || season.trim().isEmpty()) throw new MediaInputCannotBeNullException(ValidationMessage.SEASON_REQUIRED.getMessage());
        }
    }

    public static void validateStatusDates(StatusEnum status, LocalDate startDate, LocalDate endDate) {
        if (status == StatusEnum.COMPLETED && (startDate == null || endDate == null)) {
            throw new DatesCannotBeNullException(ValidationMessage.DATES_REQUIRED.getMessage());
        }
        validateDates(startDate, endDate);
    }

    public static void validateDates(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new InvalidDatesException(ValidationMessage.INVALID_DATES.getMessage());
        }
    }

    public static void validateRating(float rating) {
        if (rating < 0 || rating > 10) {
            throw new InvalidRatingException(ValidationMessage.INVALID_RATING.getMessage());
        }
    }

}
