package org.fuel50.user.repositories;

import org.fuel50.domains.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {
    Flux<User> findByCompanyId(Long companyId);
}