package org.catalog_app.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.catalog_app.entities.BaseModel;
import org.catalog_app.entities.ReminderEntity;
import org.catalog_app.enums.Frequency;
import org.catalog_app.enums.UntilType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class RecurrenceRuleDto {

    private Long id;
    // частота
    private Frequency frequency;

    // интервал: каждые .. дней
    private Integer intervalValue = 1;

    // тип окончания: never, date, count
    private UntilType untilType = UntilType.never;

    private LocalDate untilDate; // дата окончания

    private Integer occurrencesCount; // кол-во повторений

    // для weekly (дни недели)
    private Boolean monday = false;
    private Boolean tuesday = false;
    private Boolean wednesday = false;
    private Boolean thursday = false;
    private Boolean friday = false;
    private Boolean saturday = false;
    private Boolean sunday = false;

    // для monthly
    private Integer monthDay;
    private Integer monthWeek;
    private String monthWeekday;

    // для yearly
    private Integer yearMonth;
    private Integer yearDay;

    // метаданные
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
