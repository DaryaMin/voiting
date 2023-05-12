package ru.javaops.topjava.web.menu;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javaops.topjava.repository.MenuRepository;
import ru.javaops.topjava.to.MenuTo;
import ru.javaops.topjava.util.MenuUtil;
import ru.javaops.topjava.web.AuthUser;

import java.util.Optional;

@RestController
@RequestMapping(value = MenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class MenuController {

    protected static final String REST_URL = "/api/Menu";

    @Autowired
    private MenuRepository repository;

    @GetMapping("/{id}")
    @Operation(summary = "authorized user get Menu by id")
    public ResponseEntity<MenuTo> get(@PathVariable int id, @AuthenticationPrincipal AuthUser authUser) {
        log.info("get Menu {}", id);
        MenuTo MenuTo = MenuUtil.createTo(repository.getById(id));
        return ResponseEntity.of(Optional.of(MenuTo));
    }
}
