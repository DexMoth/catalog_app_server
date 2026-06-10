package org.catalog_app.dtos;

import lombok.Data;

@Data
public class VerifyCodeRequest {
    private String email;
    private String code;
}