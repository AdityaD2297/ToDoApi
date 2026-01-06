package com.application.todoapi.common.response;

import com.application.todoapi.entity.ToDo;
import com.application.todoapi.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ToDoResponse {

    private Long id;
    private String title;
    private String description;
    private ToDo.Status status;
    private ToDo.Priority priority;
    private Boolean completed;
    private User user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
