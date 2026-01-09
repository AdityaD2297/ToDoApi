package com.application.todoapi.service;

import com.application.todoapi.common.mapper.ToDoResponseMapper;
import com.application.todoapi.common.request.ToDoRequest;
import com.application.todoapi.common.response.ToDoResponse;
import com.application.todoapi.entity.ToDo;
import com.application.todoapi.entity.User;
import com.application.todoapi.exception.ResourceNotFoundException;
import com.application.todoapi.repository.ToDoRepository;
import com.application.todoapi.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.time.LocalDateTime;

import static com.application.todoapi.common.specificatiom.ToDoSpecification.*;

@Service
@Slf4j
public class ToDoService {

    private final ToDoRepository toDoRepository;

    private final SecurityUtils securityUtils;

    private final ToDoResponseMapper toDoResponseMapper;

    public ToDoService(ToDoRepository toDoRepository, SecurityUtils securityUtils, ToDoResponseMapper toDoResponseMapper) {
        this.toDoRepository = toDoRepository;
        this.securityUtils = securityUtils;
        this.toDoResponseMapper = toDoResponseMapper;
    }

    @Transactional
    @PreAuthorize("hasRole('USER')")
    public ToDoResponse createToDo(ToDoRequest toDo) {
        log.info("Creating new ToDo item for user");
        Long userId = securityUtils.getCurrentUserId();
        toDo.isCreateRequestValid();
        User user = new User();
        user.setId(userId);
        ToDo newToDo = new ToDo();
        newToDo.setTitle(toDo.getTitle());
        newToDo.setDescription(toDo.getDescription());
        newToDo.setStatus(toDo.getStatus());
        newToDo.setPriority(toDo.getPriority());
        newToDo.setDueDate(toDo.getDueDate());
        newToDo.setCompleted(false);
        newToDo.setUser(user);
        log.info("ToDo item created successfully for user with ID: {}", userId);
        return toDoResponseMapper.mapToDoToResponse(toDoRepository.save(newToDo));
    }

    @Transactional(readOnly = true)
    public ToDoResponse getToDoById(Long id) {
        log.info("Fetching ToDo item with id: {} for current user", id);
        Long userId = securityUtils.getCurrentUserId();
        Optional<ToDo> toDo = toDoRepository.findByIdAndUserId(id, userId);
        if (toDo.isPresent()) {
            log.info("ToDo item with id: {} fetched successfully for current user", id);
            return toDoResponseMapper.mapToDoToResponse(toDo.get());
        } else {
            throw new ResourceNotFoundException("ToDo item not found with id: " + id);
        }
    }

    @Transactional(readOnly = true)
    public Page<ToDoResponse> getAllLists(String search, ToDo.Status status, ToDo.Priority priority, Boolean completed, LocalDateTime dueDate, Pageable pageable) {
        log.info("Fetching ToDo items for current user with filters - search: {}, status: {}, priority: {}, completed: {}", search, status, priority, completed);
        Long userId = securityUtils.getCurrentUserId();
        Page<ToDo> page;
        Specification <ToDo> spec = Specification
                .where(hasUser(userId))
                .and(hasStatus(status))
                .and(hasPriority(priority))
                .and(isCompleted(completed))
                .and(hasDueDate(dueDate))
                .and(titleContains(search));
        page = toDoRepository.findAll(spec, pageable);
        log.info("ToDo items fetched successfully for current user");
        return page.map(toDoResponseMapper::mapToDoToResponse);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @securityUtils.isOwnerOfToDo(#id)")
    public ToDoResponse updateToDo(Long id, ToDoRequest toDo) {
        log.info("Updating ToDo item with id: {} for current user", id);
        Long userId = securityUtils.getCurrentUserId();
        toDo.isUpdateRequestValid();
        Optional<ToDo> optional = toDoRepository.findByIdAndUserId(id, userId);
        if (optional.isPresent()) {
            ToDo existingToDo = optional.get();
            existingToDo.setTitle(toDo.getTitle());
            existingToDo.setDescription(toDo.getDescription());
            existingToDo.setStatus(toDo.getStatus());
            existingToDo.setPriority(toDo.getPriority());
            existingToDo.setDueDate(toDo.getDueDate());
            existingToDo.setCompleted(toDo.getCompleted());
            log.info("ToDo item with id: {} updated successfully for current user", id);
            return toDoResponseMapper.mapToDoToResponse(toDoRepository.save(existingToDo));
        } else {
            throw new ResourceNotFoundException("ToDo item not found with id: " + id);
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @securityUtils.isOwnerOfToDo(#id)")
    public void deleteToDo(Long id) {
        log.info("Deleting ToDo item with id: {} for current user", id);
        Long userId = securityUtils.getCurrentUserId();
        Optional<ToDo> existingToDo = toDoRepository.findByIdAndUserId(id, userId);
        if (existingToDo.isPresent()) {
            log.info("ToDo item with id: {} deleted successfully for current user", id);
            toDoRepository.delete(existingToDo.get());
        } else {
            throw new ResourceNotFoundException("ToDo item not found with id: " + id);
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @securityUtils.isOwnerOfToDo(#id)")
    public ToDoResponse patchToDo(Long id, ToDoRequest patchRequest) {
        log.info("Patching ToDo item with id: {} for current user", id);
        Long userId = securityUtils.getCurrentUserId();
        Optional<ToDo> optional = toDoRepository.findByIdAndUserId(id, userId);
        if (optional.isPresent()) {
            ToDo existingToDo = optional.get();
            if (patchRequest.getTitle() != null) {
                existingToDo.setTitle(patchRequest.getTitle());
            }

            if (patchRequest.getDescription() != null) {
                existingToDo.setDescription(patchRequest.getDescription());
            }

            if (patchRequest.getStatus() != null) {
                existingToDo.setStatus(patchRequest.getStatus());
            }

            if (patchRequest.getPriority() != null) {
                existingToDo.setPriority(patchRequest.getPriority());
            }

            if (patchRequest.getDueDate() != null) {
                existingToDo.setDueDate(patchRequest.getDueDate());
            }

            if (patchRequest.getCompleted() != null) {
                existingToDo.setCompleted(patchRequest.getCompleted());
            }
            log.info("ToDo item with id: {} patched successfully for current user", id);
            return toDoResponseMapper.mapToDoToResponse(toDoRepository.save(existingToDo));
        } else {
            throw new ResourceNotFoundException("ToDo item not found with id: " + id);
        }
    }

}
