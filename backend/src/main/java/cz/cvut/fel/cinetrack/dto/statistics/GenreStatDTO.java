/*
 * Created by minmin_tranova on 24.03.2026
 */

package cz.cvut.fel.cinetrack.dto.statistics;

public class GenreStatDTO {

    private String genre;
    private int count;

    public GenreStatDTO(String genre, int count) {
        this.genre = genre;
        this.count = count;
    }

    public String getGenre() {
        return genre;
    }

    public int getCount() {
        return count;
    }
}
