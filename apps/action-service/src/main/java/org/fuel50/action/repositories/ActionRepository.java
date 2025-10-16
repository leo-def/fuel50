package org.fuel50.action.repositories;

import org.fuel50.domains.Action;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ActionRepository extends ReactiveCrudRepository<Action, Long> {
    Flux<Action> findByCompanyId(Long companyId);
    Flux<Action> findByCompanyIdAndActivityId(Long companyId, Long activityId);
}