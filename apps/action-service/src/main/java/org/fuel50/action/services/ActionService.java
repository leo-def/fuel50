package org.fuel50.action.services;

import org.fuel50.domains.Action;
import org.fuel50.action.mappers.ActionMapper;
import org.fuel50.dtos.ActionDto;
import org.fuel50.action.repositories.ActionRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ActionService {
    private final ActionRepository repository;
    private final ActionMapper mapper;

    public ActionService(ActionRepository repository, ActionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Flux<ActionDto> listByCompany(Long companyId) {
        return repository.findByCompanyId(companyId).map(mapper::toDto);
    }

    public Flux<ActionDto> listByCompanyAndActivity(Long companyId, Long activityId) {
        return repository.findByCompanyIdAndActivityId(companyId, activityId).map(mapper::toDto);
    }

    public Mono<ActionDto> getWithinCompany(Long companyId, Long id) {
        return repository.findById(id)
                .filter(a -> a.getCompanyId() != null && a.getCompanyId().equals(companyId))
                .map(mapper::toDto);
    }

    public Mono<ActionDto> createForCompany(Long companyId, ActionDto dto) {
        Action entity = mapper.toEntity(dto);
        entity.setCompanyId(companyId);
        return repository.save(entity).map(mapper::toDto);
    }

    public Mono<ActionDto> createForCompanyAndActivity(Long companyId, Long activityId, ActionDto dto) {
        Action entity = mapper.toEntity(dto);
        entity.setCompanyId(companyId);
        entity.setActivityId(activityId);
        return repository.save(entity).map(mapper::toDto);
    }

    public Mono<ActionDto> patchWithinCompany(Long companyId, Long id, ActionDto dto) {
        return repository.findById(id)
                .filter(a -> a.getCompanyId() != null && a.getCompanyId().equals(companyId))
                .map(a -> mapper.merge(dto, a))
                .flatMap(repository::save)
                .map(mapper::toDto);
    }

    public Mono<Void> deleteWithinCompany(Long companyId, Long id) {
        return repository.findById(id)
                .filter(a -> a.getCompanyId() != null && a.getCompanyId().equals(companyId))
                .flatMap(a -> repository.deleteById(a.getId()));
    }
}