import org.catalog_app.entities.TagEntity;
import org.catalog_app.error.NotFoundException;
import org.catalog_app.repositories.TagRepository;
import org.catalog_app.services.TagService;
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
class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService;

    private TagEntity testTag;
    private final Long userId = 1L;

    @BeforeEach
    void setUp() {
        testTag = new TagEntity();
        testTag.setId(1L);
        testTag.setName("Красный");
        testTag.setCreatedAt(LocalDateTime.now());
        testTag.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void getAll() {
        List<TagEntity> expectedTags = List.of(testTag);
        when(tagRepository.findAll()).thenReturn(expectedTags);

        List<TagEntity> result = tagService.getAll(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Красный", result.get(0).getName());
    }

    @Test
    void get() {
        when(tagRepository.findById(1L)).thenReturn(Optional.of(testTag));

        TagEntity result = tagService.get(userId, 1L);

        assertNotNull(result);
        assertEquals("Красный", result.getName());
    }

    @Test
    void create() {
        when(tagRepository.save(any(TagEntity.class))).thenReturn(testTag);

        TagEntity result = tagService.create(userId, testTag);

        assertNotNull(result);
        verify(tagRepository, times(1)).save(testTag);
    }

    @Test
    void update() {
        TagEntity updateData = new TagEntity();
        updateData.setName("Синий");

        when(tagRepository.findById(1L)).thenReturn(Optional.of(testTag));
        when(tagRepository.save(any(TagEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TagEntity result = tagService.update(userId, 1L, updateData);

        assertNotNull(result);
        assertEquals("Синий", result.getName());
    }

    @Test
    void delete() {
        when(tagRepository.findById(1L)).thenReturn(Optional.of(testTag));

        TagEntity result = tagService.delete(userId, 1L);

        assertNotNull(result);
        verify(tagRepository, times(1)).delete(testTag);
    }
}
