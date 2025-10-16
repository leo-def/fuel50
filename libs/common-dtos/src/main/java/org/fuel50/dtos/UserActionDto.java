package org.fuel50.dtos;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "UserAction", description = "Record of a user performing an action")
public class UserActionDto {
    @Schema(description = "Unique identifier", example = "300")
    private Long id;

    @Schema(description = "User identifier", example = "5")
    private Long userId;

    @Schema(description = "Action identifier", example = "10")
    private Long actionId;

    @Schema(description = "Performed timestamp", example = "2024-01-15T09:45:00")
    private LocalDateTime performedAt;

    @Schema(description = "Additional metadata about the action", example = "{\"device\":\"web\"}")
    private String metadata;
}