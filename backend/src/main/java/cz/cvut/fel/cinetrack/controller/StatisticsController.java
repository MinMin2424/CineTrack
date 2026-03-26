/*
 * Created by minmin_tranova on 24.03.2026
 */

package cz.cvut.fel.cinetrack.controller;

import cz.cvut.fel.cinetrack.dto.statistics.CountryStatDTO;
import cz.cvut.fel.cinetrack.dto.statistics.FunfactsDTO;
import cz.cvut.fel.cinetrack.dto.statistics.GenreStatDTO;
import cz.cvut.fel.cinetrack.dto.statistics.MonthlyActiveDTO;
import cz.cvut.fel.cinetrack.dto.statistics.OtherStatDTO;
import cz.cvut.fel.cinetrack.dto.statistics.StatusOverviewDTO;
import cz.cvut.fel.cinetrack.dto.statistics.SummaryDTO;
import cz.cvut.fel.cinetrack.dto.statistics.TopRatedDTO;
import cz.cvut.fel.cinetrack.security.SecurityUtils;
import cz.cvut.fel.cinetrack.service.StatisticsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.print.attribute.standard.MediaSize;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/statistics")
@CrossOrigin(origins = "http://localhost:3000")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/status-overview")
    public ResponseEntity<?> getStatusOverview() {
        try {
            Long userId = SecurityUtils.getCurrentUserId();
            StatusOverviewDTO result = statisticsService.getStatusOverview(userId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/summary")
    public ResponseEntity<?> getSummary() {
        try {
            Long userId = SecurityUtils.getCurrentUserId();
            SummaryDTO result = statisticsService.getSummary(userId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/top-genres")
    public ResponseEntity<?> getTopGenres(
            @RequestParam(defaultValue = "15") int limit
    ) {
        try {
            Long userId = SecurityUtils.getCurrentUserId();
            List<GenreStatDTO> result = statisticsService.getTopGenres(userId, limit);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/top-countries")
    public ResponseEntity<?> getTopCountries(
            @RequestParam(defaultValue = "5") int limit
    ) {
        try {
            Long userId = SecurityUtils.getCurrentUserId();
            List<CountryStatDTO> result = statisticsService.getTopCountries(userId, limit);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/monthly-active")
    public ResponseEntity<?> getMonthlyActive(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month
    ) {
        try {
            Long userId = SecurityUtils.getCurrentUserId();
            int y = year != null ? year : LocalDateTime.now().getYear();
            int m = month != null ? month : LocalDateTime.now().getMonthValue();
            MonthlyActiveDTO result = statisticsService.getMonthlyActive(userId, y, m);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/other")
    public ResponseEntity<?> getOther() {
        try {
            Long userId = SecurityUtils.getCurrentUserId();
            OtherStatDTO result = statisticsService.getOtherStats(userId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/fun-facts")
    public ResponseEntity<?> getFunFacts() {
        try {
            Long userId = SecurityUtils.getCurrentUserId();
            FunfactsDTO result = statisticsService.getFunFacts(userId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/top-rated")
    public ResponseEntity<?> getTopRated() {
        try {
            Long userId = SecurityUtils.getCurrentUserId();
            TopRatedDTO result = statisticsService.getTopRated(userId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
