package com.infinum.cities.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static com.infinum.cities.auth.constants.StandardConstants.DATE_TIME_FORMAT;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "city", schema = "public")
public class City {

    @Id
    @Column(name = "city_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "city_generator")
    @SequenceGenerator(name = "city_generator", sequenceName = "seq_city_id", allocationSize = 1)
    private Long id;

    @NotNull(message = "{city.name.notNull}")
    @NotBlank(message = "{city.name.notBlank}")
    @Column(name = "name", unique = true)
    private String name;

    @NotNull(message = "{city.population.notNull}")
    @Column(name = "population")
    private Integer population;

    @NotNull(message = "{city.description.notNull}")
    @NotBlank(message = "{city.description.notBlank}")
    @Column(name = "description")
    private String description;

    @JsonFormat(pattern = DATE_TIME_FORMAT)
    @Column(name = "created")
    private LocalDateTime created;

    // number displaying how many users marked this city as favorite
    @JsonFormat(pattern = DATE_TIME_FORMAT)
    @Column(name = "number_of_favorites")
    private Integer numberOfFavorites = 0;

    public City(String name, Integer population, String description) {
        this.name = name;
        this.population = population;
        this.description = description;
    }

    @PrePersist
    public void createNewCity() {
        created = LocalDateTime.now();
    }

    @Transactional
    public void incrementNumberOfFavorites() {
        ++numberOfFavorites;
    }

    @Transactional
    public void decrementNumberOfFavorites() {
        --numberOfFavorites;
    }
}
