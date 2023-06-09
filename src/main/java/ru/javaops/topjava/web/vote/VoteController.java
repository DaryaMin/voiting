package ru.javaops.topjava.web.vote;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.javaops.topjava.error.IllegalRequestDataException;
import ru.javaops.topjava.model.Restaurant;
import ru.javaops.topjava.repository.RestaurantRepository;
import ru.javaops.topjava.repository.VotesRepository;
import ru.javaops.topjava.service.VoteService;
import ru.javaops.topjava.to.VoteTo;
import ru.javaops.topjava.util.RestaurantUtil;
import ru.javaops.topjava.web.AuthUser;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(value = VoteController.REST_VOTES)
@Slf4j
public class VoteController {

    static final String REST_VOTES = "/api/votes";

    static final String VOTE_NOT_FOUND = "Vote not found";

    @Autowired
    private VoteService voteService;

    @Autowired
    private VotesRepository voteRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<String> vote(@RequestParam int restaurantId, @AuthenticationPrincipal AuthUser authUser) {
        log.info("vote for restaurant id={} by user={}", restaurantId, authUser);
        return voteService.vote(restaurantId, authUser.getUser());
    }

    @GetMapping("/restaurants/{id}")
    public Integer getVotesForRestaurantId(@PathVariable int id, @RequestParam @Nullable LocalDate date) {
        log.info("get votes for restaurant id={} for date={}", id, date);
        date = Objects.requireNonNullElseGet(date, LocalDate::now);
        return voteRepository.getCountVotesByRestaurantIdAndDate(id, date).orElse(0);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VoteTo> getVotes(@RequestParam @Nullable LocalDate date, @AuthenticationPrincipal AuthUser authUser) {
        if (date != null) {
            log.info("get vote for date={} for user={}", date, authUser);
            Optional<Restaurant> restaurantOptional = restaurantRepository.getByDateAndUserId(date, authUser.getUser().getId());
            if (restaurantOptional.isEmpty()) {
                throw new IllegalRequestDataException(VOTE_NOT_FOUND);
            }
            return List.of(new VoteTo(date, RestaurantUtil.convertFromRestaurant(restaurantOptional.get())));
        } else {
            log.info("get all votes for user={}", authUser);
            return voteService.findAllByUserId(authUser.getUser().getId());
        }
    }
}