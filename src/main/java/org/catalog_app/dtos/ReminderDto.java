package org.catalog_app.dtos;

import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.catalog_app.entities.RecurrenceRuleEntity;

import javax.persistence.JoinColumn;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReminderDto {
    private Long id;
    private String title;
    private String description;
    private String message;

    private Long itemId;
    private Long userId;

    private LocalDateTime reminderDate;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long recurrenceRuleId;
}
