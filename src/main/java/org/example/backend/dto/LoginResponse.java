package org.example.backend.dto;

import org.example.backend.model.User;

public class LoginResponse {
    private String status; // "success" or "error"
    private String message; // Descriptive message
    private UserData data; // User data (only for success responses)

    // Inner class for user data
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

    // Private constructor to enforce usage of factory methods
    private LoginResponse() {}

    // Factory method for success response
    public static LoginResponse success(String message, User user) {
        LoginResponse response = new LoginResponse();
        response.status = "success";
        response.message = message;
        response.data = new UserData(user.getId(), user.getName(), user.getEmail(), user.getRole().name());
        return response;
    }

    // Factory method for error response
    public static LoginResponse error(String message) {
        LoginResponse response = new LoginResponse();
        response.status = "error";
        response.message = message;
        response.data = null; // No data for error responses
        return response;
    }

    // Getters
    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public UserData getData() { return data; }
}
