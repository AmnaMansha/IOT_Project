package org.example.backend.dto;

public class TemperatureResponse {
    private String status; // "success" or "error"
    private String message; // Descriptive message
    private Object data; // Temperature data (single record or list)

    // Private constructor to enforce usage of factory methods
    private TemperatureResponse() {}

    // Factory method for success response
    public static TemperatureResponse success(String message, Object data) {
        TemperatureResponse response = new TemperatureResponse();
        response.status = "success";
        response.message = message;
        response.data = data;
        return response;
    }

    // Factory method for error response
    public static TemperatureResponse error(String message) {
        TemperatureResponse response = new TemperatureResponse();
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
