package org.fuel50.activity.mappers;

import org.fuel50.dtos.UserActivityDto;
import org.fuel50.domains.UserActivity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserActivityMapper {
    private final ModelMapper mapper;
    public UserActivityMapper(ModelMapper mapper) { this.mapper = mapper; }

    public UserActivityDto toDto(UserActivity entity) { return mapper.map(entity, UserActivityDto.class); }
    public UserActivity toEntity(UserActivityDto dto) { return mapper.map(dto, UserActivity.class); }
    public UserActivity merge(UserActivityDto dto, UserActivity target) { mapper.map(dto, target); return target; }
}