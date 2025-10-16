package org.fuel50.action.repositories;

import org.fuel50.domains.UserAction;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface UserActionRepository extends ReactiveCrudRepository<UserAction, Long> {
    Flux<UserAction> findByUserId(Long userId);
    Flux<UserAction> findByActionId(Long actionId);
}