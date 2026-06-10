import org.catalog_app.entities.CategoryEntity;
import org.catalog_app.error.NotFoundException;
import org.catalog_app.repositories.CategoryRepository;
import org.catalog_app.services.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private CategoryEntity testCategory;
    private final Long userId = 1L;

    @BeforeEach
    void setUp() {
        testCategory = new CategoryEntity();
        testCategory.setId(1L);
        testCategory.setName("Одежда");
        testCategory.setCreatedAt(LocalDateTime.now());
        testCategory.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void getAll() {
        List<CategoryEntity> expectedCategories = List.of(testCategory);
        when(categoryRepository.findAll()).thenReturn(expectedCategories);

        List<CategoryEntity> result = categoryService.getAll(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Одежда", result.get(0).getName());
    }

    @Test
    void get() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));

        CategoryEntity result = categoryService.get(userId, 1L);

        assertNotNull(result);
        assertEquals("Одежда", result.getName());
    }

    @Test
    void get_WhenNotExists() {
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                categoryService.get(userId, 999L)
        );
    }

    @Test
    void create() {
        when(categoryRepository.save(any(CategoryEntity.class))).thenReturn(testCategory);

        CategoryEntity result = categoryService.create(userId, testCategory);

        assertNotNull(result);
        verify(categoryRepository, times(1)).save(testCategory);
    }

    @Test
    void update() {
        CategoryEntity updateData = new CategoryEntity();
        updateData.setName("Обувь");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(categoryRepository.save(any(CategoryEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CategoryEntity result = categoryService.update(userId, 1L, updateData);

        assertNotNull(result);
        assertEquals("Обувь", result.getName());
    }

    @Test
    void delete() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));

        CategoryEntity result = categoryService.delete(userId, 1L);

        assertNotNull(result);
        verify(categoryRepository, times(1)).delete(testCategory);
    }
}