package com.application.todoapi.service;

import com.application.todoapi.common.mapper.ToDoResponseMapper;
import com.application.todoapi.common.request.ToDoRequest;
import com.application.todoapi.common.response.ToDoResponse;
import com.application.todoapi.entity.ToDo;
import com.application.todoapi.entity.User;
import com.application.todoapi.exception.ResourceNotFoundException;
import com.application.todoapi.repository.ToDoRepository;
import com.application.todoapi.repository.UserRepository;
import com.application.todoapi.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.time.LocalDateTime;

@Service
@Slf4j
public class ToDoService {

    private final ToDoRepository toDoRepository;

    private final UserRepository userRepository;

    private final SecurityUtils securityUtils;

    private final ToDoResponseMapper toDoResponseMapper;

    public ToDoService(ToDoRepository toDoRepository, UserRepository userRepository, SecurityUtils securityUtils, ToDoResponseMapper toDoResponseMapper) {
        this.toDoRepository = toDoRepository;
        this.userRepository = userRepository;
        this.securityUtils = securityUtils;
        this.toDoResponseMapper = toDoResponseMapper;
    }

    @Transactional
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
    public Page<ToDoResponse> getAllLists(String search, ToDo.Status status, ToDo.Priority priority, Boolean completed, LocalDateTime dueDate, Long userId, Pageable pageable) {
        log.info("Fetching ToDo items for user {} with filters - search: {}, status: {}, priority: {}, completed: {}, dueDate: {}", userId, search, status, priority, completed, dueDate);

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        Page<ToDo> page = null;
        
        // Count non-null filters for better logic
        int filterCount = 0;
        if (search != null) filterCount++;
        if (status != null) filterCount++;
        if (priority != null) filterCount++;
        if (completed != null) filterCount++;
        if (dueDate != null) filterCount++;
        
        // Handle all combinations of filters
        if (filterCount == 5) {
            // All filters present: search + status + priority + completed + dueDate
            page = toDoRepository.findByUserAndTitleContainingIgnoreCaseAndStatusAndDueDateBeforeOrEqual(userId, search, status, dueDate, pageable);
        } else if (filterCount == 4) {
            // 4 filters present
            if (dueDate == null) {
                // search + status + priority + completed
                page = toDoRepository.findByUserAndTitleContainingIgnoreCaseAndStatusAndPriorityAndCompleted(user, search, status, priority, completed, pageable);
            } else if (search == null) {
                // status + priority + completed + dueDate (get todos on/before due date)
                page = toDoRepository.findByUserAndCompletedAndDueDateBeforeOrEqual(userId, completed, dueDate, pageable);
            } else if (status == null) {
                // search + priority + completed + dueDate (get todos on/before due date)
                page = toDoRepository.findByUserAndTitleContainingIgnoreCaseAndPriorityAndCompleted(user, search, priority, completed, pageable);
            } else if (priority == null) {
                // search + status + completed + dueDate (get todos on/before due date)
                page = toDoRepository.findByUserAndTitleContainingIgnoreCaseAndStatusAndDueDateBeforeOrEqual(userId, search, status, dueDate, pageable);
            } else if (completed == null) {
                // search + status + priority + dueDate (get todos on/before due date)
                page = toDoRepository.findByUserAndTitleContainingIgnoreCaseAndStatusAndDueDateBeforeOrEqual(userId, search, status, dueDate, pageable);
            }
        } else if (filterCount == 3) {
            // 3 filters present
            if (search != null && status != null && priority != null) {
                // search + status + priority
                page = toDoRepository.findByUserAndTitleContainingIgnoreCaseAndStatusAndPriority(user, search, status, priority, pageable);
            } else if (search != null && status != null && completed != null) {
                // search + status + completed
                page = toDoRepository.findByUserAndTitleContainingIgnoreCaseAndStatusAndCompleted(user, search, status, completed, pageable);
            } else if (search != null && status != null && dueDate != null) {
                // search + status + dueDate (get todos on/before due date)
                page = toDoRepository.findByUserAndTitleContainingIgnoreCaseAndStatusAndDueDateBeforeOrEqual(userId, search, status, dueDate, pageable);
            } else if (search != null && priority != null && completed != null) {
                // search + priority + completed
                page = toDoRepository.findByUserAndTitleContainingIgnoreCaseAndPriorityAndCompleted(user, search, priority, completed, pageable);
            } else if (search != null && priority != null && dueDate != null) {
                // search + priority + dueDate (get todos on/before due date)
                page = toDoRepository.findByUserIdAndDueDateBeforeOrEqual(userId, dueDate, pageable);
            } else if (search != null && completed != null && dueDate != null) {
                // search + completed + dueDate (get todos on/before due date)
                page = toDoRepository.findByUserIdAndDueDateBeforeOrEqual(userId, dueDate, pageable);
            } else if (status != null && priority != null && completed != null) {
                // status + priority + completed
                page = toDoRepository.findByUserAndStatusAndPriorityAndCompleted(user, status, priority, completed, pageable);
            } else if (status != null && priority != null && dueDate != null) {
                // status + priority + dueDate (get todos on/before due date)
                page = toDoRepository.findByUserAndPriorityAndDueDateBeforeOrEqual(userId, priority, dueDate, pageable);
            } else if (status != null && completed != null && dueDate != null) {
                // status + completed + dueDate (get todos on/before due date)
                page = toDoRepository.findByUserAndCompletedAndDueDateBeforeOrEqual(userId, completed, dueDate, pageable);
            } else if (priority != null && completed != null && dueDate != null) {
                // priority + completed + dueDate (get todos on/before due date)
                page = toDoRepository.findByUserAndCompletedAndDueDateBeforeOrEqual(userId, completed, dueDate, pageable);
            }
        } else if (filterCount == 2) {
            // 2 filters present
            if (search != null && status != null) {
                // search + status
                page = toDoRepository.findByUserAndTitleContainingIgnoreCaseAndStatus(user, search, status, pageable);
            } else if (search != null && priority != null) {
                // search + priority
                page = toDoRepository.findByUserAndTitleContainingIgnoreCaseAndPriority(user, search, priority, pageable);
            } else if (search != null && completed != null) {
                // search + completed
                page = toDoRepository.findByUserAndTitleContainingIgnoreCaseAndCompleted(user, search, completed, pageable);
            } else if (search != null && dueDate != null) {
                // search + dueDate (get todos on/before due date)
                page = toDoRepository.findByUserIdAndDueDateBeforeOrEqual(userId, dueDate, pageable);
            } else if (status != null && priority != null) {
                // status + priority
                page = toDoRepository.findByUserAndStatusAndPriority(user, status, priority, pageable);
            } else if (status != null && completed != null) {
                // status + completed
                page = toDoRepository.findByUserAndStatusAndCompleted(user, status, completed, pageable);
            } else if (status != null && dueDate != null) {
                // status + dueDate (get todos on/before due date)
                page = toDoRepository.findByUserIdAndDueDateBeforeOrEqual(userId, dueDate, pageable);
            } else if (priority != null && completed != null) {
                // priority + completed
                page = toDoRepository.findByUserAndPriorityAndCompleted(user, priority, completed, pageable);
            } else if (priority != null && dueDate != null) {
                // priority + dueDate (get todos on/before due date)
                page = toDoRepository.findByUserAndPriorityAndDueDateBeforeOrEqual(userId, priority, dueDate, pageable);
            } else if (completed != null && dueDate != null) {
                // completed + dueDate (get todos on/before due date)
                page = toDoRepository.findByUserAndCompletedAndDueDateBeforeOrEqual(userId, completed, dueDate, pageable);
            }
        } else if (filterCount == 1) {
            // 1 filter present
            if (search != null) {
                // search only
                page = toDoRepository.findByUserAndTitleContainingIgnoreCase(user, search, pageable);
            } else if (status != null) {
                // status only
                page = toDoRepository.findByUserAndStatus(user, status, pageable);
            } else if (priority != null) {
                // priority only
                page = toDoRepository.findByUserAndPriority(user, priority, pageable);
            } else if (completed != null) {
                // completed only
                page = toDoRepository.findByUserAndCompleted(user, completed, pageable);
            } else if (dueDate != null) {
                // dueDate only - get todos on or before due date
                page = toDoRepository.findByUserIdAndDueDateBeforeOrEqual(userId, dueDate, pageable);
            } else {
                // Fallback - should not happen but ensures page is initialized
                page = toDoRepository.findByUserId(userId, pageable);
            }
        } else {
            // No filters - get all todos for user
            page = toDoRepository.findByUserId(userId, pageable);
        }
        
        log.info("ToDo items fetched successfully for user with ID: {}", userId);
        return page.map(toDoResponseMapper::mapToDoToResponse);
    }

    @Transactional
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
