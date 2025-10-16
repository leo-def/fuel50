package org.fuel50.domains;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("user_activity")
public class UserActivity {
    @Id
    private Long id;
    @Column("user_id")
    private Long userId;
    @Column("activity_id")
    private Long activityId;
    @Column("joined_at")
    private LocalDateTime joinedAt;
    @Column("left_at")
    private LocalDateTime leftAt;
    private String role;
}