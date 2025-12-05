/*
 * Created by minmin_tranova on 11.11.2025
 */

package cz.cvut.fel.cinetrack.service;

import cz.cvut.fel.cinetrack.model.Country;
import cz.cvut.fel.cinetrack.repository.CountryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class CountryService {

    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public List<Country> getOrCreateCountries(List<String> countryName) {
        if (countryName == null) return new ArrayList<>();

        List<String> validNames = countryName.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(name -> !name.isEmpty())
                .distinct()
                .toList();
        if (validNames.isEmpty()) return new ArrayList<>();

        List<Country> existingCountries = countryRepository.findByCountryNameIn(validNames);
        Set<String> existingNames = existingCountries.stream()
                .map(Country::getCountryName)
                .collect(Collectors.toSet());

        List<Country> result = new ArrayList<>(existingCountries);
        for (String name : validNames) {
            if (!existingNames.contains(name)) {
                Country newCountry = new Country();
                newCountry.setCountryName(name);
                Country savedCountry = countryRepository.save(newCountry);
                result.add(savedCountry);
                existingNames.add(name);
            }
        }
        return result;
    }
}
