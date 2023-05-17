package ru.javaops.topjava.web.menu;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.topjava.error.IllegalRequestDataException;
import ru.javaops.topjava.model.Menu;
import ru.javaops.topjava.model.Restaurant;
import ru.javaops.topjava.repository.MenuRepository;
import ru.javaops.topjava.repository.RestaurantRepository;
import ru.javaops.topjava.to.MenuTo;
import ru.javaops.topjava.util.MenuUtil;

import java.net.URI;
import java.time.LocalDate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static ru.javaops.topjava.util.MenuUtil.*;
import static ru.javaops.topjava.util.validation.ValidationUtil.assureIdConsistent;
import static ru.javaops.topjava.util.validation.ValidationUtil.checkNew;
import static ru.javaops.topjava.web.restaurant.RestaurantRestController.RESTAURANT_NOT_FOUND;


@RestController
@RequestMapping(value = MenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@CacheConfig(cacheNames = "menu")
public class MenuController {
    static final String REST_URL = "/api/admin/restaurants";
    static final String MENU_NOT_FOUND = "menu not found";
    static final String WRONG_RESTAURANT_ID_OR_MENU_ID = "Wrong restaurant id or menu id";

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @GetMapping("/{restaurantId}/menu")
    public List<MenuTo> getAllByRestaurantId(@PathVariable int restaurantId,
                                             @RequestParam(required = false) LocalDate date) {
        log.info("get all menu for restaurant id={} for date={}", restaurantId, date);
        date = Objects.requireNonNullElseGet(date, LocalDate::now);
        return menuRepository.findAllByRestaurantIdAndDate(restaurantId, date)
                .stream().map(MenuUtil::convertFromMenu).toList();
    }

    @GetMapping("/menu")
    @Cacheable
    public List<MenuTo> getAllByDate(@RequestParam(required = false) LocalDate date) {
        log.info("get all menu for date={}", date);
        date = Objects.requireNonNullElseGet(date, LocalDate::now);
        return menuRepository.findAllByDateAndOrderByName(date).stream()
                .map(MenuUtil::convertFromMenu)
                .toList();
    }

    @GetMapping("/{restaurantId}/menu/{id}")
    public MenuTo get(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("get menu with id={} for restaurant id={}", id, restaurantId);
        Menu menu = menuRepository.findByIdAndRestaurantId(id, restaurantId)
                .orElseThrow(() -> new IllegalRequestDataException(MENU_NOT_FOUND));
        return convertFromMenu(menu);
    }

    @PostMapping("/{restaurantId}/menu")
    @Transactional
    @CacheEvict(cacheNames = "menu", allEntries = true)
    public ResponseEntity<MenuTo> create(@Valid @RequestBody MenuTo menuTo, @PathVariable int restaurantId) {
        log.info("create {} for restaurant id={}", menuTo, restaurantId);
        checkNew(menuTo);
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new IllegalRequestDataException(RESTAURANT_NOT_FOUND);
        }
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        Menu menu = createNewFromTo(menuTo);
        menu.setRestaurant(restaurant);
        menuRepository.save(menu);
        MenuTo createdTo = convertFromMenu(menu);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(menu.getRestaurant().getId(), menu.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(createdTo);
    }

    @PutMapping(value = "/{restaurantId}/menu/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @CacheEvict(cacheNames = "menu", allEntries = true)
    public void update(@Valid @RequestBody MenuTo menuTo, @PathVariable int restaurantId, @PathVariable int id) {
        log.info("update {} with id={} for restaurant id={}", menuTo, id, restaurantId);
        assureIdConsistent(menuTo, id);
        Optional<Menu> menu = menuRepository.findById(id);
        if (menu.isPresent() && menu.get().getRestaurant().getId() == restaurantId) {
            updateFromTo(menu.get(), menuTo);
            menuRepository.save(menu.get());
        } else {
            throw new IllegalRequestDataException(WRONG_RESTAURANT_ID_OR_MENU_ID);
        }
    }

    @DeleteMapping("/{restaurantId}/menu/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(cacheNames = "menu", allEntries = true)
    public void delete(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("delete menu id={} for restaurant id={}", id, restaurantId);
        menuRepository.deleteExisted(id, restaurantId);
    }
}
