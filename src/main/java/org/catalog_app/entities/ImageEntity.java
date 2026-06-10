package org.catalog_app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "images")
public class ImageEntity extends BaseModel{
    private String url;
    private Boolean isMain = false;
    private LocalDateTime uploadedAt = LocalDateTime.now();
    @Column(name = "item_id", nullable = false)
    private Long itemId;
    @Column(name = "user_id", nullable = false)
    private Long userId;
}
