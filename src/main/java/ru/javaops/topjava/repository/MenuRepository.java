package ru.javaops.topjava.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava.model.Menu;


import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

import static ru.javaops.topjava.util.validation.ValidationUtil.checkModification;


@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository<Menu> {

    @Query("select m from Menu m where m.restaurant.id = ?1 and m.date = ?2 order by m.name")
    List<Menu> findAllByRestaurantIdAndDate(int restaurantId, LocalDate date);

    @Query("select m from Menu m where m.id = ?1 and m.restaurant.id = ?2")
    Optional<Menu> findByIdAndRestaurantId(int id, int restaurantId);

    @Transactional
    @Modifying
    @Query("delete from Menu m where m.id =?1 and m.restaurant.id = ?2")
    int deleteByIdAndRestaurantId(int id, int restaurantId);

    @Query("select  m from Menu m where m.date =?1 order by m.name")
    List<Menu> findAllByDateAndOrderByName(LocalDate date);

    default void deleteExisted(int id, int restaurantId) {
        checkModification(deleteByIdAndRestaurantId(id, restaurantId), id, restaurantId);
    }
}