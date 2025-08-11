package com.example.game.service;

import com.example.game.dto.request.InputCategoryRequest;
import com.example.game.dto.response.CategoryResponse;
import com.example.game.entity.Category;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getFullCategories();
    boolean validateInputCategory(InputCategoryRequest request);
    Category getCategoryById(int categoryId);
}
