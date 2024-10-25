package amnil.tm.service;

import amnil.tm.dto.request.AddUserRequest;
import amnil.tm.dto.response.UserResponse;
import amnil.tm.enums.Role;
import amnil.tm.exception.DuplicateRecordException;
import amnil.tm.model.User;
import amnil.tm.repository.UserRepository;
import amnil.tm.service.user.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    /* Save User */
    @Test
    public void testSaveUser() {
        AddUserRequest request = new AddUserRequest
                ("AKAK", "kaku@gmail.com", "1234", List.of(Role.USER));

        User mockUser = new User();

        mockUser.setFullname("AKAK");
        mockUser.setEmail("kaku@gmail.com");
        mockUser.setRoles(List.of(Role.USER));

        Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(mockUser);

        UserResponse response = userService.addUser(request);

        assertNotNull(response);
        assertEquals("AKAK", response.getFullname());
        assertEquals("kaku@gmail.com", response.getEmail());
        assertEquals(List.of(Role.USER), response.getRoles());
    }


    //    Test for existing user
    @Test
    public void testSaveUser_ExistingUser() {
        AddUserRequest request = new AddUserRequest
                ("AKAK", "ak@gmail.com", "1234", List.of(Role.USER));

        User mockUser = new User();

        mockUser.setFullname("AKAK");
        mockUser.setEmail("ak@gmail.com");
        mockUser.setRoles(List.of(Role.USER));

        Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(true);

        Assertions.assertThrows(DuplicateRecordException.class, () -> {
            userService.addUser(request);
        });
    }

    //    Null User
    @Test
    public void testSaveUser_NullUser() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            userService.addUser(null);
        });

        assertNotNull(exception);
    }


    /* Get Users */

    @Test
    public void testGetUsers_UsersFound() {
        User user1 = new User("ABC", "abc@mail.com", "asdf", List.of(Role.USER));
        User user2 = new User("DEF", "def@outlook.com", "123", List.of(Role.USER, Role.ADMIN));
        List<User> mockUsers = List.of(user1, user2);

        Mockito.when(userRepository.findAll()).thenReturn(mockUsers);

        List<UserResponse> userResponses = userService.getUsers();

        assertNotNull(userResponses);
        assertEquals(2, userResponses.size());
        assertEquals("ABC", userResponses.get(0).getFullname());
        assertEquals("abc@mail.com", userResponses.get(0).getEmail());
        assertEquals("DEF", userResponses.get(1).getFullname());
        assertEquals("def@outlook.com", userResponses.get(1).getEmail());
    }

    //    Empty Users
    @Test
    public void testGetUsers_NoUsersFound() {
        Mockito.when(userRepository.findAll()).thenReturn(List.of());

        List<UserResponse> userResponses = userService.getUsers();

        assertNotNull(userResponses);
        assertEquals(0, userResponses.size());
        assertTrue(userResponses.isEmpty());

    }
}
