/*
 * Created by minmin_tranova on 29.11.2025
 */

package cz.cvut.fel.cinetrack.service;

import cz.cvut.fel.cinetrack.model.Genre;
import cz.cvut.fel.cinetrack.repository.GenreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

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

    @Test
    void getOrCreateGenres_WhenNullInput_ReturnEmptyList() {
        List<Genre> genres = genreService.getOrCreateGenres(null);

        assertNotNull(genres);
        assertTrue(genres.isEmpty());
    }

    @Test
    void getOrCreateGenres_WhenEmptyList_ReturnEmptyList() {
        List<Genre> genres = genreService.getOrCreateGenres(List.of());

        assertNotNull(genres);
        assertTrue(genres.isEmpty());
    }

    @Test
    void getOrCreateGenres_WhenNewGenres_CreatesAndReturnsThem() {
        List<String> genreTypes = Arrays.asList("Horror", "Sci-fi", "Drama");
        List<Genre> genres = genreService.getOrCreateGenres(genreTypes);

        assertNotNull(genres);
        assertEquals(3, genres.size());
        assertTrue(genres.stream().anyMatch(g -> "Horror".equals(g.getType())));
        assertTrue(genres.stream().anyMatch(g -> "Sci-fi".equals(g.getType())));
        assertTrue(genres.stream().anyMatch(g -> "Drama".equals(g.getType())));

        List<Genre> dbGenres = genreRepository.findAll();
        assertEquals(3, dbGenres.size());
    }

    @Test
    void getOrCreateGenres_WhenMixedExistingAndNewGenres_ReturnAll() {
        Genre existingGenre = new Genre();
        existingGenre.setType("Horror");
        genreRepository.save(existingGenre);

        List<String> genreTypes = Arrays.asList("Horror", "Sci-fi", "Drama");
        List<Genre> genres = genreService.getOrCreateGenres(genreTypes);

        assertEquals(3, genres.size());
        assertTrue(genres.stream().anyMatch(g -> "Horror".equals(g.getType())));
        assertTrue(genres.stream().anyMatch(g -> "Sci-fi".equals(g.getType())));
        assertTrue(genres.stream().anyMatch(g -> "Drama".equals(g.getType())));

        long count = genreRepository.count();
        assertEquals(3, count);
    }

    @Test
    void getOrCreatedGenres_WhenDuplicateNamesInInput_HandlesCorrectly() {
        List<String> genreTypes = Arrays.asList("Sci-fi", "Sci-fi", "Drama");
        List<Genre> genres = genreService.getOrCreateGenres(genreTypes);

        assertEquals(2, genres.size());
        assertEquals(2, genreRepository.count());
        assertTrue(genres.stream().anyMatch(g -> "Sci-fi".equals(g.getType())));
        assertTrue(genres.stream().anyMatch(g -> "Drama".equals(g.getType())));
    }
}
