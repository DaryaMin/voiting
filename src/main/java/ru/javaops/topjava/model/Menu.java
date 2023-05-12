package ru.javaops.topjava.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.Objects;


@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "Menues", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "restaurant_id"}, name = "Menu_unique_for_restaurant_idx")})
public class Menu extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurant;

    @Size(max = 128)
    @NotNull
    private String name;

    @NotNull
    private int price;// price int penny, cents and ect.

    @Column(name = "created", nullable = false, columnDefinition = "timestamp default now()", updatable = false)
    @NotNull
    private LocalDate created;

    public Menu(Integer id, Restaurant restaurant, String name, int price, LocalDate created) {
        super(id);
        this.restaurant = restaurant;
        this.name = name;
        this.price = price;
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Menu Menu = (Menu) o;
        return Menu.price == price && Objects.equals(restaurant, Menu.restaurant) && Objects.equals(name, Menu.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), restaurant, name, price);
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
