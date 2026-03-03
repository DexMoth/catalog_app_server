package org.catalog_app.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
    @Column(name = "title", nullable = false)
    private String title;
    private String description;
    private String message;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "reminder_date")
    private LocalDateTime reminderDate;

    @Column(name = "recurrence_rule", columnDefinition = "jsonb")
    private String recurrenceRule;  // JSON строка

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}