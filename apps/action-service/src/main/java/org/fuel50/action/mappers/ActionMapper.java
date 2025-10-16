package org.fuel50.action.mappers;

import org.fuel50.dtos.ActionDto;
import org.fuel50.domains.Action;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ActionMapper {
    private final ModelMapper mapper;
    public ActionMapper(ModelMapper mapper) { this.mapper = mapper; }

    public ActionDto toDto(Action entity) { return mapper.map(entity, ActionDto.class); }
    public Action toEntity(ActionDto dto) { return mapper.map(dto, Action.class); }
    public Action merge(ActionDto dto, Action target) { mapper.map(dto, target); return target; }
}