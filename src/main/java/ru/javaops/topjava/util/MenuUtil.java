package ru.javaops.topjava.util;

import lombok.experimental.UtilityClass;
import ru.javaops.topjava.model.Menu;
import ru.javaops.topjava.to.MenuTo;

import java.util.List;
import java.util.Optional;

@UtilityClass
public class MenuUtil {
    public static Menu createNewFromTo(MenuTo menuTo) {
        return new Menu(null, menuTo.getName(), menuTo.getDate(), menuTo.getPrice());
    }

    public static Menu updateFromTo(Menu menu, MenuTo menuTo) {
        menu.setName(menuTo.getName());
        menu.setPrice(menuTo.getPrice());
        menu.setDate(menuTo.getDate());
        return menu;
    }

    public static MenuTo convertFromMenu(Menu menu) {
        return new MenuTo(menu.getId(), menu.getName(), menu.getDate(), menu.getPrice());
    }
}