package org.fuel50.rating.repositories;

import org.fuel50.domains.Rating;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface RatingRepository extends ReactiveCrudRepository<Rating, Long> {
    Flux<Rating> findByToUserActivityId(Long toUserActivityId);
    Flux<Rating> findByFromUserId(Long fromUserId);
}