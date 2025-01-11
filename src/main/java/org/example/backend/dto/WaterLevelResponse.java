package org.example.backend.dto;

public class WaterLevelResponse {
    private String status; // "success" or "error"
    private String message; // Descriptive message
    private Object data; // Water level data (single record or list)

    // Private constructor to enforce usage of factory methods
    private WaterLevelResponse() {}

    // Factory method for success response
    public static WaterLevelResponse success(String message, Object data) {
        WaterLevelResponse response = new WaterLevelResponse();
        response.status = "success";
        response.message = message;
        response.data = data;
        return response;
    }

    // Factory method for error response
    public static WaterLevelResponse error(String message) {
        WaterLevelResponse response = new WaterLevelResponse();
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
