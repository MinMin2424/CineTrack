/*
 * Created by minmin_tranova on 31.10.2025
 */

package cz.cvut.fel.cinetrack.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "languages")
@Getter @Setter
public class Language {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lang", nullable = false)
    private String lang;

    @ManyToMany(mappedBy = "languages")
    private List<Series> series = new ArrayList<>();

    @ManyToMany(mappedBy = "languages")
    private List<Movie> movies = new ArrayList<>();

}
