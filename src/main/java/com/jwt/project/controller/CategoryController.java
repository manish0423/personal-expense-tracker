package com.jwt.project.controller;

import com.jwt.project.dto.CategoryDto;
import com.jwt.project.dto.CreateCategoryRequest;
import com.jwt.project.entity.User;
import com.jwt.project.security.UserPrincipal;
import com.jwt.project.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userPrincipal.getUser();
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CreateCategoryRequest request) {
        User user = getCurrentUser();
        CategoryDto category = categoryService.createCategory(request, user);
        return ResponseEntity.ok(category);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        User user = getCurrentUser();
        List<CategoryDto> categories = categoryService.getAllCategories(user);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        User user = getCurrentUser();
        CategoryDto category = categoryService.getCategoryById(id, user);
        return ResponseEntity.ok(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @RequestBody CreateCategoryRequest request) {
        User user = getCurrentUser();
        CategoryDto category = categoryService.updateCategory(id, request, user);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        User user = getCurrentUser();
        categoryService.deleteCategory(id, user);
        return ResponseEntity.noContent().build();
    }
}
