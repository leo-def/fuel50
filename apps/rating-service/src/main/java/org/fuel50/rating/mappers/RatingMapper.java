package org.fuel50.rating.mappers;

import org.fuel50.dtos.RatingDto;
import org.fuel50.domains.Rating;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RatingMapper {
    private final ModelMapper mapper;
    public RatingMapper(ModelMapper mapper) { this.mapper = mapper; }

    public RatingDto toDto(Rating entity) { return mapper.map(entity, RatingDto.class); }
    public Rating toEntity(RatingDto dto) { return mapper.map(dto, Rating.class); }
    public Rating merge(RatingDto dto, Rating target) { mapper.map(dto, target); return target; }
}