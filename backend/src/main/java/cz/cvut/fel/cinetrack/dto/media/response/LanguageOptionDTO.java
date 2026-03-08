/*
 * Created by minmin_tranova on 08.03.2026
 */

package cz.cvut.fel.cinetrack.dto.media.response;

import cz.cvut.fel.cinetrack.model.Language;

public class LanguageOptionDTO {
    private Long id;
    private String lang;

    public LanguageOptionDTO(Language language) {
        this.id = language.getId();
        this.lang = language.getLang();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
