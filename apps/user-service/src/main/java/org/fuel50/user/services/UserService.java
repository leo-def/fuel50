package org.fuel50.user.services;

import org.fuel50.domains.User;
import org.fuel50.user.mappers.UserMapper;
import org.fuel50.dtos.UserDto;
import org.fuel50.user.repositories.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    public UserService(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Flux<UserDto> listByCompany(Long companyId) {
        return repository.findByCompanyId(companyId).map(mapper::toDto);
    }

    public Mono<UserDto> getWithinCompany(Long companyId, Long id) {
        return repository.findById(id)
                .filter(u -> u.getCompanyId() != null && u.getCompanyId().equals(companyId))
                .map(mapper::toDto);
    }

    public Mono<UserDto> createForCompany(Long companyId, UserDto dto) {
        User entity = mapper.toEntity(dto);
        entity.setCompanyId(companyId);
        return repository.save(entity).map(mapper::toDto);
    }

    public Mono<UserDto> patchWithinCompany(Long companyId, Long id, UserDto dto) {
        return repository.findById(id)
                .filter(u -> u.getCompanyId() != null && u.getCompanyId().equals(companyId))
                .map(u -> mapper.merge(dto, u))
                .flatMap(repository::save)
                .map(mapper::toDto);
    }

    public Mono<Void> deleteWithinCompany(Long companyId, Long id) {
        return repository.findById(id)
                .filter(u -> u.getCompanyId() != null && u.getCompanyId().equals(companyId))
                .flatMap(u -> repository.deleteById(u.getId()));
    }
}