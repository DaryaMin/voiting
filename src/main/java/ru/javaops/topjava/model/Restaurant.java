package ru.javaops.topjava.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "restaurants", uniqueConstraints = {
        @UniqueConstraint(name = "uniq_name", columnNames = "name")})
@Getter
@Setter
@NoArgsConstructor
public class Restaurant extends NamedEntity {

    @OneToMany(fetch = FetchType.LAZY, targetEntity = Menu.class)
    @JoinColumn(name = "restaurant_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Set<Menu> menu;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @ToString.Exclude
    private Set<Vote> vote;

    public Restaurant(Integer id, String name) {
        this(id, name, null);
    }

    public Restaurant(Integer id, String name, Collection<Menu> menu) {
        super(id, name);
        setMenu(menu);
    }

    public void setMenu(Collection<Menu> menu) {
        this.menu = CollectionUtils.isEmpty(menu) ? Set.of() : Set.copyOf(menu);
    }
}
