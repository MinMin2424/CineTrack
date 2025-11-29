/*
 * Created by minmin_tranova on 11.11.2025
 */

package cz.cvut.fel.cinetrack.service;

import cz.cvut.fel.cinetrack.model.Language;
import cz.cvut.fel.cinetrack.repository.LanguageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class LanguageService {

    private final LanguageRepository languageRepository;

    public LanguageService(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    public List<Language> getOrCreateLanguage(List<String> languages) {
        if (languages == null) return new ArrayList<>();
        List<String> uniqueLanguages = languages.stream()
                .distinct()
                .toList();
        List<Language> existingLanguages = languageRepository.findByLangIn(uniqueLanguages);
        Set<String> existingNames = existingLanguages.stream()
                .map(Language::getLang)
                .collect(Collectors.toSet());
        List<Language> result = new ArrayList<>(existingLanguages);
        for (String name : languages) {
            if (!existingNames.contains(name)) {
                Language newLanguage = new Language();
                newLanguage.setLang(name);
                Language savedLanguage = languageRepository.save(newLanguage);
                result.add(savedLanguage);
                existingNames.add(name);
            }
        }
        return result;
    }
}
