/*
 * Created by minmin_tranova on 29.11.2025
 */

package cz.cvut.fel.cinetrack.service;

import cz.cvut.fel.cinetrack.model.Language;
import cz.cvut.fel.cinetrack.repository.LanguageRepository;
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
public class LanguageServiceTest {

    @Autowired
    private LanguageService languageService;

    @Autowired
    private LanguageRepository languageRepository;

    @ParameterizedTest
    @MethodSource("provideEmptyLanguageLists")
    void getOrCreateLanguage_WhenNullOrEmptyInput_ReturnEmptyList(List<String> input) {
        List<Language> languages = languageService.getOrCreateLanguage(input);

        assertNotNull(languages);
        assertTrue(languages.isEmpty());
    }

    private static Stream<Arguments> provideEmptyLanguageLists() {
        return Stream.of(
                Arguments.of(Collections.emptyList()),
                Arguments.of(List.of("")),
                Arguments.of(List.of(" ", " ")),
                Arguments.of(Arrays.asList(null, ""))
        );
    }

    @ParameterizedTest
    @MethodSource("provideLanguageScenarios")
    void getOrCreateLanguage_WhenNewLanguages_CreatesAndReturnsThem(
            List<String> inputLanguages,
            List<String> preExistingLanguages,
            int expectedResultSize,
            int expectedDBSize,
            List<String> expectedLanguages
    ) {
        if (preExistingLanguages != null && !preExistingLanguages.isEmpty()) {
            preExistingLanguages.forEach(languageName -> {
                Language existingLanguage = new Language();
                existingLanguage.setLang(languageName);
                languageRepository.save(existingLanguage);
            });
        }
        List<Language> result = languageService.getOrCreateLanguage(inputLanguages);

        assertNotNull(result);
        assertEquals(expectedResultSize, result.size());

        if (expectedLanguages != null) {
            for (String expectedLanguage : expectedLanguages) {
                assertTrue(result.stream().anyMatch(lang -> expectedLanguage.equals(lang.getLang())),
                        "Missing language: " + expectedLanguage);
            }
        }

        assertEquals(expectedDBSize, languageRepository.count());
    }

    private static Stream<Arguments> provideLanguageScenarios() {
        return Stream.of(
                // Test 1: New genres
                Arguments.of(
                        Arrays.asList("English", "Spanish", "French"),
                        null,
                        3,
                        3,
                        Arrays.asList("English", "Spanish", "French")
                ),
                // Test 2: Mix existing with new genres
                Arguments.of(
                        Arrays.asList("English", "Spanish", "French"),
                        Arrays.asList("English"),
                        3,
                        3,
                        Arrays.asList("English", "Spanish", "French")
                ),
                // Test 3: Duplicate names in input
                Arguments.of(
                        Arrays.asList("English", "English", "French"),
                        null,
                        2,
                        2,
                        Arrays.asList("English", "French")
                ),
                // Test 4: All genres already exist
                Arguments.of(
                        Arrays.asList("English", "Spanish"),
                        Arrays.asList("English", "Spanish"),
                        2,
                        2,
                        Arrays.asList("English", "Spanish")
                )
        );
    }

    @Test
    void getOrCreateLanguages_WhenContainsEmptyString_FiltersThemOut() {
        List<String> input = Arrays.asList("English", "", "Spanish", null);
        List<Language> result = languageService.getOrCreateLanguage(input);

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(lang -> "English".equals(lang.getLang())));
        assertTrue(result.stream().anyMatch(lang -> "Spanish".equals(lang.getLang())));
        assertEquals(2, languageRepository.count());
    }

    @Test
    void getOrCreateLanguages_WhenCallMultipleTimes_IsIdempotent() {
        List<String> input = Arrays.asList("English", "Spanish");

        List<Language> firstResult = languageService.getOrCreateLanguage(input);
        long firstDBCount = languageRepository.count();

        List<Language> secondResult = languageService.getOrCreateLanguage(input);
        long secondDBCount = languageRepository.count();

        List<Language> thirdResult = languageService.getOrCreateLanguage(input);
        long thirdDBCount = languageRepository.count();

        assertEquals(2, firstResult.size());
        assertEquals(2, secondResult.size());
        assertEquals(2, thirdResult.size());

        assertEquals(firstDBCount, secondDBCount);
        assertEquals(secondDBCount, thirdDBCount);
        assertEquals(2, firstDBCount);
    }

    @Test
    void getOrCreateLanguage_WithLargeNumberOfLanguages_HandlesCorrectly() {
        List<String> input = Arrays.asList(
                "English", "Spanish", "French", "German", "Italian",
                "Portuguese", "Russian", "Japanese", "Chinese", "Korean",
                "Arabic", "Hindi", "Dutch", "Swedish", "Norwegian",
                "Danish", "Finnish", "Polish", "Czech", "Slovak"
        );
        List<Language> result = languageService.getOrCreateLanguage(input);

        assertEquals(20, result.size());
        assertEquals(20, languageRepository.count());

        for (String language: input) {
            assertTrue(result.stream().anyMatch(lang -> language.equals(lang.getLang())),
                    "Missing language: " + language);
        }
    }
}
