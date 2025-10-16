package org.fuel50.company.services;

import org.fuel50.domains.Company;
import org.fuel50.company.mappers.CompanyMapper;
import org.fuel50.dtos.CompanyDto;
import org.fuel50.company.repositories.CompanyRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CompanyService {
    private final CompanyRepository repository;
    private final CompanyMapper mapper;

    public CompanyService(CompanyRepository repository, CompanyMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Flux<CompanyDto> list() {
        return repository.findAll().map(mapper::toDto);
    }

    public Mono<CompanyDto> get(Long id) {
        return repository.findById(id).map(mapper::toDto);
    }

    public Mono<CompanyDto> create(CompanyDto dto) {
        Company entity = mapper.toEntity(dto);
        return repository.save(entity).map(mapper::toDto);
    }

    public Mono<CompanyDto> patch(Long id, CompanyDto dto) {
        return repository.findById(id)
                .map(entity -> mapper.merge(dto, entity))
                .flatMap(repository::save)
                .map(mapper::toDto);
    }

    public Mono<Void> delete(Long id) {
        return repository.deleteById(id);
    }
}