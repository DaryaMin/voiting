package ru.javaops.topjava.web.restaurant;

import ru.javaops.topjava.model.Restaurant;
import ru.javaops.topjava.to.RestaurantTo;
import ru.javaops.topjava.web.MatcherFactory;

import static ru.javaops.topjava.web.menu.AdminMenuTestData.*;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class);
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER_WITH_MENU =
            MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "vote", "menu.restaurant");
    public static final MatcherFactory.Matcher<RestaurantTo> RESTAURANT_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(RestaurantTo.class);

    public static final int RESTAURANT1_ID = 1;
    public static final int RESTAURANT2_ID = 2;
    public static final int RESTAURANT3_ID = 3;
    public static final int WRONG_RESTAURANT_ID = 555;
    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT1_ID, "Пушкин", menuSet1);
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT2_ID, "Воронеж", null);
    public static final Restaurant restaurant3 = new Restaurant(RESTAURANT3_ID, "Дрова", menuSet3);

    public static final RestaurantTo restaurantTo1 = new RestaurantTo(RESTAURANT1_ID, "Пушкин");
    public static final RestaurantTo restaurantTo2 = new RestaurantTo(RESTAURANT2_ID, "Воронеж");
    public static final RestaurantTo restaurantTo3 = new RestaurantTo(RESTAURANT3_ID, "Дрова");

    public static Restaurant getNew() {
        return new Restaurant(null, "NewRestaurant");
    }

    public static Restaurant getNotUniqueName() {
        return new Restaurant(null, "Пушкин");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT1_ID, "UpdatedName");
    }
}
