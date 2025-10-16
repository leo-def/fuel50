package org.fuel50.activity.repositories;

import org.fuel50.domains.UserActivity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface UserActivityRepository extends ReactiveCrudRepository<UserActivity, Long> {
    Flux<UserActivity> findByUserId(Long userId);
    Flux<UserActivity> findByActivityId(Long activityId);
}