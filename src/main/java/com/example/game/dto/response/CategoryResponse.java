package com.example.game.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponse {
    private int categoryId;
    private String categoryName;
}
