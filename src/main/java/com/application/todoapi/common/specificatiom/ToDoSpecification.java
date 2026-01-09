package com.application.todoapi.common.specificatiom;

import com.application.todoapi.entity.ToDo;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ToDoSpecification {

    public static Specification<ToDo> hasUser(Long userId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("user").get("id"), userId);
    }

    public static Specification<ToDo> hasStatus(ToDo.Status status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<ToDo> hasPriority(ToDo.Priority priority) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("priority"), priority);
    }

    public static Specification<ToDo> hasDueDate(LocalDateTime dueDate) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("dueDate"), dueDate);
    }

    public static Specification<ToDo> isCompleted(Boolean completed) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("completed"), completed);
    }

    public static Specification<ToDo> titleContains(String title) {
        return (root, query, criteriaBuilder) ->
                (title == null || title.isEmpty()) ? null :
                criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

}
