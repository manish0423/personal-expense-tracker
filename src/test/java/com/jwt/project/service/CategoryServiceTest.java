package com.jwt.project.service;

import com.jwt.project.dto.CategoryDto;
import com.jwt.project.dto.CreateCategoryRequest;
import com.jwt.project.entity.Category;
import com.jwt.project.entity.User;
import com.jwt.project.mapper.CategoryMapper;
import com.jwt.project.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    private User user;
    private Category category;
    private CategoryDto categoryDto;
    private CreateCategoryRequest createRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        category = new Category();
        category.setId(1L);
        category.setName("Food");
        category.setUser(user);

        categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("Food");

        createRequest = new CreateCategoryRequest("Food");
    }

    @Test
    void createCategory_Success() {
        // Arrange
        when(categoryRepository.existsByNameAndUser(anyString(), any(User.class))).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryMapper.toDto(any(Category.class))).thenReturn(categoryDto);

        // Act
        CategoryDto result = categoryService.createCategory(createRequest, user);

        // Assert
        assertNotNull(result);
        assertEquals("Food", result.getName());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void createCategory_NameExists_ThrowsException() {
        // Arrange
        when(categoryRepository.existsByNameAndUser("Food", user)).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> categoryService.createCategory(createRequest, user));
        assertEquals("Category name already exists for this user", exception.getMessage());
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void getAllCategories_Success() {
        // Arrange
        List<Category> categories = Arrays.asList(category);
        when(categoryRepository.findByUser(user)).thenReturn(categories);
        when(categoryMapper.toDto(any(Category.class))).thenReturn(categoryDto);

        // Act
        List<CategoryDto> result = categoryService.getAllCategories(user);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Food", result.get(0).getName());
    }

    @Test
    void getCategoryById_Success() {
        // Arrange
        when(categoryRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        // Act
        CategoryDto result = categoryService.getCategoryById(1L, user);

        // Assert
        assertNotNull(result);
        assertEquals("Food", result.getName());
    }

    @Test
    void getCategoryById_NotFound_ThrowsException() {
        // Arrange
        when(categoryRepository.findByIdAndUser(1L, user)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> categoryService.getCategoryById(1L, user));
        assertEquals("Category not found", exception.getMessage());
    }

    @Test
    void updateCategory_Success() {
        // Arrange
        CreateCategoryRequest updateRequest = new CreateCategoryRequest("Groceries");
        when(categoryRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(category));
        when(categoryRepository.existsByNameAndUser("Groceries", user)).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryMapper.toDto(any(Category.class))).thenReturn(categoryDto);

        // Act
        CategoryDto result = categoryService.updateCategory(1L, updateRequest, user);

        // Assert
        assertNotNull(result);
        verify(categoryRepository).save(category);
    }

    @Test
    void deleteCategory_Success() {
        // Arrange
        when(categoryRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(category));

        // Act
        categoryService.deleteCategory(1L, user);

        // Assert
        verify(categoryRepository).delete(category);
    }
}
