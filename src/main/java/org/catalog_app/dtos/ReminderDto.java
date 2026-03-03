package org.catalog_app.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReminderDto {
    private String title;
    private String description;
    private String message;

    private Long itemId;
    private Long userId;

    private LocalDateTime reminderDate;
    private String recurrenceRule;  // JSON строка
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
