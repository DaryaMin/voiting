package ru.javaops.topjava.util;

import lombok.experimental.UtilityClass;
import ru.javaops.topjava.model.Vote;
import ru.javaops.topjava.to.VoteTo;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

@UtilityClass
public class VotesUtil {

    public static final LocalTime CHECK_TIME = LocalTime.of(11, 0);

    public static VoteTo createTo(Vote vote) {
        return new VoteTo(vote.getId(), vote.getRestaurant().getId(), vote.getDate());
    }

    public static List<VoteTo> getTos(Collection<Vote> votes) {
        return votes.stream().map(VotesUtil::createTo).toList();
    }

    public static boolean isTimeBeforeThreshold() {
        LocalTime nowTime = LocalDateTime.now().toLocalTime();
        return !nowTime.isAfter(CHECK_TIME);
    }
}
