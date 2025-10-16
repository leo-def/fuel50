package org.fuel50.activity.services;

import org.fuel50.domains.Activity;
import org.fuel50.activity.mappers.ActivityMapper;
import org.fuel50.dtos.ActivityDto;
import org.fuel50.activity.repositories.ActivityRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ActivityService {
    private final ActivityRepository repository;
    private final ActivityMapper mapper;

    public ActivityService(ActivityRepository repository, ActivityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Flux<ActivityDto> listByCompany(Long companyId) {
        return repository.findByCompanyId(companyId).map(mapper::toDto);
    }

    public Flux<ActivityDto> listByCompanyAndUser(Long companyId, Long userId) {
        return repository.findByCompanyId(companyId)
                .filter(a -> a.getCreatedBy() != null && a.getCreatedBy().equals(userId))
                .map(mapper::toDto);
    }

    public Mono<ActivityDto> getWithinCompany(Long companyId, Long id) {
        return repository.findById(id)
                .filter(a -> a.getCompanyId() != null && a.getCompanyId().equals(companyId))
                .map(mapper::toDto);
    }

    public Mono<ActivityDto> getWithinCompanyAndUser(Long companyId, Long userId, Long id) {
        return repository.findById(id)
                .filter(a -> a.getCompanyId() != null && a.getCompanyId().equals(companyId))
                .filter(a -> a.getCreatedBy() != null && a.getCreatedBy().equals(userId))
                .map(mapper::toDto);
    }

    public Mono<ActivityDto> createForCompany(Long companyId, ActivityDto dto) {
        Activity entity = mapper.toEntity(dto);
        entity.setCompanyId(companyId);
        return repository.save(entity).map(mapper::toDto);
    }

    public Mono<ActivityDto> createForCompanyAndUser(Long companyId, Long userId, ActivityDto dto) {
        Activity entity = mapper.toEntity(dto);
        entity.setCompanyId(companyId);
        entity.setCreatedBy(userId);
        return repository.save(entity).map(mapper::toDto);
    }

    public Mono<ActivityDto> patchWithinCompany(Long companyId, Long id, ActivityDto dto) {
        return repository.findById(id)
                .filter(a -> a.getCompanyId() != null && a.getCompanyId().equals(companyId))
                .map(a -> mapper.merge(dto, a))
                .flatMap(repository::save)
                .map(mapper::toDto);
    }

    public Mono<ActivityDto> patchWithinCompanyAndUser(Long companyId, Long userId, Long id, ActivityDto dto) {
        return repository.findById(id)
                .filter(a -> a.getCompanyId() != null && a.getCompanyId().equals(companyId))
                .filter(a -> a.getCreatedBy() != null && a.getCreatedBy().equals(userId))
                .map(a -> mapper.merge(dto, a))
                .flatMap(repository::save)
                .map(mapper::toDto);
    }

    public Mono<Void> deleteWithinCompany(Long companyId, Long id) {
        return repository.findById(id)
                .filter(a -> a.getCompanyId() != null && a.getCompanyId().equals(companyId))
                .flatMap(a -> repository.deleteById(a.getId()));
    }

    public Mono<Void> deleteWithinCompanyAndUser(Long companyId, Long userId, Long id) {
        return repository.findById(id)
                .filter(a -> a.getCompanyId() != null && a.getCompanyId().equals(companyId))
                .filter(a -> a.getCreatedBy() != null && a.getCreatedBy().equals(userId))
                .flatMap(a -> repository.deleteById(a.getId()));
    }
}