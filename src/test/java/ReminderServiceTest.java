import org.catalog_app.entities.RecurrenceRuleEntity;
import org.catalog_app.entities.ReminderEntity;
import org.catalog_app.error.NotFoundException;
import org.catalog_app.repositories.RecurrenceRuleRepository;
import org.catalog_app.repositories.ReminderRepository;
import org.catalog_app.services.RecurrenceRuleService;
import org.catalog_app.services.ReminderService;
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
class ReminderServiceTest {

    @Mock
    private ReminderRepository reminderRepository;

    @Mock
    private RecurrenceRuleRepository ruleRepository;

    @Mock
    private RecurrenceRuleService ruleService;

    @InjectMocks
    private ReminderService reminderService;

    private ReminderEntity testReminder;

    @BeforeEach
    void setUp() {
        testReminder = new ReminderEntity();
        testReminder.setId(1L);
        testReminder.setTitle("Напомнить");
        testReminder.setDescription("Описание напоминания");
        testReminder.setMessage("Сообщение");
        testReminder.setReminderDate(LocalDateTime.now().plusDays(1));
        testReminder.setIsActive(true);
        testReminder.setItemId(10L);
        testReminder.setCreatedAt(LocalDateTime.now());
        testReminder.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void getAll() {
        List<ReminderEntity> expectedReminders = List.of(testReminder);
        when(reminderRepository.findAll()).thenReturn(expectedReminders);

        List<ReminderEntity> result = reminderService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Напомнить", result.get(0).getTitle());
    }

    @Test
    void get_WhenExists() {
        when(reminderRepository.findById(1L)).thenReturn(Optional.of(testReminder));

        ReminderEntity result = reminderService.get(1L);

        assertNotNull(result);
        assertEquals("Напомнить", result.getTitle());
    }

    @Test
    void get_WhenNotExists() {
        when(reminderRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                reminderService.get(999L)
        );
    }

    @Test
    void create() {
        when(reminderRepository.save(any(ReminderEntity.class))).thenReturn(testReminder);

        ReminderEntity result = reminderService.create(testReminder);

        assertNotNull(result);
        verify(reminderRepository, times(1)).save(testReminder);
    }

    @Test
    void update() {
        ReminderEntity updateData = new ReminderEntity();
        updateData.setTitle("Новое название");
        updateData.setDescription("Новое описание");
        updateData.setIsActive(false);

        when(reminderRepository.findById(1L)).thenReturn(Optional.of(testReminder));
        when(reminderRepository.save(any(ReminderEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReminderEntity result = reminderService.update(1L, updateData);

        assertNotNull(result);
        assertEquals("Новое название", result.getTitle());
        assertEquals("Новое описание", result.getDescription());
        assertFalse(result.getIsActive());
    }

    @Test
    void updateActive_ToggleStatus() {
        when(reminderRepository.findById(1L)).thenReturn(Optional.of(testReminder));
        when(reminderRepository.save(any(ReminderEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReminderEntity result = reminderService.updateActive(1L, false);

        assertNotNull(result);
        assertFalse(result.getIsActive());
    }

    @Test
    void delete() {
        when(reminderRepository.findById(1L)).thenReturn(Optional.of(testReminder));

        ReminderEntity result = reminderService.delete(1L);

        assertNotNull(result);
        verify(reminderRepository, times(1)).delete(testReminder);
    }
}