package amnil.tm.service;

import amnil.tm.dto.request.TaskRequest;
import amnil.tm.dto.response.TaskResponse;
import amnil.tm.enums.TaskStatus;
import amnil.tm.model.Task;
import amnil.tm.model.User;
import amnil.tm.repository.TaskRepository;
import amnil.tm.repository.UserRepository;
import amnil.tm.service.task.TaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTests {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;

    /* Save Task */

    @Test
    public void testSaveTask_success() {
        TaskRequest taskRequest = new TaskRequest("Do laundry", "Wash all your dirty clothes", TaskStatus.PENDING);

        Task mockTask = new Task();
        mockTask.setId(1L);
        mockTask.setTitle("Do laundry");
        mockTask.setDescription("Wash all your dirty clothes");
        mockTask.setStatus(TaskStatus.PENDING);
        mockTask.setAssignedUser(null);
        mockTask.setAssignedOn(null);

        Mockito.when(taskRepository.save(Mockito.any(Task.class))).thenReturn(mockTask);

        Task response = taskService.saveTask(taskRequest);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(mockTask.getId(), response.getId());
        Assertions.assertEquals(mockTask.getTitle(), response.getTitle());
        Assertions.assertEquals(mockTask.getDescription(), response.getDescription());
        Assertions.assertEquals("PENDING", response.getStatus().name());

        Assertions.assertNull(response.getAssignedUser());
        Assertions.assertNull(response.getAssignedOn());
    }

    @Test
    public void testSaveTask_Null() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            taskService.saveTask(null);
        });
    }

    /* Get Tasks */
    @Test
    public void testGetAllTasks_success() {
        Task task1 = new Task(1L, "Dashboard design", "by tomorrow", TaskStatus.PENDING,
                LocalDate.now(), new User(1L, "AK", "ak@gmail.com"));
        Task task2 = new Task(1L, "Login design", "by next week", TaskStatus.IN_PROGRESS,
                LocalDate.now(), new User(2L, "KAKA", "kaka@gmail.com"));
        List<Task> allTasks = List.of(task1, task2);

        Mockito.when(taskRepository.findAll()).thenReturn(allTasks);

        List<TaskResponse> response = taskService.getTasks();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(allTasks.size(), response.size());
        Assertions.assertEquals(allTasks.get(0).getAssignedUser().getFullname(), response.get(0).getAssignedUser().getFullname());
        Assertions.assertEquals(allTasks.get(1).getTitle(), response.get(1).getTitle());
    }

    @Test
    public void testGetAllTasks_null() {
        Mockito.when(taskRepository.findAll()).thenReturn(List.of());

        List<TaskResponse> taskResponse = taskService.getTasks();

        assertNotNull(taskResponse);
        assertEquals(0, taskResponse.size());
        assertTrue(taskResponse.isEmpty());
    }

    /* getTaskById */

    @Test
    public void testGetTaskById_success() {
        Task task = new Task(1L, "Some Task", "Task Desc", TaskStatus.PENDING, null, null);

        Mockito.when(taskRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(task));
        Task response = taskService.getTaskById(1L);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(task.getId(), response.getId());
        Assertions.assertEquals(task.getTitle(), response.getTitle());
        Assertions.assertEquals(task.getDescription(), response.getDescription());
        Assertions.assertEquals(task.getStatus(), response.getStatus());
    }

    @Test
    public void testGetTaskById_NotFound() {
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            taskService.getTaskById(100L);
        });
    }

}
