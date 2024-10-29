package amnil.tm.controller;

import amnil.tm.dto.request.TaskRequest;
import amnil.tm.dto.response.ApiResponse;
import amnil.tm.dto.response.TaskResponse;
import amnil.tm.enums.TaskStatus;
import amnil.tm.model.Task;
import amnil.tm.service.task.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("${api.prefix}/tasks")
public class TaskController {

    private final ITaskService taskService;

    @Autowired
    public TaskController(ITaskService taskService) {
        this.taskService = taskService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse> tasks() {
        try {
            List<TaskResponse> allTasks = taskService.getTasks();

            if (allTasks.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ApiResponse("No Tasks Found!!", HttpStatus.NO_CONTENT));
            }

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ApiResponse("Tasks Fetched Successfully", allTasks));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Something went wrong", e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/new")
    public ResponseEntity<ApiResponse> newTask(@RequestBody TaskRequest request) {
        try {
            Task task = taskService.saveTask(request);

            if (task == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Something went wrong", HttpStatus.BAD_REQUEST));
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Task Created Successfully", task));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Something went wrong", e.getMessage()));
        }
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<ApiResponse> getTask(@PathVariable Long taskId) {
        try {
            Task task = taskService.getTaskById(taskId);

            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Task", task));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Task Not Found", HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Something went wrong", e.getMessage()));
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/assigned")
    public ResponseEntity<ApiResponse> getAssignedTasks() {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            List<TaskResponse> allTasks = taskService.getAssignedTasks(email);

            if (allTasks.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse("No tasks have been assigned to " + email, "NOTHING FOUND"));
            }

            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Assigned Tasks", allTasks));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Something went wrong", e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{taskId}")
    public ResponseEntity<ApiResponse> deleteTask(@PathVariable Long taskId) {
        try {
            taskService.deleteTask(taskId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Something went wrong", e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/assign/{taskId}/{userId}")
    public ResponseEntity<ApiResponse> assignTask(@PathVariable Long taskId, @PathVariable Long userId) {
        try {
            TaskResponse task = taskService.assignTask(taskId, userId);

            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Task Assigned Successfully", task));

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Task Not Found", HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Something went wrong", e.getMessage()));
        }
    }

    @PutMapping("/update/{taskId}")
    public ResponseEntity<ApiResponse> updateTaskStatus(@PathVariable Long taskId,
                                                        @RequestBody Map<String, String> status) {
        try {
            TaskResponse task = taskService.updateTaskStatus(taskId, TaskStatus.valueOf(status.get("status")));

            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Task Status changed successfully", task));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new ApiResponse("Something went wrong", e.getMessage()));
        }

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateTask(@RequestBody TaskRequest request) {
        try {
            TaskResponse task = taskService.updateTask(request);

            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Task Updated Successfully", task));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Something went wrong", e.getMessage()));
        }
    }
}
