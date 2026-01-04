package com.application.todoapi.common.response;

import com.application.todoapi.entity.ToDo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ToDoResponse {

    private Long id;
    private String title;
    private String description;
    private ToDo.Status status;
    private Boolean completed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
