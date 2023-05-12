package ru.javaops.topjava.util;

import lombok.experimental.UtilityClass;
import ru.javaops.topjava.model.Menu;
import ru.javaops.topjava.to.MenuTo;

import java.util.List;
import java.util.Optional;

@UtilityClass
public class MenuUtil {
    public static MenuTo createTo(Menu Menu) {
        return new MenuTo(Menu.getId(), Menu.getName(), Menu.getPrice());
    }

    public  List<MenuTo> getTos(Optional<Menu> Menues) {
        return Menues.stream().map(MenuUtil::createTo).toList();
    }

    public static Menu createNewFromTo(MenuTo MenuTo) {
        return new Menu(null, null, MenuTo.getName(), MenuTo.getPrice(), DateTimeUtil.getNowDate());
    }
}