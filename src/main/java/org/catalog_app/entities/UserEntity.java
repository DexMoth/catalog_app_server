package org.catalog_app.entities;

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
@Table(name = "users")
public class UserEntity extends BaseModel{
    private String googleId;
    private String email;
    private String name;
    private String avatarUrl;
    private LocalDateTime createdAt = LocalDateTime.now();
}
