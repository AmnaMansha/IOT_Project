package org.example.backend.dto;

public class HumidityResponse {
    private String status; // "success" or "error"
    private String message; // Descriptive message
    private Object data; // Humidity data (single record or list)

    // Private constructor to enforce usage of factory methods
    private HumidityResponse() {}

    // Factory method for success response
    public static HumidityResponse success(String message, Object data) {
        HumidityResponse response = new HumidityResponse();
        response.status = "success";
        response.message = message;
        response.data = data;
        return response;
    }

    // Factory method for error response
    public static HumidityResponse error(String message) {
        HumidityResponse response = new HumidityResponse();
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
