package org.catalog_app.entities;

import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "verification_codes")
public class VerificationCodeEntity extends BaseModel {

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private boolean used = false;

    public VerificationCodeEntity(String email, String code, int expirationMinutes) {
        this.email = email;
        this.code = code;
        this.expiresAt = LocalDateTime.now().plusMinutes(expirationMinutes);
        this.used = false;
    }

    public boolean isValid() {
        return !used && expiresAt.isAfter(LocalDateTime.now());
    }
}
