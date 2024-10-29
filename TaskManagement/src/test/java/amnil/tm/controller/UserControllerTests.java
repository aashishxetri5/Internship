package amnil.tm.controller;


import amnil.tm.dto.response.UserResponse;
import amnil.tm.enums.Role;
import amnil.tm.service.UserDetailsServiceImpl;
import amnil.tm.service.user.IUserService;
import amnil.tm.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTests {

    private final MockMvc mockMvc;

    @MockBean
    private IUserService userService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private UserController userController;

    private String token;

    @Autowired
    public UserControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @BeforeEach
    public void setUp() {
        token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJBRE1JTiIsIlVTRVIiXSwic3ViIjoiYWtAZ21haWwuY29tIiwiaWF0IjoxNzMwMTk4MzQxLCJleHAiOjE3MzAyMDE5NDF9.-Oq6UjXj-RNxDL_7Att0-7DnBSVa3xWeYBvTd22ImAY";
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void testGetUserWithContent() throws Exception {
        List<UserResponse> userList = new ArrayList<>();
        userList.add(new UserResponse(1L, "Aashish Katwal", "ak@gmail.com", List.of(Role.ADMIN, Role.USER)));
        userList.add(new UserResponse(2L, "AK", "bk@gmail.com", List.of(Role.ADMIN, Role.USER)));

        Mockito.when(userService.getUsers()).thenReturn(userList);

        mockMvc.perform(get("/api/users")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(jsonPath("$.data[0].fullname").value("Aashish Katwal"))
                .andExpect(jsonPath("$.data[0].email").value("ak@gmail.com"))
                .andExpect(jsonPath("$.data[0].roles[0]").value("ADMIN"))
                .andExpect(jsonPath("$.data[0].roles[1]").value("USER"));
    }
}
