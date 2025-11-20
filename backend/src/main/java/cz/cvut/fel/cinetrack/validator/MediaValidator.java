/*
 * Created by minmin_tranova on 18.11.2025
 */

package cz.cvut.fel.cinetrack.validator;

import cz.cvut.fel.cinetrack.exception.media.InvalidDatesException;
import cz.cvut.fel.cinetrack.exception.media.nonNullData.DatesCannotBeNullException;
import cz.cvut.fel.cinetrack.exception.media.nonNullData.StartDateCannotBeNullException;
import cz.cvut.fel.cinetrack.model.enums.StatusEnum;
import cz.cvut.fel.cinetrack.model.enums.ValidationMessage;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class MediaValidator {

    public void validateStatusDates(StatusEnum status, LocalDate startDate, LocalDate endDate) {
        if (status == StatusEnum.COMPLETED && (startDate == null || endDate == null)) {
            throw new DatesCannotBeNullException(ValidationMessage.DATES_REQUIRED.getMessage());
        }
        if ((status == StatusEnum.WATCHING ||
                status == StatusEnum.PLAN_TO_WATCH ||
                status == StatusEnum.PAUSED ||
                status == StatusEnum.DROPPED) && startDate == null
        ) {
            throw new StartDateCannotBeNullException(ValidationMessage.START_DATE_REQUIRED.getMessage());
        }
        if (endDate != null && startDate != null && endDate.isBefore(startDate)) {
            throw new InvalidDatesException(ValidationMessage.INVALID_DATES.getMessage());
        }
    }

}
