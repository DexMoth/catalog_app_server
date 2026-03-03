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
public class ImageDto {
    private String url;
    private Boolean isMain = false;
    private LocalDateTime uploadedAt;
    private Long itemId;
    private Long userId;
}
