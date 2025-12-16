import org.catalog_app.controllers.ItemController;
import org.catalog_app.dtos.CategoryDto;
import org.catalog_app.dtos.ItemDto;
import org.catalog_app.dtos.TagDto;
import org.catalog_app.entities.CategoryEntity;
import org.catalog_app.entities.ItemEntity;
import org.catalog_app.services.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemTests {

    @Mock
    private ItemService service;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ItemController controller;

    private ItemDto dto1;
    private ItemDto dto2;
    private ItemEntity ent1;
    private ItemEntity ent2;

    @BeforeEach
    void setUp() {
        var cat1 = new CategoryDto();

        dto1 = new ItemDto();
        dto1.setId(1L);
        dto1.setName("Item1");

        dto2 = new ItemDto();
        dto2.setId(2L);
        dto2.setName("Item2");

        ent1 = new ItemEntity();
        ent1.setId(1L);
        ent1.setName("Item1");

        ent2 = new ItemEntity();
        ent2.setId(2L);
        ent2.setName("Item2");
    }

    @Test
    void testCreateAndGet() {
        when(service.create(ent1)).thenReturn(ent1);

        when(service.create(ent2)).thenReturn(ent2);

        var result1 = service.create(ent1);
        var result2 = service.create(ent2);

        assertEquals("Item1", result1.getName());
        assertEquals("Item2", result2.getName());
    }

    @Test
    void testUpdate() {
        ItemDto updateDto = new ItemDto();
        updateDto.setName("Item newName1");

        ItemEntity updateEntity = new ItemEntity();
        updateEntity.setName("Item newName1");

        ItemEntity updatedEntity = new ItemEntity();
        updatedEntity.setId(1L);
        updatedEntity.setName("Item newName1");

        ItemDto updatedDto = new ItemDto();
        updatedDto.setId(1L);
        updatedDto.setName("Item newName1");

        when(service.create(updateEntity)).thenReturn(updateEntity);
        when(service.update(anyLong(), any(ItemEntity.class))).thenReturn(updatedEntity);
        when(service.get(1L)).thenReturn(updatedEntity);

        var updateCat = service.create(updateEntity);
        updateCat = service.update(1L, updatedEntity);
        var retrievedCat = controller.get(1L);

        assertEquals("Item newName1", updateCat.getName());
        assertEquals(1L, updateCat.getId());
        assertEquals("Item newName1", retrievedCat.getName());
    }

    @Test
    void testDelete() {
        when(service.delete(1L)).thenReturn(ent1);
        when(service.delete(2L)).thenReturn(ent2);

        var deletedCat1 = controller.delete(1L);
        var deletedCat2 = controller.delete(2L);

        assertEquals("Item1", deletedCat1.getName());
        assertEquals("Item2", deletedCat2.getName());

        verify(service, times(1)).delete(1L);
        verify(service, times(1)).delete(2L);
    }

    @Test
    void testGetAll() {
        List<ItemEntity> entities = Arrays.asList(ent1, ent2);
        List<ItemDto> dtos = Arrays.asList(dto1, dto2);

        when(service.getAll(null, null, null)).thenReturn(entities);

        var allCategories = controller.getAll(null, null, null);

        assertEquals(2, allCategories.size());

    }

    @Test
    void testGetWithText() {
        List<ItemEntity> entities = Arrays.asList(ent1, ent2);
        List<ItemDto> dtos = Arrays.asList(dto1, dto2);

        when(service.getAll(null, null, "item1")).thenReturn(Arrays.asList(ent1));

        var allCategories = controller.getAll(null, null, "item1");

        assertEquals(1, allCategories.size());
    }
}