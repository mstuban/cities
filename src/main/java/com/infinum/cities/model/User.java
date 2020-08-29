package com.infinum.cities.model;

import com.infinum.cities.exception.CannotAddCityException;
import com.infinum.cities.exception.CityAlreadyAddedException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user", schema = "public")
public class User implements UserDetails {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    @SequenceGenerator(name = "user_generator", sequenceName = "seq_user_id", allocationSize = 1)
    private Long id;

    @NotBlank(message = "{user.email.notBlank}")
    @NotNull(message = "{user.email.notNull}")
    @Email(message = "{user.email.format}")
    @Column(name = "email", unique = true)
    private String email;

    @NotBlank(message = "{user.password.notBlank}")
    @NotNull(message = "{user.password.notNull}")
    @JsonIgnore
    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_city",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "city_id")
    )
    private Set<City> favoriteCities;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email) {
        this.email = email;
    }

    @Transactional
    public User addFavoriteCity(City city) {
        if (favoriteCities.contains(city)) {
            throw new CityAlreadyAddedException("User " + email + " already added " + city.getName() + " as favorite");
        }
        favoriteCities.add(city);
        city.incrementNumberOfFavorites();
        return this;
    }

    @Transactional
    public User removeFavoriteCity(City city) {
        if (!favoriteCities.contains(city)) {
            throw new CannotAddCityException("User " + email + " does not have " + city.getName() + " as favorite");
        }
        favoriteCities.remove(city);
        city.decrementNumberOfFavorites();
        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
