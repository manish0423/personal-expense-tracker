package com.jwt.project.service;

import com.jwt.project.dto.CategoryDto;
import com.jwt.project.dto.CreateCategoryRequest;
import com.jwt.project.entity.Category;
import com.jwt.project.entity.User;
import com.jwt.project.mapper.CategoryMapper;
import com.jwt.project.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryDto createCategory(CreateCategoryRequest request, User user) {
        if (categoryRepository.existsByNameAndUser(request.getName(), user)) {
            throw new RuntimeException("Category name already exists for this user");
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setUser(user);

        Category saved = categoryRepository.save(category);
        return categoryMapper.toDto(saved);
    }

    public List<CategoryDto> getAllCategories(User user) {
        return categoryRepository.findByUser(user).stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    public CategoryDto getCategoryById(Long id, User user) {
        Category category = categoryRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return categoryMapper.toDto(category);
    }

    public CategoryDto updateCategory(Long id, CreateCategoryRequest request, User user) {
        Category category = categoryRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!category.getName().equals(request.getName()) &&
            categoryRepository.existsByNameAndUser(request.getName(), user)) {
            throw new RuntimeException("Category name already exists for this user");
        }

        category.setName(request.getName());
        Category saved = categoryRepository.save(category);
        return categoryMapper.toDto(saved);
    }

    public void deleteCategory(Long id, User user) {
        Category category = categoryRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        categoryRepository.delete(category);
    }
}
