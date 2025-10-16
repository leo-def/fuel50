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
@Table("rating")
public class Rating {
    @Id
    private Long id;
    @Column("from_user_id")
    private Long fromUserId;
    @Column("to_user_activity_id")
    private Long toUserActivityId;
    private Byte score;
    private String comment;
    @Column("created_at")
    private LocalDateTime createdAt;
}