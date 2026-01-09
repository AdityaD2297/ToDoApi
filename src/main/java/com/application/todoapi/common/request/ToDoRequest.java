package com.application.todoapi.common.request;

import com.application.todoapi.entity.ToDo;
import com.application.todoapi.exception.ResourceNotFoundException;
import lombok.*;

import java.util.Objects;
import java.util.stream.Stream;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ToDoRequest {

    private String title;
    private String description;
    private ToDo.Status status;
    private ToDo.Priority priority;
    private LocalDateTime dueDate;
    private Boolean completed;

    public void isCreateRequestValid() {
         if(!Stream.of(title, status, priority, dueDate).allMatch(Objects::nonNull)) {
             throw new ResourceNotFoundException("Required Fields Missing");
         }
    }

    public void isUpdateRequestValid(){
        if(!Stream.of(title, status, priority, completed).allMatch(Objects::nonNull)) {
            throw new ResourceNotFoundException("Required Fields Missing");
        }
    }

}
