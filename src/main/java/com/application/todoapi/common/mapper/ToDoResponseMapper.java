package com.application.todoapi.common.mapper;

import com.application.todoapi.common.response.ToDoResponse;
import com.application.todoapi.entity.ToDo;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ToDoResponseMapper {

    public ToDoResponse mapToDoToResponse(ToDo toDo) {
        return getToDoResponse(toDo);
    }

    @NonNull
    public static ToDoResponse getToDoResponse(ToDo toDo) {
        ToDoResponse response = new ToDoResponse();
        response.setId(toDo.getId());
        response.setTitle(toDo.getTitle());
        response.setDescription(toDo.getDescription());
        response.setStatus(toDo.getStatus());
        response.setCompleted(toDo.getCompleted());
        response.setCreatedAt(toDo.getCreatedAt());
        response.setUpdatedAt(toDo.getUpdatedAt());
        return response;
    }
}
