import org.catalog_app.entities.ItemEntity;
import org.catalog_app.error.NotFoundException;
import org.catalog_app.repositories.ItemRepository;
import org.catalog_app.services.ItemService;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    private ItemEntity testItem;
    private final Long userId = 1L;

    @BeforeEach
    void setUp() {
        testItem = new ItemEntity();
        testItem.setId(1L);
        testItem.setName("Тестовая вещь");
        testItem.setDescription("Тестовое описание");
        testItem.setEmbedding("0.123,0.456,0.789");
        testItem.setCreatedAt(LocalDateTime.now());
        testItem.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void getAll() {
        List<ItemEntity> expectedItems = List.of(testItem);
        when(itemRepository.findByUserId(userId)).thenReturn(expectedItems);

        List<ItemEntity> result = itemService.getAll(userId, null, null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Тестовая вещь", result.get(0).getName());
        verify(itemRepository, times(1)).findByUserId(userId);
    }

    @Test
    void getAll_FilterByCategory() {
        Long categoryId = 10L;
        when(itemRepository.findByCategory(userId, categoryId)).thenReturn(List.of(testItem));

        List<ItemEntity> result = itemService.getAll(userId, categoryId, null, null);

        assertNotNull(result);
        verify(itemRepository, times(1)).findByCategory(userId, categoryId);
    }

    @Test
    void getAll_FilterByTag() {
        Long tagId = 20L;
        when(itemRepository.findByTag(userId, tagId)).thenReturn(List.of(testItem));

        List<ItemEntity> result = itemService.getAll(userId, null, tagId, null);

        assertNotNull(result);
        verify(itemRepository, times(1)).findByTag(userId, tagId);
    }

    @Test
    void getAll_SearchByText() {
        String search = "тест";
        when(itemRepository.findByText(userId, search)).thenReturn(List.of(testItem));

        List<ItemEntity> result = itemService.getAll(userId, null, null, search);

        assertNotNull(result);
        verify(itemRepository, times(1)).findByText(userId, search);
    }

    @Test
    void getAllWithoutParent() {
        List<ItemEntity> expectedItems = List.of(testItem);
        when(itemRepository.findByParentIsNullAndUserId(userId)).thenReturn(expectedItems);

        List<ItemEntity> result = itemService.getAllWithoutParent(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(itemRepository, times(1)).findByParentIsNullAndUserId(userId);
    }

    @Test
    void get_WhenExists() {
        when(itemRepository.findByUserIdAndId(userId, 1L)).thenReturn(Optional.of(testItem));

        ItemEntity result = itemService.get(userId, 1L);

        assertNotNull(result);
        assertEquals("Тестовая вещь", result.getName());
    }

    @Test
    void get_WhenNotExists() {
        when(itemRepository.findByUserIdAndId(userId, 999L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                itemService.get(userId, 999L)
        );
    }

    @Test
    void findChildren() {
        ItemEntity childItem = new ItemEntity();
        childItem.setId(2L);
        childItem.setName("Дочерняя вещь");
        childItem.setParent(testItem);

        when(itemRepository.findChildren(userId, 1L)).thenReturn(List.of(childItem));

        List<ItemEntity> result = itemService.findChildren(userId, 1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Дочерняя вещь", result.get(0).getName());
    }

    @Test
    void create() {
        when(itemRepository.save(any(ItemEntity.class))).thenReturn(testItem);

        ItemEntity result = itemService.create(userId, testItem);

        assertNotNull(result);
        verify(itemRepository, times(1)).save(testItem);
    }

    @Test
    void update() {
        ItemEntity updateData = new ItemEntity();
        updateData.setName("Обновлённое название");
        updateData.setDescription("Новое описание");
        updateData.setEmbedding("0.999,0.888,0.777");

        when(itemRepository.findById(1L)).thenReturn(Optional.of(testItem));
        when(itemRepository.save(any(ItemEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ItemEntity result = itemService.update(userId, 1L, updateData);

        assertNotNull(result);
        assertEquals("Обновлённое название", result.getName());
        assertEquals("Новое описание", result.getDescription());
        verify(itemRepository, times(1)).save(any(ItemEntity.class));
    }
}