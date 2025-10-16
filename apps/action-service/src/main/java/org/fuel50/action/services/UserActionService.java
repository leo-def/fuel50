package org.fuel50.action.services;

import org.fuel50.domains.UserAction;
import org.fuel50.action.mappers.UserActionMapper;
import org.fuel50.dtos.UserActionDto;
import org.fuel50.action.repositories.UserActionRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserActionService {
    private final UserActionRepository repository;
    private final UserActionMapper mapper;

    public UserActionService(UserActionRepository repository, UserActionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Flux<UserActionDto> listByUser(Long userId) {
        return repository.findByUserId(userId).map(mapper::toDto);
    }

    public Flux<UserActionDto> listByAction(Long actionId) {
        return repository.findByActionId(actionId).map(mapper::toDto);
    }

    public Mono<UserActionDto> get(Long id) { return repository.findById(id).map(mapper::toDto); }

    public Mono<UserActionDto> create(UserActionDto dto) {
        UserAction entity = mapper.toEntity(dto);
        return repository.save(entity).map(mapper::toDto);
    }

    public Mono<UserActionDto> patch(Long id, UserActionDto dto) {
        return repository.findById(id)
                .map(e -> mapper.merge(dto, e))
                .flatMap(repository::save)
                .map(mapper::toDto);
    }

    public Mono<Void> delete(Long id) { return repository.deleteById(id); }
}