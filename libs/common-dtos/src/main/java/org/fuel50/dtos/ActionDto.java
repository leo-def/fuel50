package org.fuel50.dtos;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "Action", description = "Action entity within an activity")
public class ActionDto {
    @Schema(description = "Unique identifier", example = "10")
    private Long id;

    @Schema(description = "Owning company identifier", example = "1")
    private Long companyId;

    @Schema(description = "Parent activity identifier", example = "100")
    private Long activityId;

    @Schema(description = "Action name", example = "Draft proposal")
    private String name;

    @Schema(description = "Action description", example = "Prepare project proposal draft")
    private String description;

    @Schema(description = "Identifier of user who created the action", example = "5")
    private Long createdBy;

    @Schema(description = "Creation timestamp", example = "2024-01-01T00:00:00")
    private LocalDateTime createdAt;
}