package org.fuel50.rating.services;

import org.fuel50.domains.Rating;
import org.fuel50.rating.mappers.RatingMapper;
import org.fuel50.dtos.RatingDto;
import org.fuel50.rating.repositories.RatingRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RatingService {
    private final RatingRepository repository;
    private final RatingMapper mapper;

    public RatingService(RatingRepository repository, RatingMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Flux<RatingDto> listByToUserActivity(Long userActivityId) {
        return repository.findByToUserActivityId(userActivityId).map(mapper::toDto);
    }

    public Flux<RatingDto> listByFromUser(Long userId) {
        return repository.findByFromUserId(userId).map(mapper::toDto);
    }

    public Mono<RatingDto> get(Long id) { return repository.findById(id).map(mapper::toDto); }

    public Mono<RatingDto> create(RatingDto dto) {
        Rating entity = mapper.toEntity(dto);
        return repository.save(entity).map(mapper::toDto);
    }

    public Mono<RatingDto> patch(Long id, RatingDto dto) {
        return repository.findById(id)
                .map(e -> mapper.merge(dto, e))
                .flatMap(repository::save)
                .map(mapper::toDto);
    }

    public Mono<Void> delete(Long id) { return repository.deleteById(id); }
}