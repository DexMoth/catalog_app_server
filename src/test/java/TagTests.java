import org.catalog_app.controllers.TagController;
import org.catalog_app.dtos.TagDto;
import org.catalog_app.entities.TagEntity;
import org.catalog_app.repositories.TagRepository;
import org.catalog_app.services.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TagTests {

    @Mock
    private TagService service;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TagController controller;

    private TagDto dto1;
    private TagDto dto2;
    private TagEntity ent1;
    private TagEntity ent2;

    @BeforeEach
    void setUp() {
        dto1 = new TagDto();
        dto1.setId(1L);
        dto1.setName("Tag1");

        dto2 = new TagDto();
        dto2.setId(2L);
        dto2.setName("Tag2");

        ent1 = new TagEntity();
        ent1.setId(1L);
        ent1.setName("Tag1");

        ent2 = new TagEntity();
        ent2.setId(2L);
        ent2.setName("Tag2");
    }

    @Test
    void testCreateAndGet() {
        when(service.create(ent1)).thenReturn(ent1);

        when(service.create(ent2)).thenReturn(ent2);

        var result1 = service.create(ent1);
        var result2 = service.create(ent2);

        assertEquals("Tag1", result1.getName());
        assertEquals("Tag2", result2.getName());
    }

    @Test
    void testUpdate() {
        TagDto updateDto = new TagDto();
        updateDto.setName("Tag newName1");

        TagEntity updateEntity = new TagEntity();
        updateEntity.setName("Tag newName1");

        TagEntity updatedEntity = new TagEntity();
        updatedEntity.setId(1L);
        updatedEntity.setName("Tag newName1");

        TagDto updatedDto = new TagDto();
        updatedDto.setId(1L);
        updatedDto.setName("Tag newName1");

        when(modelMapper.map(updateDto, TagEntity.class)).thenReturn(updateEntity);
        when(service.update(eq(1L), eq(updateEntity))).thenReturn(updatedEntity);
        when(modelMapper.map(updatedEntity, TagDto.class)).thenReturn(updatedDto);
        when(service.get(1L)).thenReturn(updatedEntity);
        when(modelMapper.map(updatedEntity, TagDto.class)).thenReturn(updatedDto);

        var updatedCat = controller.update(1L, updateDto);
        var retrievedCat = controller.get(1L);

        assertEquals("Tag newName1", updatedCat.getName());
        assertEquals(1L, updatedCat.getId());
        assertEquals("Tag newName1", retrievedCat.getName());

        verify(modelMapper, times(1)).map(updateDto, TagEntity.class);
        verify(service, times(1)).update(eq(1L), eq(updateEntity));
        verify(modelMapper, times(2)).map(updatedEntity, TagDto.class);
        verify(service, times(1)).get(1L);
    }

    @Test
    void testDelete() {
        when(service.delete(1L)).thenReturn(ent1);
        when(service.delete(2L)).thenReturn(ent2);
        when(modelMapper.map(ent1, TagDto.class)).thenReturn(dto1);
        when(modelMapper.map(ent2, TagDto.class)).thenReturn(dto2);

        var deletedCat1 = controller.delete(1L);
        var deletedCat2 = controller.delete(2L);

        assertEquals("Tag1", deletedCat1.getName());
        assertEquals("Tag2", deletedCat2.getName());

        verify(service, times(1)).delete(1L);
        verify(service, times(1)).delete(2L);
        verify(modelMapper, times(1)).map(ent1, TagDto.class);
        verify(modelMapper, times(1)).map(ent2, TagDto.class);
    }

    @Test
    void testGetAll() {
        List<TagEntity> entities = Arrays.asList(ent1, ent2);
        List<TagDto> dtos = Arrays.asList(dto1, dto2);

        when(service.getAll()).thenReturn(entities);
        when(modelMapper.map(ent1, TagDto.class)).thenReturn(dto1);
        when(modelMapper.map(ent2, TagDto.class)).thenReturn(dto2);

        var allCategories = controller.getAll();

        assertEquals(2, allCategories.size());

        verify(service, times(1)).getAll();
        verify(modelMapper, times(2)).map(any(TagEntity.class), eq(TagDto.class));
    }
}