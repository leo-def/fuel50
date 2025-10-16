package org.fuel50.activity.repositories;

import org.fuel50.domains.Activity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ActivityRepository extends ReactiveCrudRepository<Activity, Long> {
    Flux<Activity> findByCompanyId(Long companyId);
}