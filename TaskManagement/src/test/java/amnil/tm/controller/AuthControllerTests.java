package amnil.tm.controller;

import amnil.tm.dto.request.AddUserRequest;
import amnil.tm.dto.request.LoginRequest;
import amnil.tm.dto.response.ApiResponse;
import amnil.tm.dto.response.UserResponse;
import amnil.tm.enums.Role;
import amnil.tm.exception.DuplicateRecordException;
import amnil.tm.filters.JwtFilter;
import amnil.tm.repository.UserRepository;
import amnil.tm.service.UserDetailsServiceImpl;
import amnil.tm.service.user.IUserService;
import amnil.tm.utils.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@WebMvcTest(AuthController.class)
public class AuthControllerTests {

    @MockBean
    private IUserService userService;

    @MockBean(name = "userDetailsServiceImpl")
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtFilter jwtFilter;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserController userController;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private AuthController authController;

    /* Signup */
    @Test
    public void testSignUp_Success() {
        try {
            AddUserRequest request = new AddUserRequest
                    ("Aashish", "akakaka@gmail.com", "asdf", List.of(Role.ADMIN));

            UserResponse user = new UserResponse
                    (1L, "Aashish", "akakaka@gmail.com", List.of(Role.ADMIN));

            Mockito.when(userService.addUser(Mockito.any(AddUserRequest.class))).thenReturn(user);

            ResponseEntity<ApiResponse> controllerResponse = authController.signup(request);

            System.out.println(controllerResponse.getBody());

            Assertions.assertEquals(HttpStatus.CREATED, controllerResponse.getStatusCode());
            Assertions.assertNotNull(controllerResponse.getBody());
            Assertions.assertEquals(user, controllerResponse.getBody().getData());


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testSignUp_Failure_DuplicateUser() throws Exception {
        AddUserRequest request = new AddUserRequest
                ("Aashish", "ak@gmail.com", "asdf", List.of(Role.ADMIN));

        Mockito.when(userService.addUser(Mockito.any(AddUserRequest.class))).thenThrow(new DuplicateRecordException("User already exists!!"));

        ResponseEntity<ApiResponse> controllerResponse = authController.signup(request);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, controllerResponse.getStatusCode());
        Assertions.assertNotNull(controllerResponse.getBody());
        Assertions.assertEquals("User already exists!!", controllerResponse.getBody().getData());
    }

    /* Login */
    @Test
    public void testLogin_Success() {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJBRE1JTiIsIlVTRVIiXSwic3ViIjoiYWtAZ21haWwuY29tIiwiaWF0IjoxNzMwMTk4MzQxLCJleHAiOjE3MzAyMDE5NDF9.-Oq6UjXj-RNxDL_7Att0-7DnBSVa3xWeYBvTd22ImAY";
        LoginRequest request = new LoginRequest("ak@gmail.com", "asdf");

        List<? extends GrantedAuthority> roles = Stream.of(Role.ADMIN, Role.USER)
                .map(Role::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        UserDetails userDetails = new User(request.getUsername(), request.getPassword(), roles);

        Mockito.when(authenticationManager.authenticate(Mockito.any()))
                .thenReturn(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));

        Mockito.when(userDetailsService.loadUserByUsername(request.getUsername())).thenReturn(userDetails);
        Mockito.when(jwtUtil.generateToken(request.getUsername(), List.of(Role.ADMIN.name(), Role.USER.name()))).thenReturn(token);

        ResponseEntity<ApiResponse> controllerResponse = authController.login(request);

        Assertions.assertEquals(HttpStatus.OK, controllerResponse.getStatusCode());
        Assertions.assertNotNull(controllerResponse.getBody());
        Assertions.assertEquals(token, controllerResponse.getBody().getData());
    }

    @Test
    public void testLogin_Failure_NullFields() {
        LoginRequest request = new LoginRequest(null, null);

        ResponseEntity<ApiResponse> controllerResponse = authController.login(request);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, controllerResponse.getStatusCode());
        Assertions.assertNotNull(controllerResponse.getBody());
    }


}
