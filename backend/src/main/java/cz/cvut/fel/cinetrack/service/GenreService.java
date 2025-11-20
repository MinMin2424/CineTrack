/*
 * Created by minmin_tranova on 11.11.2025
 */

package cz.cvut.fel.cinetrack.service;

import cz.cvut.fel.cinetrack.model.Genre;
import cz.cvut.fel.cinetrack.repository.GenreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<Genre> getOrCreateGenres(List<String> types) {
        if (types == null) return new ArrayList<>();
        List<Genre> existingGenres = genreRepository.findByTypeIn(types);
        Set<String> existingTypes = existingGenres.stream()
                .map(Genre::getType)
                .collect(Collectors.toSet());
        List<Genre> result = new ArrayList<>(existingGenres);
        for (String type: types) {
            if (!existingTypes.contains(type)) {
                Genre newGenre = new Genre();
                newGenre.setType(type);
                result.add(genreRepository.save(newGenre));
            }
        }
        return result;
    }
}
