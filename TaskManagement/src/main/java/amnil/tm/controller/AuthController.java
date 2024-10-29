package amnil.tm.controller;

import amnil.tm.dto.request.AddUserRequest;
import amnil.tm.dto.request.LoginRequest;
import amnil.tm.dto.response.ApiResponse;
import amnil.tm.dto.response.UserResponse;
import amnil.tm.service.UserDetailsServiceImpl;
import amnil.tm.service.user.IUserService;
import amnil.tm.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/auth")
public class AuthController {

    private final IUserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(IUserService userService, AuthenticationManager authenticationManager,
                          UserDetailsServiceImpl userDetailsService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> signup(@ModelAttribute AddUserRequest request) {
        try {
            UserResponse user = userService.addUser(request);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                            new ApiResponse("User added successfully", user)
                    );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            if (loginRequest.getUsername() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Error", "Username is required"));
            }
            if (loginRequest.getPassword() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Error", "Password is required"));
            }

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());

            String token = jwtUtil.generateToken(userDetails.getUsername(),
                    userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())
            );

            return ResponseEntity.ok().body(new ApiResponse("token", token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", e.getMessage()));
        }
    }
}
