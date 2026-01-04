package com.application.todoapi.common.request;

import com.application.todoapi.entity.ToDo;
import com.application.todoapi.exception.ResourceNotFoundException;
import lombok.Data;

import java.util.Objects;
import java.util.stream.Stream;

@Data
public class ToDoRequest {

    private String title;
    private String description;
    private ToDo.Status status;
    private Boolean completed;

    public void isCreateRequestValid() {
         if(!Stream.of(title, status).allMatch(Objects::nonNull)) {
             throw new ResourceNotFoundException("Required Fields Missing");
         }
    }

    public void isUpdateRequestValid(){
        if(!Stream.of(title, status, completed).allMatch(Objects::nonNull)) {
            throw new ResourceNotFoundException("Required Fields Missing");
        }
    }

}
