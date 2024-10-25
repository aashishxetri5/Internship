package amnil.tm.service.task;

import amnil.tm.dto.request.TaskRequest;
import amnil.tm.dto.response.TaskResponse;
import amnil.tm.enums.TaskStatus;
import amnil.tm.model.Task;

import java.util.List;

public interface ITaskService {

    Task saveTask(TaskRequest task);

    TaskResponse updateTask(TaskRequest task);

    List<TaskResponse> getTasks();

    Task getTaskById(Long id);

    TaskResponse updateTaskStatus(Long id, TaskStatus status);

    List<TaskResponse> getAssignedTasks(String email);

    void deleteTask(Long taskId);

    TaskResponse assignTask(Long taskId, Long userId);
}
