/*
 * Created by minmin_tranova on 24.03.2026
 */

package cz.cvut.fel.cinetrack.service;

import cz.cvut.fel.cinetrack.dto.statistics.CountryStatDTO;
import cz.cvut.fel.cinetrack.dto.statistics.FunfactsDTO;
import cz.cvut.fel.cinetrack.dto.statistics.GenreStatDTO;
import cz.cvut.fel.cinetrack.dto.statistics.MonthlyActiveDTO;
import cz.cvut.fel.cinetrack.dto.statistics.OtherStatDTO;
import cz.cvut.fel.cinetrack.dto.statistics.StatusCountDTO;
import cz.cvut.fel.cinetrack.dto.statistics.StatusOverviewDTO;
import cz.cvut.fel.cinetrack.dto.statistics.SummaryDTO;
import cz.cvut.fel.cinetrack.dto.statistics.TopRatedDTO;
import cz.cvut.fel.cinetrack.dto.statistics.TopRatedItemDTO;
import cz.cvut.fel.cinetrack.model.Movie;
import cz.cvut.fel.cinetrack.model.Series;
import cz.cvut.fel.cinetrack.model.enums.StatusEnum;
import cz.cvut.fel.cinetrack.repository.MovieRepository;
import cz.cvut.fel.cinetrack.repository.SeriesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.print.attribute.standard.MediaSize;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
public class StatisticsService {

    private final MovieRepository movieRepository;
    private final SeriesRepository seriesRepository;

    public StatisticsService(MovieRepository movieRepository, SeriesRepository seriesRepository) {
        this.movieRepository = movieRepository;
        this.seriesRepository = seriesRepository;
    }

    /* STATUS OVERVIEW */
    public StatusOverviewDTO getStatusOverview(Long userId) {
        List<Movie> movies = getListMovies(userId);
        List<Series> series = getListSeries(userId);

        return new StatusOverviewDTO(
                buildStatusCount(movies, series, StatusEnum.COMPLETED),
                buildStatusCount(movies, series, StatusEnum.WATCHING),
                buildStatusCount(movies, series, StatusEnum.PLAN_TO_WATCH),
                buildStatusCount(movies, series, StatusEnum.DROPPED)
        );
    }

    private StatusCountDTO buildStatusCount(List<Movie> movies, List<Series> series, StatusEnum status) {
        int movieCount = (int) movies.stream().filter(m -> status == m.getStatus()).count();
        int seriesCount = (int) series.stream().filter(s -> status == s.getStatus()).count();
        return new StatusCountDTO(
                movieCount + seriesCount,
                movieCount,
                seriesCount
        );
    }

    /* SUMMARY */
    public SummaryDTO getSummary(Long userId) {
        int movieCount = getListMovies(userId).size();
        int seriesCount = getListSeries(userId).size();
        return new SummaryDTO(
                movieCount + seriesCount,
                movieCount,
                seriesCount
        );
    }

    /* TOP GENRES */
    public List<GenreStatDTO> getTopGenres(Long userId, int limit) {
        List<Movie> movies = getListMovies(userId);
        List<Series> series = getListSeries(userId);

        Map<String, Integer> genreCounts = new HashMap<>();

        movies.forEach(m -> m.getGenres().forEach(g ->
                genreCounts.merge(g.getType(), 1, Integer::sum)
        ));
        series.forEach(s -> s.getGenres().forEach(g ->
                genreCounts.merge(g.getType(), 1, Integer::sum)
        ));

        return genreCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(limit)
                .map(e -> new GenreStatDTO(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    /* TOP COUNTRIES */
    public List<CountryStatDTO> getTopCountries(Long userId, int limit) {
        List<Movie> movies = getListMovies(userId);
        List<Series> series = getListSeries(userId);

        Map<String, int[]> countryCounts = new HashMap<>();

        movies.forEach(m -> m.getCountries().forEach(c -> {
            countryCounts.computeIfAbsent(c.getCountryName(), k -> new int[2]);
            countryCounts.get(c.getCountryName())[0]++;
        }));
        series.forEach(m -> m.getCountries().forEach(c -> {
            countryCounts.computeIfAbsent(c.getCountryName(), k -> new int[2]);
            countryCounts.get(c.getCountryName())[1]++;
        }));

        return countryCounts.entrySet().stream()
                .map(e -> new CountryStatDTO(e.getKey(), e.getValue()[0], e.getValue()[1]))
                .sorted(Comparator.comparingInt(CountryStatDTO::getTotal).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    /* MONTHLY ACTIVE */
    public MonthlyActiveDTO getMonthlyActive(Long userId, int year, int month) {
        List<Movie> movies = getListMovies(userId);
        List<Series> series = getListSeries(userId);

        int movieCount = countAddedInMonth(movies.stream().map(Movie::getCreatedAt), year, month);
        int seriesCount = countAddedInMonth(series.stream().map(Series::getCreatedAt), year, month);
        int total = movieCount + seriesCount;

        int prevYear = month == 1 ? year - 1 : year;
        int prevMonth = month == 1 ? 12 : month - 1;
        int prevMovies = countAddedInMonth(movies.stream().map(Movie::getCreatedAt), prevYear, prevMonth);
        int prevSeries = countAddedInMonth(series.stream().map(Series::getCreatedAt), prevYear, prevMonth);

        return new MonthlyActiveDTO(
                year,
                month,
                total,
                movieCount,
                seriesCount,
                prevMovies + prevSeries
        );
    }

    private int countAddedInMonth(Stream<LocalDateTime> dates, int year, int month) {
        return (int) dates
                .filter(Objects::nonNull)
                .filter(d -> d.getYear() == year && d.getMonthValue() == month)
                .count();
    }

    /* OTHER STATS */
    public OtherStatDTO getOtherStats(Long userId) {
        int currentYear = LocalDateTime.now().getYear();

        List<Movie> movies = getListMovies(userId);
        List<Series> series = getListSeries(userId);

        int completedThisYear = (int) Stream.concat(
                movies.stream().filter(m -> m.getStatus() == StatusEnum.COMPLETED && m.getWatchEndDate() != null && m.getWatchEndDate().getYear() == currentYear),
                series.stream().filter(m -> m.getStatus() == StatusEnum.COMPLETED && m.getWatchEndDate() != null && m.getWatchEndDate().getYear() == currentYear)
        ).count();

        double avgRating = Stream.concat(
                movies.stream().map(Movie::getRating),
                series.stream().map(Series::getRating)
        )
                .filter(r -> r != null && r > 0)
                .mapToDouble(Float::doubleValue)
                .average()
                .orElse(0.0);

        double roundedAvg = Math.round(avgRating * 10.0) / 10.0;
        return new OtherStatDTO(completedThisYear, roundedAvg);
    }

    /* FUN FACTS */
    public FunfactsDTO getFunFacts(Long userId) {
        List<Movie> movies = getListMovies(userId);
        List<Series> series = getListSeries(userId);

        /* longest series by episode count */
        series.stream()
                .max(Comparator.comparingInt(Series::getEpisodes))
                .ifPresent(s -> {});
        Optional<Series> longestSeries = series.stream()
                .max(Comparator.comparingInt(Series::getEpisodes));

        /* longest movie by runtime */
        Optional<Movie> longestMovie = movies.stream()
                .filter(m -> m.getRuntime() != null && !m.getRuntime().isBlank())
                .max(Comparator.comparingInt(m -> parseRuntime(m.getRuntime())));

        /* unique countries */
        long uniqueCountries = Stream.concat(
                movies.stream().flatMap(m -> m.getCountries().stream())
                        .map(c -> c.getCountryName()),
                series.stream().flatMap(s -> s.getCountries().stream())
                        .map(c -> c.getCountryName())
        ).distinct().count();

        return new FunfactsDTO(
                longestSeries.map(Series::getEpisodes).orElse(null),
                longestSeries.map(Series::getTitle).orElse(null),
                longestMovie.map(m -> parseRuntime(m.getRuntime())).orElse(null),
                longestMovie.map(Movie::getTitle).orElse(null),
                (int) uniqueCountries
        );
    }

    private int parseRuntime(String runtime) {
        if (runtime == null || runtime.isBlank()) return 0;
        try {
            return Integer.parseInt(runtime.trim().split(" ")[0]);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public TopRatedDTO getTopRated(Long userId) {
        List<Movie> movies = getListMovies(userId);
        List<Series> series = getListSeries(userId);

        Optional<Movie> topMovie = movies.stream()
                .filter(m -> m.getRating() > 0)
                .max(Comparator.comparingDouble(Movie::getRating));
        Optional<Series> topSeries = series.stream()
                .filter(s -> s.getRating() > 0)
                .max(Comparator.comparingDouble(Series::getRating));

        return new TopRatedDTO(
                topMovie.map(m -> new TopRatedItemDTO(m.getTitle(), m.getRating())).orElse(null),
                topSeries.map(s -> new TopRatedItemDTO(s.getTitle(), s.getRating())).orElse(null)
        );
    }

    private List<Movie> getListMovies(Long userId) {
        return movieRepository.findNotDeletedMoviesByUserId(userId);
    }

    private List<Series> getListSeries(Long userId) {
        return seriesRepository.findNotDeletedSeriesByUserId(userId);
    }
}
