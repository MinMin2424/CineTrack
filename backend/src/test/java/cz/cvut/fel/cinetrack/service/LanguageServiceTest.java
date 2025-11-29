/*
 * Created by minmin_tranova on 29.11.2025
 */

package cz.cvut.fel.cinetrack.service;

import cz.cvut.fel.cinetrack.model.Language;
import cz.cvut.fel.cinetrack.repository.LanguageRepository;
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
public class LanguageServiceTest {

    @Autowired
    private LanguageService languageService;

    @Autowired
    private LanguageRepository languageRepository;

    @Test
    void getOrCreateLanguage_WhenNullInput_ReturnEmptyList() {
        List<Language> languages = languageService.getOrCreateLanguage(null);

        assertNotNull(languages);
        assertTrue(languages.isEmpty());
    }

    @Test
    void getOrCreateLanguage_WhenEmptyList_ReturnEmptyList() {
        List<Language> languages = languageService.getOrCreateLanguage(List.of());

        assertNotNull(languages);
        assertTrue(languages.isEmpty());
    }

    @Test
    void getOrCreateLanguage_WhenNewLanguages_CreatesAndReturnsThem() {
        List<String> languageNames = Arrays.asList("English", "Spanish", "Korean");
        List<Language> languages = languageService.getOrCreateLanguage(languageNames);

        assertNotNull(languages);
        assertEquals(3, languages.size());
        assertTrue(languages.stream().anyMatch(l -> "English".equals(l.getLang())));
        assertTrue(languages.stream().anyMatch(l -> "Spanish".equals(l.getLang())));
        assertTrue(languages.stream().anyMatch(l -> "Korean".equals(l.getLang())));

        List<Language> dbLanguages = languageRepository.findAll();
        assertEquals(3, dbLanguages.size());
    }

    @Test
    void getOrCreateLanguage_WhenMixedExistingAndNewLanguages_ReturnsAll() {
        Language existingLanguage = new Language();
        existingLanguage.setLang("English");
        languageRepository.save(existingLanguage);

        List<String> languageNames = Arrays.asList("English", "Spanish", "Korean");
        List<Language> languages = languageService.getOrCreateLanguage(languageNames);

        assertEquals(3, languages.size());
        assertTrue(languages.stream().anyMatch(l -> "English".equals(l.getLang())));
        assertTrue(languages.stream().anyMatch(l -> "Spanish".equals(l.getLang())));
        assertTrue(languages.stream().anyMatch(l -> "Korean".equals(l.getLang())));

        long count = languageRepository.count();
        assertEquals(3, count);
    }

    @Test
    void getOrCreateLanguage_WhenDuplicateNamesInput_HandlesCorrectly() {
        List<String> languageNames = Arrays.asList("English", "English", "Korean");
        List<Language> languages = languageService.getOrCreateLanguage(languageNames);

        assertEquals(2, languages.size());
        assertEquals(2, languageRepository.count());
        assertTrue(languages.stream().anyMatch(l -> "English".equals(l.getLang())));
        assertTrue(languages.stream().anyMatch(l -> "Korean".equals(l.getLang())));
    }
}
