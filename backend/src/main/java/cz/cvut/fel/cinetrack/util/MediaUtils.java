/*
 * Created by minmin_tranova on 18.11.2025
 */

package cz.cvut.fel.cinetrack.util;

import cz.cvut.fel.cinetrack.model.enums.StatusEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MediaUtils {

    public static List<String> parseStringToList(String input) {
        if (input == null || input.trim().isEmpty() || "N/A".equals(input)) {
            return new ArrayList<>();
        }
        return Arrays.stream(input.split(",\\s*"))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    public static int parseStringToInt(String input) {
        if (input == null || input.trim().isEmpty() || "N/A".equals(input)) {
            return 0;
        }
        return Integer.parseInt(input);
    }

    public static float parseStringToFloat(String input) {
        if (input == null || input.trim().isEmpty() || "N/A".equals(input)) {
            return 0f;
        }
        return Float.parseFloat(input);
    }

    public static int parseYear(String input) {
        if (input == null || input.trim().isEmpty() || "N/A".equals(input)) {
            return 0;
        }
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Error parsing year: " + input);
        }
        try {
            String number = input.trim().replaceAll("[^0-9]", "");
            if (number.length() >= 4) {
                String year = number.substring(0, 4);
                return Integer.parseInt(year);
            }
        } catch (NumberFormatException e) {
            return 0;
        }
        return 0;
    }

    public static String parseRuntime(String input) {
        if (input == null || input.trim().isEmpty() || "N/A".equals(input)) {
            return "Unknown";
        }
        return input;
    }

    public static StatusEnum parseStatus(String input) {
        return switch (input) {
            case "completed" -> StatusEnum.COMPLETED;
            case "watching" -> StatusEnum.WATCHING;
            case "plan to watch" -> StatusEnum.PLAN_TO_WATCH;
            case "dropped" -> StatusEnum.DROPPED;
            case "paused" -> StatusEnum.PAUSED;
            default -> null;
        };
    }
}
