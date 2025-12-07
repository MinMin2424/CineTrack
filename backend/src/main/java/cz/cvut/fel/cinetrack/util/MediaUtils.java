/*
 * Created by minmin_tranova on 18.11.2025
 */

package cz.cvut.fel.cinetrack.util;

import cz.cvut.fel.cinetrack.dto.media.MediaItemDTO;
import cz.cvut.fel.cinetrack.model.enums.EpisodeStatusEnum;
import cz.cvut.fel.cinetrack.model.enums.StatusEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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

    public static EpisodeStatusEnum parseEpisodeStatus(String input) {
        return switch(input) {
            case "completed" -> EpisodeStatusEnum.COMPLETED;
            case "watching" -> EpisodeStatusEnum.WATCHING;
            case "plan to watch" -> EpisodeStatusEnum.PLAN_TO_WATCH;
            case "dropped" -> EpisodeStatusEnum.DROPPED;
            case "paused" -> EpisodeStatusEnum.PAUSED;
            default -> EpisodeStatusEnum.NONE;
        };
    }

    public static List<MediaItemDTO> sortMediaItems(List<MediaItemDTO> mediaItems, String sortBy) {
        if (sortBy == null || sortBy.isEmpty()) {
            return mediaItems.stream()
                    .sorted(Comparator.comparing(MediaItemDTO::getCreatedAt).reversed())
                    .toList();
        }
        return switch (sortBy.toUpperCase()) {
            case "TITLE_ASC" -> mediaItems.stream()
                    .sorted(Comparator.comparing(MediaItemDTO::getTitle, String.CASE_INSENSITIVE_ORDER))
                    .toList();
            case "TITLE_DESC" -> mediaItems.stream()
                    .sorted(Comparator.comparing(MediaItemDTO::getTitle, String.CASE_INSENSITIVE_ORDER).reversed())
                    .toList();
            case "RELEASE_YEAR_ASC" -> mediaItems.stream()
                    .sorted(Comparator.comparing(MediaItemDTO::getReleaseYear))
                    .toList();
            case "RELEASE_YEAR_DESC" -> mediaItems.stream()
                    .sorted(Comparator.comparing(MediaItemDTO::getReleaseYear).reversed())
                    .toList();
            case "CREATED_AT_ASC" -> mediaItems.stream()
                    .sorted(Comparator.comparing(MediaItemDTO::getCreatedAt))
                    .toList();
            default -> mediaItems.stream()
                    .sorted(Comparator.comparing(MediaItemDTO::getCreatedAt).reversed())
                    .toList();
        };
    }

    public static boolean matchesSearchTerm(String title, String searchTerm) {
        if (title == null || searchTerm == null || searchTerm.trim().isEmpty()) {
            return false;
        }
        String normalizedTitle = title.toLowerCase();
        if (normalizedTitle.equals(searchTerm)) {
            return true;
        }
        if (normalizedTitle.startsWith(searchTerm)) {
            return true;
        }
        if (normalizedTitle.contains(searchTerm)) {
            return true;
        }
        String[] titleWords = normalizedTitle.split("[\\s\\-_:,()+]");
        for (String word : titleWords) {
            if (word.startsWith(searchTerm)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isExactMatch(String title, String searchTerm) {
        if (title == null || searchTerm == null) {
            return false;
        }
        return title.toLowerCase().equals(searchTerm);
    }
}
