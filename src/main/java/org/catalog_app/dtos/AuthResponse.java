package org.catalog_app.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private boolean success;
    private String message;
    private Long userId;
    private String username;
    private String email;
    private String token;
}
