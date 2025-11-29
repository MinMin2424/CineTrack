/*
 * Created by minmin_tranova on 21.11.2025
 */

package cz.cvut.fel.cinetrack.service;

import cz.cvut.fel.cinetrack.model.Country;
import cz.cvut.fel.cinetrack.repository.CountryRepository;
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
public class CountryServiceTest {

    @Autowired
    private CountryService countryService;

    @Autowired
    private CountryRepository countryRepository;

    @Test
    void getOrCreateCountries_WhenNullInput_ReturnEmptyList() {
        List<Country> countries = countryService.getOrCreateCountries(null);

        assertNotNull(countries);
        assertTrue(countries.isEmpty());
    }

    @Test
    void getOrCreateCountries_WhenEmptyList_ReturnEmptyList() {
        List<Country> countries = countryService.getOrCreateCountries(List.of());

        assertNotNull(countries);
        assertTrue(countries.isEmpty());
    }

    @Test
    void getOrCreateCountries_WhenNewCountries_CreatesAndReturnsThem() {
        List<String> countryNames = Arrays.asList("United States", "Germany", "France");
        List<Country> countries = countryService.getOrCreateCountries(countryNames);

        assertNotNull(countries);
        assertEquals(3, countries.size());
        assertTrue(countries.stream().anyMatch(c -> "United States".equals(c.getCountryName())));
        assertTrue(countries.stream().anyMatch(c -> "Germany".equals(c.getCountryName())));
        assertTrue(countries.stream().anyMatch(c -> "France".equals(c.getCountryName())));

        List<Country> dbCountries = countryRepository.findAll();
        assertEquals(3, dbCountries.size());
    }

    @Test
    void getOrCreateCountries_WhenMixedExistingAndNewCountries_ReturnsAll() {
        Country existingCountry = new Country();
        existingCountry.setCountryName("United States");
        countryRepository.save(existingCountry);

        List<String> countryNames = Arrays.asList("United States", "Germany", "France");
        List<Country> countries = countryService.getOrCreateCountries(countryNames);

        assertEquals(3, countries.size());
        assertTrue(countries.stream().anyMatch(c -> "United States".equals(c.getCountryName())));
        assertTrue(countries.stream().anyMatch(c -> "Germany".equals(c.getCountryName())));
        assertTrue(countries.stream().anyMatch(c -> "France".equals(c.getCountryName())));

        long count = countryRepository.count();
        assertEquals(3, count);
    }

    @Test
    void getOrCreateCountries_WhenDuplicateNamesInInput_HandlesCorrectly() {
        List<String> countryNames = Arrays.asList("Germany", "Germany", "France");
        List<Country> countries = countryService.getOrCreateCountries(countryNames);

        assertEquals(2, countries.size());
        assertEquals(2, countryRepository.count());
        assertTrue(countries.stream().anyMatch(c -> "Germany".equals(c.getCountryName())));
        assertTrue(countries.stream().anyMatch(c -> "France".equals(c.getCountryName())));
    }
}
