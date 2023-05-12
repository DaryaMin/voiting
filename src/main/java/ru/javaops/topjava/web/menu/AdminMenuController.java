package ru.javaops.topjava.web.menu;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.topjava.model.Menu;
import ru.javaops.topjava.repository.MenuRepository;
import ru.javaops.topjava.repository.RestaurantRepository;
import ru.javaops.topjava.to.MenuTo;
import ru.javaops.topjava.util.MenuUtil;

import java.net.URI;

import static ru.javaops.topjava.util.validation.ValidationUtil.assureIdConsistent;

@RestController
@RequestMapping(value = AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminMenuController {

    protected static final String REST_URL = "/api/admin/Menu";

    @Autowired
    private MenuRepository repository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @PostMapping("/restaurant/{id}")
    @Operation(summary = "admin create Menu for restaurant (id)")
    public ResponseEntity<MenuTo> create(@RequestBody @Valid MenuTo MenuTo, @PathVariable int id) {
        log.info("create {}", MenuTo);
        Menu Menu = MenuUtil.createNewFromTo(MenuTo);
        Menu.setRestaurant(restaurantRepository.getById(id));
        Menu saved = repository.save(Menu);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(MenuController.REST_URL + "/{id}")
                .buildAndExpand(saved.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(MenuUtil.createTo(saved));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "admin delete Menu")
    public void delete(@PathVariable int id) {
        log.info("delete Menu {}", id);
        repository.delete(id);
    }

    @PutMapping("/restaurant/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "admin update Menu")
    public void update(@RequestBody @Valid Menu Menu, @PathVariable int id) {
        log.info("update {}", Menu);
        assureIdConsistent(Menu.getRestaurant(), id);
        Menu.setRestaurant(restaurantRepository.getById(id));
        repository.save(Menu);
    }
}
