package ru.javaops.topjava.web.restaurant;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javaops.topjava.model.Menu;
import ru.javaops.topjava.repository.MenuRepository;
import ru.javaops.topjava.repository.RestaurantRepository;
import ru.javaops.topjava.to.RestaurantTo;
import ru.javaops.topjava.util.RestaurantUtil;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = RestaurantRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class RestaurantRestController {
    protected static final String REST_URL = "/api/restaurant";

    @Autowired
    private RestaurantRepository repository;

    @Autowired
    private MenuRepository menuRepository;

    @GetMapping("/{id}")
    @Operation(summary = "authorized user get restaurant by id")
    public ResponseEntity<RestaurantTo> get(@PathVariable int id) {
        log.info("get restaurant {}", id);
        RestaurantTo restaurantTo = RestaurantUtil.createTo(repository.getById(id));
        return ResponseEntity.of(Optional.of(restaurantTo));
    }


    @GetMapping
    @Operation(summary = "authorized user get all restaurants")
    public List<RestaurantTo> getAll() {
        log.info("get all restaurants");
        return RestaurantUtil.getTos(repository.findAll(Sort.by(Sort.Direction.ASC, "name")));
    }

    @GetMapping("/{id}/menus")
    @Operation(summary = "user get menues of restaurants (id) which actual from date to date")
    public List<Menu> getMenu(@PathVariable int id) {
        log.info("get menu for restaurant {}", id);
        return menuRepository.getMenu(id).stream().toList();
    }
}