package org.example.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.example.backend.service.UserService;
import org.example.backend.model.User;
import java.util.List;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.example.backend.model.LoginRequest;
import org.example.backend.dto.LoginResponse;
import org.example.backend.dto.RegisterRequest;
import org.example.backend.model.Role;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(
        @RequestParam String name,
        @RequestParam String email,
        @RequestParam String password,
        @RequestParam(required = false, defaultValue = "USER") Role role
    ) {
        try {
            User user = userService.register(name, email, password, role);
            return ResponseEntity.ok(new LoginResponse.UserData(
                user.getId(), 
                user.getName(), 
                user.getEmail(), 
                user.getRole().name()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(LoginResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/test")
    public String test() {
        return "API Test";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // First, check if user exists
            User user = userService.findByEmail(loginRequest.getEmail());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(LoginResponse.error("User not found"));
            }

            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), 
                    loginRequest.getPassword()
                )
            );

            if (authentication.isAuthenticated()) {
                return ResponseEntity.ok(LoginResponse.success(user));
            }
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(LoginResponse.error("Invalid credentials"));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(LoginResponse.error("Authentication failed: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(LoginResponse.error("Error during login: " + e.getMessage()));
        }
    }

    @GetMapping("/test-user/{email}")
    public ResponseEntity<?> testUser(@PathVariable String email) {
        try {
            User user = userService.findByEmail(email);
            return ResponseEntity.ok("User found: " + user.getEmail());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}
