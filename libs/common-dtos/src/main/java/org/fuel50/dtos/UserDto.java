package org.fuel50.dtos;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "User", description = "User representation in the system")
public class UserDto {
    @Schema(description = "Unique identifier", example = "1")
    private Long id;

    @Schema(description = "Company identifier this user belongs to", example = "1")
    private Long companyId;

    @Schema(description = "User email address", example = "jane.doe@example.com")
    private String email;

    @Schema(description = "First name", example = "Jane")
    private String firstName;

    @Schema(description = "Last name", example = "Doe")
    private String lastName;

    @Schema(description = "Date and time the user joined", example = "2024-01-10T09:30:00")
    private LocalDateTime joinedAt;

    @Schema(description = "Whether the user is an administrator", example = "false")
    private Boolean isAdmin;

    @Schema(description = "Creation timestamp", example = "2024-01-01T00:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp", example = "2024-01-02T12:00:00")
    private LocalDateTime updatedAt;
}