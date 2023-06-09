package ru.javaops.topjava.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.topjava.model.User;
import ru.javaops.topjava.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VotesRepository extends BaseRepository<Vote> {

    @Query("select v from Vote v where v.user.id =?1 and v.date =?2")
    Optional<Vote> findByUserIdAndDate(int userId, LocalDate date);

    @Query("select COUNT (v.restaurant.id) from Vote v where v.restaurant.id =?1 and v.date =?2 group by v.restaurant.id")
    Optional<Integer> getCountVotesByRestaurantIdAndDate(int restaurantId, LocalDate date);

    @Query("select v.date, r.id, r.name from Vote v, Restaurant r where v.restaurant.id = r.id and v.user.id =?1 order by v.date desc ")
    List<Object[]> findAllByUserId(Integer id);
}
