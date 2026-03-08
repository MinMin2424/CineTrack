/*
 * Created by minmin_tranova on 08.03.2026
 */

package cz.cvut.fel.cinetrack.controller;

import cz.cvut.fel.cinetrack.dto.media.CountryOptionDTO;
import cz.cvut.fel.cinetrack.dto.media.GenreOptionDTO;
import cz.cvut.fel.cinetrack.dto.media.response.LanguageOptionDTO;
import cz.cvut.fel.cinetrack.service.CountryService;
import cz.cvut.fel.cinetrack.service.GenreService;
import cz.cvut.fel.cinetrack.service.LanguageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/media/metadata")
@CrossOrigin(origins = "http://localhost:3000")
public class MediaMetadataController {

    private final GenreService genreService;
    private final LanguageService languageService;
    private final CountryService countryService;

    public MediaMetadataController(GenreService genreService,
                                   LanguageService languageService,
                                   CountryService countryService) {
        this.genreService = genreService;
        this.languageService = languageService;
        this.countryService = countryService;
    }

    @GetMapping("/genres")
    public ResponseEntity<List<GenreOptionDTO>> getGenres() {
        List<GenreOptionDTO> genres = genreService.getAllGenres();
        return ResponseEntity.ok(genres);
    }

    @GetMapping("/languages")
    public ResponseEntity<List<LanguageOptionDTO>> getLanguages() {
        List<LanguageOptionDTO> languages = languageService.getAllLanguages();
        return ResponseEntity.ok(languages);
    }

    @GetMapping("/countries")
    public ResponseEntity<List<CountryOptionDTO>> getCountries() {
        List<CountryOptionDTO> countries = countryService.getAllCountries();
        return ResponseEntity.ok(countries);
    }
}
