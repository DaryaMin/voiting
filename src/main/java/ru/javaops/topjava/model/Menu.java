package ru.javaops.topjava.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "menu", indexes = @Index(name = "menu_date", columnList = "menu_date DESC, restaurant_id"),
        uniqueConstraints = @UniqueConstraint(name = "uniq_name_date", columnNames = {"menu_date", "name", "restaurant_id"}))
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class Menu extends NamedEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    @ToString.Exclude
    private Restaurant restaurant;

    @Column(name = "menu_date", nullable = false)
    @NotNull
    private LocalDate date;

    @Column(name = "price", nullable = false)
    @NotNull
    @PositiveOrZero
    private int price;

    public Menu(Integer id, String name, LocalDate date, int price, Restaurant restaurant) {
        super(id, name);
        this.date = date;
        this.price = price;
        this.restaurant = restaurant;
    }

    public Menu(Integer id, String name, LocalDate date, int price) {
        this(id, name, date, price, null);
    }
}
