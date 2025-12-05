/*
 * Created by minmin_tranova on 29.11.2025
 */

package cz.cvut.fel.cinetrack.service;

import cz.cvut.fel.cinetrack.model.Genre;
import cz.cvut.fel.cinetrack.repository.GenreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@ComponentScan(basePackages = "cz.cvut.fel.cinetrack")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GenreServiceTest {

    @Autowired
    private GenreService genreService;

    @Autowired
    private GenreRepository genreRepository;

    @ParameterizedTest
    @NullSource
    @MethodSource("provideEmptyGenreLists")
    void getOrCreateGenres_WhenNullOrEmptyInput_ReturnEmptyList(List<String> input) {
        List<Genre> genres = genreService.getOrCreateGenres(input);

        assertNotNull(genres);
        assertTrue(genres.isEmpty());
    }

    private static Stream<Arguments> provideEmptyGenreLists() {
        return Stream.of(
                Arguments.of(Collections.emptyList()),
                Arguments.of(List.of("")),
                Arguments.of(List.of(" ", " ")),
                Arguments.of(Arrays.asList(null, ""))
        );
    }

    @ParameterizedTest
    @MethodSource("provideGenreScenarios")
    void getOrCreateGenres_WhenNewGenres_CreatesAndReturnsThem(
            List<String> inputGenres,
            List<String> preExistingGenres,
            int expectedResultSize,
            int expectedDBSize,
            List<String> expectedGenres
    ) {
        // Setup
        if (preExistingGenres != null && !preExistingGenres.isEmpty()) {
            preExistingGenres.forEach(genreName -> {
                Genre existingGenre = new Genre();
                existingGenre.setType(genreName);
                genreRepository.save(existingGenre);
            });
        }
        // Act
        List<Genre> result = genreService.getOrCreateGenres(inputGenres);

        //Assert
        assertNotNull(result);
        assertEquals(expectedResultSize, result.size());

        if (expectedGenres != null) {
            for (String expectedGenre : expectedGenres) {
                assertTrue(result.stream().anyMatch(g -> expectedGenre.equals(g.getType())),
                        "Missing genre: " + expectedGenre);
            }
        }

        assertEquals(expectedDBSize, genreRepository.count());
    }

    private static Stream<Arguments> provideGenreScenarios() {
        return Stream.of(
                // Test 1: New genres
                Arguments.of(
                        Arrays.asList("Horror", "Sci-fi", "Drama"),
                        null,
                        3,
                        3,
                        Arrays.asList("Horror", "Sci-fi", "Drama")
                ),
                // Test 2: Mix existing with new genres
                Arguments.of(
                        Arrays.asList("Horror", "Sci-fi", "Drama"),
                        Arrays.asList("Horror"),
                        3,
                        3,
                        Arrays.asList("Horror", "Sci-fi", "Drama")
                ),
                // Test 3: Duplicate names in input
                Arguments.of(
                        Arrays.asList("Sci-fi", "Sci-fi", "Drama"),
                        null,
                        2,
                        2,
                        Arrays.asList("Sci-fi", "Drama")
                ),
                // Test 4: All genres already exist
                Arguments.of(
                        Arrays.asList("Action", "Comedy"),
                        Arrays.asList("Action", "Comedy"),
                        2,
                        2,
                        Arrays.asList("Action", "Comedy")
                )
        );
    }

    @Test
    void getOrCreatedGenres_WhenContainsEmptyStrings_FiltersThemOut() {
        List<String> input = Arrays.asList("Action", "", "Comedy", " ", null);
        List<Genre> result = genreService.getOrCreateGenres(input);

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(g -> "Action".equals(g.getType())));
        assertTrue(result.stream().anyMatch(g -> "Comedy".equals(g.getType())));
        assertEquals(2, genreRepository.count());
    }

    @Test
    void getOrCreateGenres_WhenCalledMultipleTimes_IsIdempotent() {
        List<String> input = Arrays.asList("Horror", "Comedy");

        List<Genre> firstResult = genreService.getOrCreateGenres(input);
        long firstDBCount = genreRepository.count();

        List<Genre> secondResult = genreService.getOrCreateGenres(input);
        long secondDBCount = genreRepository.count();

        List<Genre> thirdResult = genreService.getOrCreateGenres(input);
        long thirdDBCount = genreRepository.count();

        assertEquals(2, firstResult.size());
        assertEquals(2, secondResult.size());
        assertEquals(2, thirdResult.size());

        assertEquals(firstDBCount, secondDBCount);
        assertEquals(secondDBCount, thirdDBCount);
        assertEquals(2, firstDBCount);
    }

    @Test
    void getOrCreateGenres_WithLargeNumberOfGenres_HandlesCorrectly() {
        List<String> input = Arrays.asList(
                "Action", "Adventure", "Animation", "Comedy", "Crime",
                "Documentary", "Drama", "Family", "Fantasy", "History",
                "Horror", "Music", "Mystery", "Romance", "Science Fiction",
                "TV Movie", "Thriller", "War", "Western"
        );
        List<Genre> result = genreService.getOrCreateGenres(input);

        assertEquals(19, result.size());
        assertEquals(19, genreRepository.count());

        for (String genre: input) {
            assertTrue(result.stream().anyMatch(g -> genre.equals(g.getType())),
            "Missing genre: " + genre);
        }
    }
}
