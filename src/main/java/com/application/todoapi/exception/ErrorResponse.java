package com.application.todoapi.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private String type;
    private long timestamp;
    
    public ErrorResponse(String message, String type) {
        this.message = message;
        this.type = type;
        this.timestamp = System.currentTimeMillis();
    }
}
