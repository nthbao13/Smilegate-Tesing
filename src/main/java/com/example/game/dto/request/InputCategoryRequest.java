package com.example.game.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InputCategoryRequest {
    private int categoryId;
    private String categoryName;
}
