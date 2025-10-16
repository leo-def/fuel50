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
@Table("user_action")
public class UserAction {
    @Id
    private Long id;
    @Column("user_id")
    private Long userId;
    @Column("action_id")
    private Long actionId;
    @Column("performed_at")
    private LocalDateTime performedAt;
    private String metadata; // store JSON as string
}