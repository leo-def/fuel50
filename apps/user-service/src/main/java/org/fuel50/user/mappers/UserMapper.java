package org.fuel50.user.mappers;

import org.fuel50.dtos.UserDto;
import org.fuel50.domains.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final ModelMapper mapper;
    public UserMapper(ModelMapper mapper) { this.mapper = mapper; }

    public UserDto toDto(User entity) { return mapper.map(entity, UserDto.class); }
    public User toEntity(UserDto dto) { return mapper.map(dto, User.class); }
    public User merge(UserDto dto, User target) { mapper.map(dto, target); return target; }
}