package org.fuel50.dtos;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "UserActivity", description = "Link between a user and an activity")
public class UserActivityDto {
    @Schema(description = "Unique identifier", example = "200")
    private Long id;

    @Schema(description = "User identifier", example = "5")
    private Long userId;

    @Schema(description = "Activity identifier", example = "100")
    private Long activityId;

    @Schema(description = "Date/time when user joined the activity", example = "2024-01-01T08:00:00")
    private LocalDateTime joinedAt;

    @Schema(description = "Date/time when user left the activity", example = "2024-02-01T17:00:00")
    private LocalDateTime leftAt;

    @Schema(description = "User role within the activity", example = "participant")
    private String role;
}