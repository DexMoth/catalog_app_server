package org.catalog_app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "reminders")
public class ReminderEntity extends BaseModel {
    private String title;
    private String description;
    private String message;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "reminder_date")
    private LocalDateTime reminderDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recurrence_rule_id")
    private RecurrenceRuleEntity recurrenceRule;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}