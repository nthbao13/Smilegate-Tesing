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
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryResponse> getFullCategories() {
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
        Category category = this.getCategoryById(request.getCategoryId());
        return true;
    }

    @Override
    public Category getCategoryById(int categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (!category.isPresent() || category.get().getCategoryId() < 0) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }
        return category.get();
    }

}
