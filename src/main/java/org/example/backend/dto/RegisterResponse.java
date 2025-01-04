package org.example.backend.dto;

public class RegisterResponse {
    private String status; // "success" or "error"
    private String message; // Descriptive message
    private Object data; // User data (only for success responses)

    // Private constructor to enforce usage of factory methods
    private RegisterResponse() {}

    // Factory method for success response
    public static RegisterResponse success(String message, Object data) {
        RegisterResponse response = new RegisterResponse();
        response.status = "success";
        response.message = message;
        response.data = data;
        return response;
    }

    // Factory method for error response
    public static RegisterResponse error(String message) {
        RegisterResponse response = new RegisterResponse();
        response.status = "error";
        response.message = message;
        response.data = null;
        return response;
    }

    // Getters
    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public Object getData() { return data; }
}
