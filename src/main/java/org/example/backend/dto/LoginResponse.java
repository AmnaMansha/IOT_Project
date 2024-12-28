package org.example.backend.dto;

import org.example.backend.model.User;

public class LoginResponse {
    private String status;
    private String message;
    private UserData data;

    public static class UserData {
        private Long id;
        private String name;
        private String email;
        private String role;

        public UserData(Long id, String name, String email, String role) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.role = role;
        }

        // Getters
        public Long getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getRole() { return role; }
    }

    // Constructors
    public static LoginResponse success(User user) {
        LoginResponse response = new LoginResponse();
        response.status = "success";
        response.message = "Login successful";
        response.data = new UserData(user.getId(), user.getName(), user.getEmail(), user.getRole().name());
        return response;
    }

    public static LoginResponse error(String message) {
        LoginResponse response = new LoginResponse();
        response.status = "error";
        response.message = message;
        return response;
    }

    // Getters
    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public UserData getData() { return data; }
}