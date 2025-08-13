package com.example.game.service;

import com.example.game.dto.response.CategoryResponse;
import com.example.game.entity.Category;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getFullCategories();
    Category getCategoryById(int categoryId);
}
