package org.catalog_app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.catalog_app.enums.Frequency;
import org.catalog_app.enums.UntilType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "recurrence_rules")
public class RecurrenceRuleEntity extends BaseModel{

    // частота
    @Column(name = "frequency")
    @Enumerated(EnumType.STRING)
    private Frequency frequency;

    // интервал: каждые .. дней
    @Column(name = "interval_value", nullable = false)
    private Integer intervalValue = 1;

    // тип окончания: never, date, count
    @Column(name = "until_type", nullable = false)
    @Enumerated(EnumType.STRING)
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
    @Column(length = 255)
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
