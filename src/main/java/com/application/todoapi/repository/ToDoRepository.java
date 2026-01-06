package com.application.todoapi.repository;

import com.application.todoapi.entity.ToDo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Long>, JpaSpecificationExecutor<ToDo> {

    Optional<ToDo> findByIdAndUserId(Long id, Long userId);

    boolean existsByIdAndUserId(Long id, Long userId);

    Page<ToDo> findByTitleContainingIgnoreCase(String title, Long userId, Pageable pageable);

    Page<ToDo> findByStatusAndCompleted(ToDo.Status status, boolean completed, Long userId, Pageable pageable);

    Page<ToDo> findByStatusAndPriority(ToDo.Status status, ToDo.Priority priority, Long userId, Pageable pageable);

    Page<ToDo> findByPriorityAndCompleted(ToDo.Priority priority, boolean completed, Long userId, Pageable pageable);

    Page<ToDo> findByStatus(ToDo.Status status, Long userId, Pageable pageable);

    Page<ToDo> findByPriority(ToDo.Priority priority, Long userId, Pageable pageable);

    Page<ToDo> findByCompleted(boolean completed, Long userId, Pageable pageable);

    Page<ToDo> findAllByUserId(Long userId, Pageable pageable);
}
