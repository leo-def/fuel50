package org.fuel50.company.mappers;

import org.fuel50.dtos.CompanyDto;
import org.fuel50.domains.Company;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {
    private final ModelMapper mapper;
    public CompanyMapper(ModelMapper mapper) { this.mapper = mapper; }

    public CompanyDto toDto(Company entity) {
        return mapper.map(entity, CompanyDto.class);
    }

    public Company toEntity(CompanyDto dto) {
        return mapper.map(dto, Company.class);
    }

    public Company merge(CompanyDto dto, Company target) {
        mapper.map(dto, target); // skips nulls
        return target;
    }
}