package amnil.tm.controller;

import amnil.tm.dto.response.TaskResponse;
import amnil.tm.enums.TaskStatus;
import amnil.tm.model.Task;
import amnil.tm.service.UserDetailsServiceImpl;
import amnil.tm.service.task.ITaskService;
import amnil.tm.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
public class TaskControllerTests {

    private final MockMvc mockMvc;

    @MockBean
    private ITaskService taskService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private TaskController taskController;

    @Autowired
    public TaskControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    private String token;

    @BeforeEach
    public void setup() {
        token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJBRE1JTiIsIlVTRVIiXSwic3ViIjoiYWtAZ21haWwuY29tIiwiaWF0IjoxNzMwMjAyNTQyLCJleHAiOjE3MzAyMDYxNDJ9.6i_Iod6fDLXAqPEG9fv5U4-TYjG7peUwgPEOyEeD5ps";
    }

    /*Task List*/
    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testTasks_Success_DataFound() throws Exception {
        TaskResponse task1 = new TaskResponse(1L, "Design", "Complete by tomorrow", TaskStatus.PENDING, null);
        TaskResponse task2 = new TaskResponse
                (2L, "Development", "Complete by evening", TaskStatus.PENDING,
                        new TaskResponse.AssignedUserResponse(1L, "AK", "kaka@gmail.com")
                );
        List<TaskResponse> allTasks = List.of(task1, task2);
        Mockito.when(taskService.getTasks()).thenReturn(allTasks);

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(jsonPath("$.data[0].title").value("Design"))
                .andExpect(jsonPath("$.data[1].title").value("Development"));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testTasks_Success_DataNotFound() throws Exception {
        Mockito.when(taskService.getTasks()).thenReturn(List.of());

        mockMvc.perform(get("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()));

    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testTasks_Failure_ThrowException() throws Exception {
        Mockito.when(taskService.getTasks()).thenThrow(new RuntimeException("Something went wrong"));

        mockMvc.perform(get("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()));

    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testTasks_Failure_Unauthorized() throws Exception {
        Mockito.when(taskService.getTasks()).thenThrow(new RuntimeException("Something went wrong"));

        mockMvc.perform(get("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isInternalServerError())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()));
    }

    /* Save Task */
//    @Test
//    @WithMockUser(authorities = {"ADMIN", "USER"})
//    public void testNewTask_Success() throws Exception {
//        TaskRequest request = new TaskRequest("Design", "complete dashboard", TaskStatus.PENDING);
//
//        Mockito.when(taskService.saveTask(Mockito.any(TaskRequest.class)))
//                .thenReturn(new Task(1L, "Design", "complete dashboard", TaskStatus.PENDING, null, null));
//
//        mockMvc.perform(post("/api/tasks/new")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(request))
//                        .header("Authorization", token)
//                )
//                .andExpect(status().isCreated())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
//                .andExpect(jsonPath("$.data.title").value("Design"))
//                .andExpect(jsonPath("$.data.description").value("complete dashboard"))
//                .andExpect(jsonPath("$.data.status").value("PENDING"))
//                .andExpect(jsonPath("$.data.assignedUser").value("null"))
//                .andExpect(jsonPath("$.data.assignedOn").value("null"));
//    }


    /* Get task by id */
    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testGetTask_Success() throws Exception {

        Mockito.when(taskService.getTaskById(Mockito.anyLong()))
                .thenReturn(new Task(1L, "Design", "complete dashboard", TaskStatus.PENDING, null, null));

        mockMvc.perform(get("/api/tasks/1")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(jsonPath("$.data.title").value("Design"))
                .andExpect(jsonPath("$.data.status").value(TaskStatus.PENDING.toString()));
    }
}
