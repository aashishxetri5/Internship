package amnil.tm.service.task;

import amnil.tm.dto.request.TaskRequest;
import amnil.tm.dto.response.TaskResponse;
import amnil.tm.enums.TaskStatus;
import amnil.tm.exception.DuplicateRecordException;
import amnil.tm.exception.TaskNotAssignedException;
import amnil.tm.exception.UnauthorizedAccessException;
import amnil.tm.model.Task;
import amnil.tm.repository.TaskRepository;
import amnil.tm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class TaskService implements ITaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Task saveTask(TaskRequest task) {
        return taskRepository.save(convertToTask(task));
    }

    private Task convertToTask(TaskRequest task) {
        return new Task(
                task.getTitle(),
                task.getDescription(),
                task.getStatus()
        );
    }

    @Override
    public TaskResponse updateTask(TaskRequest request) {
        return taskRepository.findById(request.getId())
                .map(
                        task -> {
                            task.setTitle(request.getTitle());
                            task.setDescription(request.getDescription());
                            taskRepository.save(task);

                            if (task.getAssignedUser() == null)
                                return convertToTaskResponseNotAssigned(task);

                            return convertToTaskResponse(task);
                        }
                )
                .orElseThrow(
                        () -> new NoSuchElementException("Requested task doesn't exit!!")
                );
    }

    @Override
    public List<TaskResponse> getTasks() {
        List<Task> tasks = taskRepository.findAll();

        return tasks.stream()
                .map(
                        task -> {
                            if (task.getAssignedUser() != null) {
                                return convertToTaskResponse(task);
                            } else {
                                return convertToTaskResponseNotAssigned(task);
                            }
                        }
                )
                .collect(Collectors.toList());
    }

    private TaskResponse convertToTaskResponseNotAssigned(Task task) {
        return new TaskResponse(task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                null
        );
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Task not found")
        );
    }

    @Override
    public TaskResponse updateTaskStatus(Long id, TaskStatus status) {
        return taskRepository.findById(id)
                .map(
                        task -> {
                            if (task.getAssignedUser() == null) {
                                throw new TaskNotAssignedException("Task must be assigned to user before changing status");
                            }

                            // Check who is changing the status. Only the user who is assigned the task can change status.
                            if (!task.getAssignedUser().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
                                throw new UnauthorizedAccessException("This task is not assigned to you.");
                            }

                            task.setStatus(status);
                            taskRepository.save(task);
                            return convertToTaskResponse(task);
                        }
                )
                .orElseThrow(
                        () -> new NoSuchElementException("Task not found")
                );
    }

    @Override
    public List<TaskResponse> getAssignedTasks(String email) {
        return taskRepository.findByAssignedUserEmail(email)
                .stream().map(this::convertToTaskResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    @Override
    public TaskResponse assignTask(Long taskId, Long userId) {
        return taskRepository.findById(taskId)
                .map(
                        task -> {
                            if (task.getAssignedUser() != null) {
                                throw new DuplicateRecordException("Task is already assigned to " + task.getAssignedUser().getEmail());
                            }
                            task.setAssignedUser(userRepository.getReferenceById(userId));
                            task.setAssignedOn(LocalDate.now());
                            taskRepository.save(task);

                            return convertToTaskResponse(task);

                        }
                )
                .orElseThrow(
                        () -> new NoSuchElementException("Task not found")
                );
    }

    private TaskResponse convertToTaskResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                new TaskResponse.AssignedUserResponse(
                        task.getAssignedUser().getId(),
                        task.getAssignedUser().getFullname(),
                        task.getAssignedUser().getEmail()
                )
        );
    }
}
