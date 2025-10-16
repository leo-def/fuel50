package org.fuel50.dtos;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "Company", description = "Company entity details")
public class CompanyDto {
    @Schema(description = "Unique identifier", example = "1")
    private Long id;

    @Schema(description = "Company name", example = "Fuel50")
    private String name;

    @Schema(description = "Company domain", example = "fuel50.com")
    private String domain;

    @Schema(description = "Creation timestamp", example = "2024-01-01T00:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp", example = "2024-01-02T12:00:00")
    private LocalDateTime updatedAt;
}