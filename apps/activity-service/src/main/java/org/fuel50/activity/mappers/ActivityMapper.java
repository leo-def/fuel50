package org.fuel50.activity.mappers;

import org.fuel50.dtos.ActivityDto;
import org.fuel50.domains.Activity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ActivityMapper {
    private final ModelMapper mapper;
    public ActivityMapper(ModelMapper mapper) { this.mapper = mapper; }

    public ActivityDto toDto(Activity entity) { return mapper.map(entity, ActivityDto.class); }
    public Activity toEntity(ActivityDto dto) { return mapper.map(dto, Activity.class); }
    public Activity merge(ActivityDto dto, Activity target) { mapper.map(dto, target); return target; }
}