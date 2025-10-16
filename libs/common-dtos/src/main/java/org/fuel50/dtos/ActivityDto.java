package org.fuel50.dtos;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "Activity", description = "Activity entity within a company")
public class ActivityDto {
    @Schema(description = "Unique identifier", example = "100")
    private Long id;

    @Schema(description = "Owning company identifier", example = "1")
    private Long companyId;

    @Schema(description = "Activity name", example = "Onboarding")
    private String name;

    @Schema(description = "Activity description", example = "New hire onboarding process")
    private String description;

    @Schema(description = "Identifier of user who created the activity", example = "5")
    private Long createdBy;

    @Schema(description = "Creation timestamp", example = "2024-01-01T00:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp", example = "2024-01-02T12:00:00")
    private LocalDateTime updatedAt;
}