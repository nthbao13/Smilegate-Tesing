package com.example.game.service.impl;

import com.example.game.dto.CategoryDTO;
import com.example.game.entity.Category;
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
    public List<CategoryDTO> findAll() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(category -> CategoryDTO.builder()
                        .categoryId(category.getCategoryId())
                        .categoryName(category.getCategoryName())
                        .build())
                .toList();

        return categoryDTOS;
    }
}
