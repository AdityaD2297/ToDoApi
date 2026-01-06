package com.application.todoapi.security;

import com.application.todoapi.repository.ToDoRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    private final ToDoRepository todoRepository;

    public SecurityUtils(ToDoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Long) {
            return (Long) authentication.getPrincipal();
        }
        return null;
    }

    public boolean isOwnerOfToDo(Long todoId) {
        Long userId = getCurrentUserId();
        return todoRepository.existsByIdAndUserId(todoId, userId);
    }

    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }
}
