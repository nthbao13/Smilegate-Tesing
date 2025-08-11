package com.example.game.service.impl;

import com.example.game.dto.request.InputCategoryRequest;
import com.example.game.dto.response.CategoryResponse;
import com.example.game.entity.Category;
import com.example.game.exception.CustomException;
import com.example.game.exception.ErrorCode;
import com.example.game.exception.InputException;
import com.example.game.repository.CategoryRepository;
import com.example.game.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryResponse> findAll() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponse> categoryResponses = categories.stream()
                .map(category -> CategoryResponse.builder()
                        .categoryId(category.getCategoryId())
                        .categoryName(category.getCategoryName())
                        .build())
                .toList();

        return categoryResponses;
    }

    @Override
    public boolean validateInputCategory(InputCategoryRequest request) {
        if (request.getCategoryId() == 0) {
            Category category = categoryRepository.findByCategoryName(request.getCategoryName());
            if (request.getCategoryName().trim().isEmpty()) {
                throw new InputException(ErrorCode.INVALID_CATEGORY_NAME);
            }

            if (category != null && category.getCategoryId() > 0) {
                throw new InputException(ErrorCode.GAME_CATEGORY_EXISTS);
            }
        }

        return true;
    }

    @Override
    public Category findByCategoryName(String name) {
        Category category = categoryRepository.findByCategoryName(name);
        if (category == null) {
            throw new CustomException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
        return category;
    }
}
