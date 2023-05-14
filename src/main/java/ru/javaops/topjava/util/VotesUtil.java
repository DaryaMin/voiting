package ru.javaops.topjava.util;

import lombok.experimental.UtilityClass;
import ru.javaops.topjava.model.Restaurant;
import ru.javaops.topjava.model.Vote;
import ru.javaops.topjava.to.VoteTo;

import java.time.LocalDate;


@UtilityClass
public class VotesUtil {
    public static VoteTo convertFromRestaurantAndVote(LocalDate date, Restaurant restaurant) {
        return new VoteTo(date, RestaurantUtil.convertFromRestaurant(restaurant));
    }

    public static VoteTo convertFromVote(Vote vote) {
        return new VoteTo(vote.getDate(), RestaurantUtil.convertFromRestaurant(vote.getRestaurant()));
    }
}
