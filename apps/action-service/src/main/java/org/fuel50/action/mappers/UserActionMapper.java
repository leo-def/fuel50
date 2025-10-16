package org.fuel50.action.mappers;

import org.fuel50.dtos.UserActionDto;
import org.fuel50.domains.UserAction;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserActionMapper {
    private final ModelMapper mapper;
    public UserActionMapper(ModelMapper mapper) { this.mapper = mapper; }

    public UserActionDto toDto(UserAction entity) { return mapper.map(entity, UserActionDto.class); }
    public UserAction toEntity(UserActionDto dto) { return mapper.map(dto, UserAction.class); }
    public UserAction merge(UserActionDto dto, UserAction target) { mapper.map(dto, target); return target; }
}