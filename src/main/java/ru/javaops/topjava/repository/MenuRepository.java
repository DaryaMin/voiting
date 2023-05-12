package ru.javaops.topjava.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava.error.NotFoundException;
import ru.javaops.topjava.model.Menu;
import ru.javaops.topjava.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static ru.javaops.topjava.config.SecurityConfig.PASSWORD_ENCODER;

@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository<Menu> {

    @Query("SELECT m FROM Menu m WHERE m.restaurant.id=:restaurant_id ORDER BY m.created DESC, m.price ASC")
    Optional<Menu> getMenu(@Param("restaurant_id") int restaurantId);

    @Transactional
    default Menu prepareAndSave(Menu menu) {
        return save(menu);
    }
}