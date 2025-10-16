package org.fuel50.dtos;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "Rating", description = "User rating for a user activity")
public class RatingDto {
    @Schema(description = "Unique identifier", example = "400")
    private Long id;

    @Schema(description = "Identifier of the user who gave the rating", example = "5")
    private Long fromUserId;

    @Schema(description = "Identifier of the target user-activity link", example = "200")
    private Long toUserActivityId;

    @Schema(description = "Rating score value", example = "5")
    private Byte score;

    @Schema(description = "Optional comment for the rating", example = "Great collaboration!")
    private String comment;

    @Schema(description = "Creation timestamp", example = "2024-01-20T14:00:00")
    private LocalDateTime createdAt;
}