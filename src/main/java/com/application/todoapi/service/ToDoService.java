package com.application.todoapi.service;

import com.application.todoapi.common.request.ToDoRequest;
import com.application.todoapi.common.response.ToDoResponse;
import com.application.todoapi.entity.ToDo;
import com.application.todoapi.exception.ResourceNotFoundException;
import com.application.todoapi.repository.ToDoRepository;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@NoArgsConstructor
public class ToDoService {

    private ToDoRepository toDoRepository;

    public ToDoService(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    public ToDoResponse createToDo(ToDoRequest toDo) {
        toDo.isCreateRequestValid();
        ToDo newToDo = new ToDo();
        newToDo.setTitle(toDo.getTitle());
        newToDo.setDescription(toDo.getDescription());
        newToDo.setStatus(toDo.getStatus());
        newToDo.setCompleted(false);
        newToDo.setCreatedAt(LocalDateTime.now());
        return mapToDoToResponse(toDoRepository.save(newToDo));
    }

    public ToDoResponse getToDoById(Long id) {
        return toDoRepository.findById(id).map(this::mapToDoToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("ToDo item not found with id: " + id));
    }

    public Page<ToDoResponse> getAllLists(Pageable pageable) {
        return toDoRepository.findAll(pageable)
                .map(this::mapToDoToResponse);
    }

    public ToDoResponse updateToDo(Long id, ToDoRequest toDo) {
        toDo.isUpdateRequestValid();
        ToDo existingToDo = toDoRepository.findById(id).orElse(null);
        existingToDo.setTitle(toDo.getTitle());
        existingToDo.setDescription(toDo.getDescription());
        existingToDo.setStatus(toDo.getStatus());
        existingToDo.setCompleted(toDo.getCompleted());
        existingToDo.setUpdatedAt(LocalDateTime.now());
        return mapToDoToResponse(toDoRepository.save(existingToDo));
    }

    public void deleteToDo(Long id) {
        if(!toDoRepository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("ToDo item not found with id: " + id);
        }
        toDoRepository.deleteById(id);
    }

    public ToDoResponse patchToDo(Long id, ToDoRequest patchRequest) {
        ToDo existingToDo = toDoRepository.findById(id).orElse(null);
        if (patchRequest.getTitle() != null) {
            existingToDo.setTitle(patchRequest.getTitle());
        }

        if (patchRequest.getDescription() != null) {
            existingToDo.setDescription(patchRequest.getDescription());
        }

        if (patchRequest.getStatus() != null) {
            existingToDo.setStatus(patchRequest.getStatus());
        }

        if (patchRequest.getCompleted() != null) {
            existingToDo.setCompleted(patchRequest.getCompleted());
        }

        existingToDo.setUpdatedAt(LocalDateTime.now());

        return mapToDoToResponse(existingToDo);
    }

    public ToDoResponse mapToDoToResponse(ToDo toDo) {
        ToDoResponse response = new ToDoResponse();
        response.setId(toDo.getId());
        response.setTitle(toDo.getTitle());
        response.setDescription(toDo.getDescription());
        response.setStatus(toDo.getStatus());
        response.setCompleted(toDo.isCompleted());
        response.setCreatedAt(toDo.getCreatedAt());
        response.setUpdatedAt(toDo.getUpdatedAt());
        return response;
    }

}
