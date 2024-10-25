package amnil.tm.controller;

import amnil.tm.dto.request.AddUserRequest;
import amnil.tm.dto.request.LoginRequest;
import amnil.tm.dto.response.ApiResponse;
import amnil.tm.dto.response.UserResponse;
import amnil.tm.service.token.TokenBlacklistService;
import amnil.tm.service.user.IUserService;
import amnil.tm.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthController {

    private final IUserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final TokenBlacklistService tokenBlacklistService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signup(@RequestBody AddUserRequest request) {
        try {
            UserResponse user = userService.addUser(request);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(
                            new ApiResponse("User added successfully", user)
                    );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            System.out.println("HERE!!" + loginRequest);

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());

            String jwt = jwtUtil.generateToken(userDetails.getUsername());

            return ResponseEntity.ok().body(new ApiResponse("token", jwt));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new ApiResponse("message", e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@RequestHeader("Authorization") String token) {
        System.out.println("ABCDEFGHIJ");
        SecurityContextHolder.clearContext();
        tokenBlacklistService.blacklistToken(token.substring(7));
        return ResponseEntity.ok().body(new ApiResponse("message", "You have been logged out!"));
    }
}
