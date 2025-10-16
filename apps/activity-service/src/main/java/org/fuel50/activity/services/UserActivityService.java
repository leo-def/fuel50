package org.fuel50.activity.services;

import org.fuel50.domains.UserActivity;
import org.fuel50.activity.mappers.UserActivityMapper;
import org.fuel50.dtos.UserActivityDto;
import org.fuel50.activity.repositories.UserActivityRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserActivityService {
    private final UserActivityRepository repository;
    private final UserActivityMapper mapper;

    public UserActivityService(UserActivityRepository repository, UserActivityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Mono<UserActivityDto> get(Long id) {
        return repository.findById(id).map(mapper::toDto);
    }

    public Flux<UserActivityDto> listByUser(Long userId) {
        return repository.findByUserId(userId).map(mapper::toDto);
    }

    public Flux<UserActivityDto> listByActivity(Long activityId) {
        return repository.findByActivityId(activityId).map(mapper::toDto);
    }

    public Mono<UserActivityDto> findLink(Long userId, Long activityId) {
        return repository.findByUserId(userId)
                .filter(ua -> ua.getActivityId() != null && ua.getActivityId().equals(activityId))
                .next()
                .map(mapper::toDto);
    }

    public Mono<UserActivityDto> createLink(UserActivityDto dto) {
        UserActivity entity = mapper.toEntity(dto);
        return repository.save(entity).map(mapper::toDto);
    }

    public Mono<UserActivityDto> patch(Long id, UserActivityDto dto) {
        return repository.findById(id)
                .map(e -> mapper.merge(dto, e))
                .flatMap(repository::save)
                .map(mapper::toDto);
    }

    public Mono<Void> delete(Long id) {
        return repository.deleteById(id);
    }
}