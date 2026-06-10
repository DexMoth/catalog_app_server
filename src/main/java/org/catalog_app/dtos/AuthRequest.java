package org.catalog_app.dtos;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}
