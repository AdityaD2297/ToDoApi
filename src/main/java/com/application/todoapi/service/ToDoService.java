package com.application.todoapi.service;

import com.application.todoapi.common.request.ToDoRequest;
import com.application.todoapi.common.response.ToDoResponse;
import com.application.todoapi.entity.ToDo;
import com.application.todoapi.entity.User;
import com.application.todoapi.exception.ResourceNotFoundException;
import com.application.todoapi.repository.ToDoRepository;
import com.application.todoapi.repository.UserRepository;
import com.application.todoapi.security.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@NoArgsConstructor
public class ToDoService {

    private ToDoRepository toDoRepository;

    private UserRepository userRepository;

    private SecurityUtils securityUtils;

    public ToDoService(ToDoRepository toDoRepository) {
        this.toDoRepository = toDoRepository;
    }

    public ToDoResponse createToDo(ToDoRequest toDo) {
        Long userId = securityUtils.getCurrentUserId();
        toDo.isCreateRequestValid();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        ToDo newToDo = new ToDo();
        newToDo.setTitle(toDo.getTitle());
        newToDo.setDescription(toDo.getDescription());
        newToDo.setStatus(toDo.getStatus());
        newToDo.setPriority(toDo.getPriority());
        newToDo.setCompleted(false);
        newToDo.setUser(user);
        return mapToDoToResponse(toDoRepository.save(newToDo));
    }

    public ToDoResponse getToDoById(Long id) {
        Long userId = securityUtils.getCurrentUserId();
        ToDo toDo = toDoRepository.findByIdAndUserId(id, userId);
        if (toDo == null) {
            throw new ResourceNotFoundException("ToDo item not found with id: " + id);
        }
        return mapToDoToResponse(toDo);
    }

    public Page<ToDoResponse> getAllLists(String search, ToDo.Status status, ToDo.Priority priority, Boolean completed,Pageable pageable) {
        Long userId = securityUtils.getCurrentUserId();
        Page<ToDo> page;
        if (search != null && !search.isEmpty()) {
            page = toDoRepository.findByTitleContainingIgnoreCase(search, userId, pageable);
        } else if (status != null && completed != null) {
            page = toDoRepository.findByStatusAndCompleted(status, completed, userId, pageable);
        } else if (status != null && priority != null) {
            page = toDoRepository.findByStatusAndPriority(status, priority, userId, pageable);
        }  else if (priority != null && completed != null) {
            page = toDoRepository.findByPriorityAndCompleted(priority, completed, userId, pageable);
        } else if (status != null) {
            page = toDoRepository.findByStatus(status, userId, pageable);
        } else if (priority != null) {
            page = toDoRepository.findByPriority(priority, userId, pageable);
        } else if (completed != null) {
            page = toDoRepository.findByCompleted(completed, userId, pageable);
        } else {
            page = toDoRepository.findAll(userId, pageable);
        }
        return page.map(this::mapToDoToResponse);
    }

    public ToDoResponse updateToDo(Long id, ToDoRequest toDo) {
        Long userId = securityUtils.getCurrentUserId();
        toDo.isUpdateRequestValid();
        ToDo existingToDo = toDoRepository.findByIdAndUserId(id, userId);
        if (existingToDo == null) {
            throw new ResourceNotFoundException("ToDo item not found with id: " + id);
        }
        existingToDo.setTitle(toDo.getTitle());
        existingToDo.setDescription(toDo.getDescription());
        existingToDo.setStatus(toDo.getStatus());
        existingToDo.setPriority(toDo.getPriority());
        existingToDo.setCompleted(toDo.getCompleted());
        return mapToDoToResponse(toDoRepository.save(existingToDo));
    }

    public void deleteToDo(Long id) {
        Long userId = securityUtils.getCurrentUserId();
        ToDo existingToDo = toDoRepository.findByIdAndUserId(id, userId);
        if (existingToDo == null) {
            throw new ResourceNotFoundException("ToDo item not found with id: " + id);
        }
        toDoRepository.delete(existingToDo);
    }

    public ToDoResponse patchToDo(Long id, ToDoRequest patchRequest) {
        Long userId = securityUtils.getCurrentUserId();
        ToDo existingToDo = toDoRepository.findByIdAndUserId(id, userId);
        if (existingToDo == null) {
            throw new ResourceNotFoundException("ToDo item not found with id: " + id);
        }
        if (patchRequest.getTitle() != null) {
            existingToDo.setTitle(patchRequest.getTitle());
        }

        if (patchRequest.getDescription() != null) {
            existingToDo.setDescription(patchRequest.getDescription());
        }

        if (patchRequest.getStatus() != null) {
            existingToDo.setStatus(patchRequest.getStatus());
        }

        if(patchRequest.getPriority() != null) {
            existingToDo.setPriority(patchRequest.getPriority());
        }

        if (patchRequest.getCompleted() != null) {
            existingToDo.setCompleted(patchRequest.getCompleted());
        }

        return mapToDoToResponse(existingToDo);
    }

    public ToDoResponse mapToDoToResponse(ToDo toDo) {
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
