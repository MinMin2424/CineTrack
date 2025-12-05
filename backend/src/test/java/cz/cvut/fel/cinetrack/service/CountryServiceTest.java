/*
 * Created by minmin_tranova on 21.11.2025
 */

package cz.cvut.fel.cinetrack.service;

import com.sun.source.tree.CompilationUnitTree;
import cz.cvut.fel.cinetrack.model.Country;
import cz.cvut.fel.cinetrack.repository.CountryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
public class CountryServiceTest {

    @Autowired
    private CountryService countryService;

    @Autowired
    private CountryRepository countryRepository;

    @ParameterizedTest
    @MethodSource("provideEmptyCountryLists")
    void getOrCreateCountries_WhenNullInput_ReturnEmptyList(List<String> input) {
        List<Country> countries = countryService.getOrCreateCountries(input);

        assertNotNull(countries);
        assertTrue(countries.isEmpty());
    }

    private static Stream<Arguments> provideEmptyCountryLists() {
        return Stream.of(
                Arguments.of(Collections.emptyList()),
                Arguments.of(List.of("")),
                Arguments.of(List.of(" ", " ")),
                Arguments.of(Arrays.asList(null, ""))
        );
    }

    @ParameterizedTest
    @MethodSource("provideCountryScenarios")
    void getOrCreateCountries_WhenNewCountries_CreatesAndReturnsThem(
            List<String> inputCountries,
            List<String> preExistingCountries,
            int expectedResultSize,
            int expectedDBSize,
            List<String> expectedCountries
    ) {
        if (preExistingCountries != null && !preExistingCountries.isEmpty()) {
            preExistingCountries.forEach(countryName -> {
                Country existingCountry = new Country();
                existingCountry.setCountryName(countryName);
                countryRepository.save(existingCountry);
            });
        }
        List<Country> result = countryService.getOrCreateCountries(inputCountries);

        assertNotNull(result);
        assertEquals(expectedResultSize, result.size());

        if (expectedCountries != null) {
            for (String expectedCountry : expectedCountries) {
                assertTrue(result.stream().anyMatch(country -> expectedCountry.equals(country.getCountryName())),
                        "Missing country: " + expectedCountry);
            }
        }

        assertEquals(expectedDBSize, countryRepository.count());
    }

    private static Stream<Arguments> provideCountryScenarios() {
        return Stream.of(
                // Test 1: New genres
                Arguments.of(
                        Arrays.asList("United States", "United Kingdom", "France"),
                        null,
                        3,
                        3,
                        Arrays.asList("United States", "United Kingdom", "France")
                ),
                // Test 2: Mix existing with new genres
                Arguments.of(
                        Arrays.asList("United States", "United Kingdom", "France"),
                        Arrays.asList("United States"),
                        3,
                        3,
                        Arrays.asList("United States", "United Kingdom", "France")
                ),
                // Test 3: Duplicate names in input
                Arguments.of(
                        Arrays.asList("United States", "United States", "France"),
                        null,
                        2,
                        2,
                        Arrays.asList("United States", "France")
                ),
                // Test 4: All genres already exist
                Arguments.of(
                        Arrays.asList("United States", "United Kingdom"),
                        Arrays.asList("United States", "United Kingdom"),
                        2,
                        2,
                        Arrays.asList("United States", "United Kingdom")
                )
        );
    }

    @Test
    void getOrCreateCountries_WhenContainsEmptyString_FiltersThemOut() {
        List<String> input = Arrays.asList("United States", "", "United Kingdom", " ", null);
        List<Country> result = countryService.getOrCreateCountries(input);

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(country -> "United States".equals(country.getCountryName())));
        assertTrue(result.stream().anyMatch(country -> "United Kingdom".equals(country.getCountryName())));
        assertEquals(2, countryRepository.count());
    }

    @Test
    void getOrCreateCountries_WhenCallMultipleTimes_IsIdempotent() {
        List<String> input = Arrays.asList("United States", "United Kingdom");

        List<Country> firstResult = countryService.getOrCreateCountries(input);
        long firstDBSize = countryRepository.count();

        List<Country> secondResult = countryService.getOrCreateCountries(input);
        long secondDBSize = countryRepository.count();

        List<Country> thirdResult = countryService.getOrCreateCountries(input);
        long thirdDBSize = countryRepository.count();

        assertEquals(2, firstResult.size());
        assertEquals(2, secondResult.size());
        assertEquals(2, thirdResult.size());

        assertEquals(firstDBSize, secondDBSize);
        assertEquals(secondDBSize, thirdDBSize);
        assertEquals(2, firstDBSize);

    }

    @Test
    void getOrCreateCountries_WithLargeNumberOfCountries_HandlesCorrectly() {
        List<String> input = Arrays.asList(
                "United States", "France", "United Kingdom", "Germany", "Italy",
                "Spain", "Canada", "Australia", "Japan", "China",
                "South Korea", "Brazil", "Mexico", "India", "Russia",
                "Netherlands", "Sweden", "Norway", "Denmark", "Finland"
        );
        List<Country> result = countryService.getOrCreateCountries(input);

        assertEquals(20, result.size());
        assertEquals(20, countryRepository.count());

        for (String countryName : input) {
            assertTrue(result.stream().anyMatch(country -> country.getCountryName().equals(countryName)),
                    "Missing country: " + countryName);
        }
    }
}
