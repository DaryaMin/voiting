package ru.javaops.topjava.web.restaurant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import ru.javaops.topjava.error.IllegalRequestDataException;
import ru.javaops.topjava.error.NotFoundException;
import ru.javaops.topjava.model.Restaurant;
import ru.javaops.topjava.repository.RestaurantRepository;
import ru.javaops.topjava.to.RestaurantTo;
import ru.javaops.topjava.util.RestaurantUtil;

import java.util.List;

public class RestaurantRestController {
    public static final String RESTAURANT_NOT_FOUND = "Restaurant not found";

    @Autowired
    private RestaurantRepository restaurantRepository;

    public RestaurantTo get(int id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Entity with id=" + id + " not found"));
        return RestaurantUtil.convertFromRestaurant(restaurant);
    }

    public List<Restaurant> getAll() {
        return restaurantRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }
}