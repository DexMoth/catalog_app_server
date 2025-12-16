import org.catalog_app.controllers.CategoryController;
import org.catalog_app.dtos.CategoryDto;
import org.catalog_app.entities.CategoryEntity;
import org.catalog_app.services.CategoryService;
import org.catalog_app.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryTests {

    @Mock
    private CategoryService service;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CategoryController controller;

    private CategoryDto dto1;
    private CategoryDto dto2;
    private CategoryEntity ent1;
    private CategoryEntity ent2;

    @BeforeEach
    void setUp() {
        dto1 = new CategoryDto();
        dto1.setId(1L);
        dto1.setName("Category1");

        dto2 = new CategoryDto();
        dto2.setId(2L);
        dto2.setName("Category2");

        ent1 = new CategoryEntity();
        ent1.setId(1L);
        ent1.setName("Category1");

        ent2 = new CategoryEntity();
        ent2.setId(2L);
        ent2.setName("Category2");
    }

    @Test
    void testCreateAndGet() {
        when(service.create(ent1)).thenReturn(ent1);

        when(service.create(ent2)).thenReturn(ent2);

        var result1 = service.create(ent1);
        var result2 = service.create(ent2);

        assertEquals("Category1", result1.getName());
        assertEquals("Category2", result2.getName());
    }

    @Test
    void testUpdate() {
        CategoryDto updateDto = new CategoryDto();
        updateDto.setName("Category newName1");

        CategoryEntity updateEntity = new CategoryEntity();
        updateEntity.setName("Category newName1");

        CategoryEntity updatedEntity = new CategoryEntity();
        updatedEntity.setId(1L);
        updatedEntity.setName("Category newName1");

        CategoryDto updatedDto = new CategoryDto();
        updatedDto.setId(1L);
        updatedDto.setName("Category newName1");

        when(modelMapper.map(updateDto, CategoryEntity.class)).thenReturn(updateEntity);
        when(service.update(eq(1L), eq(updateEntity))).thenReturn(updatedEntity);
        when(modelMapper.map(updatedEntity, CategoryDto.class)).thenReturn(updatedDto);
        when(service.get(1L)).thenReturn(updatedEntity);
        when(modelMapper.map(updatedEntity, CategoryDto.class)).thenReturn(updatedDto);

        var updatedCat = controller.update(1L, updateDto);
        var retrievedCat = controller.get(1L);

        assertEquals("Category newName1", updatedCat.getName());
        assertEquals(1L, updatedCat.getId());
        assertEquals("Category newName1", retrievedCat.getName());

        verify(modelMapper, times(1)).map(updateDto, CategoryEntity.class);
        verify(service, times(1)).update(eq(1L), eq(updateEntity));
        verify(modelMapper, times(2)).map(updatedEntity, CategoryDto.class);
        verify(service, times(1)).get(1L);
    }

    @Test
    void testDelete() {
        when(service.delete(1L)).thenReturn(ent1);
        when(service.delete(2L)).thenReturn(ent2);
        when(modelMapper.map(ent1, CategoryDto.class)).thenReturn(dto1);
        when(modelMapper.map(ent2, CategoryDto.class)).thenReturn(dto2);

        var deletedCat1 = controller.delete(1L);
        var deletedCat2 = controller.delete(2L);

        assertEquals("Category1", deletedCat1.getName());
        assertEquals("Category2", deletedCat2.getName());

        verify(service, times(1)).delete(1L);
        verify(service, times(1)).delete(2L);
        verify(modelMapper, times(1)).map(ent1, CategoryDto.class);
        verify(modelMapper, times(1)).map(ent2, CategoryDto.class);
    }

    @Test
    void testGetAll() {
        List<CategoryEntity> entities = Arrays.asList(ent1, ent2);
        List<CategoryDto> dtos = Arrays.asList(dto1, dto2);

        when(service.getAll()).thenReturn(entities);
        when(modelMapper.map(ent1, CategoryDto.class)).thenReturn(dto1);
        when(modelMapper.map(ent2, CategoryDto.class)).thenReturn(dto2);

        var allCategories = controller.getAll();

        assertEquals(2, allCategories.size());

        verify(service, times(1)).getAll();
        verify(modelMapper, times(2)).map(any(CategoryEntity.class), eq(CategoryDto.class));
    }
}