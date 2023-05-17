package ru.javaops.topjava.web.menu;

import ru.javaops.topjava.model.Menu;
import ru.javaops.topjava.to.MenuTo;
import ru.javaops.topjava.util.MenuUtil;
import ru.javaops.topjava.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static ru.javaops.topjava.web.restaurant.RestaurantTestData.restaurant1;


public class AdminMenuTestData {
    public static final MatcherFactory.Matcher<Menu> MENU_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Menu.class);
    public static final MatcherFactory.Matcher<MenuTo> MENU_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(MenuTo.class);

    public static final int MENU1_ID = 11;
    public static final int MENU2_ID = 12;
    public static final int MENU3_ID = 13;
    public static final int MENU4_ID = 14;
    public static final int MENU5_ID = 15;
    public static final int MENU6_ID = 16;
    public static final int MENU7_ID = 17;
    public static final int NEW_MENU_ID = 21;

    public static final LocalDate TODAY = LocalDate.now();

    public static final Menu menu1 = new Menu(MENU1_ID, "Суп", TODAY, 50);
    public static final Menu menu2 = new Menu(MENU2_ID, "Чай", TODAY, 25);
    public static final Menu menu3 = new Menu(MENU3_ID, "Пшенка", TODAY, 60);
    public static final Menu menu4 = new Menu(MENU4_ID, "Рыба", TODAY, 150);
    public static final Menu menu5 = new Menu(MENU5_ID, "Солянка", TODAY, 100);
    public static final Menu menu6 = new Menu(MENU6_ID, "Котлета", TODAY, 160);
    public static final Menu menu7 = new Menu(MENU7_ID, "Кофе", TODAY, 60);

    public static final Set<Menu> menuSet1 = Set.of(menu1, menu2, menu3, menu4);
    public static final Set<Menu> menuSet3 = Set.of(menu5, menu6, menu7);

    public static final List<MenuTo> todayMenu = Stream.of(menu6, menu7, menu3, menu4, menu5, menu1, menu2).map(MenuUtil::convertFromMenu).toList();

    public static MenuTo getNew() {
        return new MenuTo(null, "New", TODAY, 99);
    }

    public static MenuTo getWithNotUniqueDateAndName() {
        return new MenuTo(null, menu1.getName(), TODAY, 99);
    }

    public static MenuTo getNewWithWrongData() {
        return new MenuTo(null, "N", TODAY, 99);
    }

    public static MenuTo getNewAfterSaveInRepo() {
        return new MenuTo(NEW_MENU_ID, "New", TODAY, 99);
    }

    public static Menu getUpdated() {
        return new Menu(MENU1_ID, "New", TODAY, 99, restaurant1);
    }
}
