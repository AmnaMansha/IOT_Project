package org.example.backend.dto;

public class AlertResponse {
    private String status; // "success" or "error"
    private String message; // Descriptive message
    private Object data; // Alert data (single record or list)

    // Private constructor to enforce usage of factory methods
    private AlertResponse() {}

    // Factory method for success response
    public static AlertResponse success(String message, Object data) {
        AlertResponse response = new AlertResponse();
        response.status = "success";
        response.message = message;
        response.data = data;
        return response;
    }

    // Factory method for error response
    public static AlertResponse error(String message) {
        AlertResponse response = new AlertResponse();
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
