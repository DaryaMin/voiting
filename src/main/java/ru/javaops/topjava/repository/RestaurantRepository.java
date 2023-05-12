package ru.javaops.topjava.repository;

import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava.model.Restaurant;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {
}
