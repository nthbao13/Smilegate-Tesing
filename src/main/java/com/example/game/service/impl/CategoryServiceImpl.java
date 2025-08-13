package com.example.game.service.impl;

import com.example.game.dto.response.CategoryResponse;
import com.example.game.entity.Category;
import com.example.game.exception.CustomException;
import com.example.game.exception.ErrorCode;
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

        return categories.stream()
                .map(category -> CategoryResponse.builder()
                        .categoryId(category.getCategoryId())
                        .categoryName(category.getCategoryName())
                        .build())
                .toList();
    }

    @Override
    public Category getCategoryById(int categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty() || category.get().getCategoryId() < 0) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }
        return category.get();
    }

}
