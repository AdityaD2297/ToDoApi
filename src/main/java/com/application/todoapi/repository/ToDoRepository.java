package com.application.todoapi.repository;

import com.application.todoapi.entity.ToDo;
import com.application.todoapi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Long>, JpaSpecificationExecutor<ToDo> {

    Optional<ToDo> findByIdAndUserId(Long id, Long userId);

    boolean existsByIdAndUserId(Long id, Long userId);

    // All combinations for filtering
    Page<ToDo> findByUserAndTitleContainingIgnoreCaseAndStatusAndPriorityAndCompleted(User user, String title, ToDo.Status status, ToDo.Priority priority, Boolean completed, Pageable pageable);
    
    Page<ToDo> findByUserAndTitleContainingIgnoreCaseAndStatusAndPriority(User user, String title, ToDo.Status status, ToDo.Priority priority, Pageable pageable);
    
    Page<ToDo> findByUserAndTitleContainingIgnoreCaseAndStatusAndCompleted(User user, String title, ToDo.Status status, Boolean completed, Pageable pageable);
    
    Page<ToDo> findByUserAndTitleContainingIgnoreCaseAndStatus(User user, String title, ToDo.Status status, Pageable pageable);
    
    Page<ToDo> findByUserAndTitleContainingIgnoreCaseAndPriorityAndCompleted(User user, String title, ToDo.Priority priority, Boolean completed, Pageable pageable);
    
    Page<ToDo> findByUserAndTitleContainingIgnoreCaseAndPriority(User user, String title, ToDo.Priority priority, Pageable pageable);
    
    Page<ToDo> findByUserAndTitleContainingIgnoreCaseAndCompleted(User user, String title, Boolean completed, Pageable pageable);
    
    Page<ToDo> findByUserAndTitleContainingIgnoreCase(User user, String title, Pageable pageable);
    
    Page<ToDo> findByUserAndStatusAndPriorityAndCompleted(User user, ToDo.Status status, ToDo.Priority priority, Boolean completed, Pageable pageable);
    
    Page<ToDo> findByUserAndStatusAndPriority(User user, ToDo.Status status, ToDo.Priority priority, Pageable pageable);
    
    Page<ToDo> findByUserAndStatusAndCompleted(User user, ToDo.Status status, Boolean completed, Pageable pageable);
    
    Page<ToDo> findByUserAndStatus(User user, ToDo.Status status, Pageable pageable);
    
    Page<ToDo> findByUserAndPriorityAndCompleted(User user, ToDo.Priority priority, Boolean completed, Pageable pageable);
    
    Page<ToDo> findByUserAndPriority(User user, ToDo.Priority priority, Pageable pageable);
    
    Page<ToDo> findByUserAndCompleted(User user, Boolean completed, Pageable pageable);
    
    Page<ToDo> findByUserId(Long userId, Pageable pageable);
    
    // Due date filtering methods
    @Query("SELECT t FROM ToDo t WHERE t.user.id = :userId AND t.dueDate <= :dueDate")
    Page<ToDo> findByUserIdAndDueDateBeforeOrEqual(@Param("userId") Long userId, @Param("dueDate") LocalDateTime dueDate, Pageable pageable);
    
    @Query("SELECT t FROM ToDo t WHERE t.user.id = :userId AND t.dueDate > :dueDate")
    Page<ToDo> findByUserIdAndDueDateAfter(@Param("userId") Long userId, @Param("dueDate") LocalDateTime dueDate, Pageable pageable);
    
    @Query("SELECT t FROM ToDo t WHERE t.user.id = :userId AND t.dueDate = :dueDate")
    Page<ToDo> findByUserIdAndDueDateEqual(@Param("userId") Long userId, @Param("dueDate") LocalDateTime dueDate, Pageable pageable);
    
    // Combined due date with other filters
    @Query("SELECT t FROM ToDo t WHERE t.user.id = :userId AND LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%')) AND t.status = :status AND t.dueDate <= :dueDate")
    Page<ToDo> findByUserAndTitleContainingIgnoreCaseAndStatusAndDueDateBeforeOrEqual(@Param("userId") Long userId, @Param("search") String search, @Param("status") ToDo.Status status, @Param("dueDate") LocalDateTime dueDate, Pageable pageable);
    
    @Query("SELECT t FROM ToDo t WHERE t.user.id = :userId AND LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%')) AND t.status = :status AND t.dueDate > :dueDate")
    Page<ToDo> findByUserAndTitleContainingIgnoreCaseAndStatusAndDueDateAfter(@Param("userId") Long userId, @Param("search") String search, @Param("status") ToDo.Status status, @Param("dueDate") LocalDateTime dueDate, Pageable pageable);
    
    @Query("SELECT t FROM ToDo t WHERE t.user.id = :userId AND t.priority = :priority AND t.dueDate <= :dueDate")
    Page<ToDo> findByUserAndPriorityAndDueDateBeforeOrEqual(@Param("userId") Long userId, @Param("priority") ToDo.Priority priority, @Param("dueDate") LocalDateTime dueDate, Pageable pageable);
    
    @Query("SELECT t FROM ToDo t WHERE t.user.id = :userId AND t.priority = :priority AND t.dueDate > :dueDate")
    Page<ToDo> findByUserAndPriorityAndDueDateAfter(@Param("userId") Long userId, @Param("priority") ToDo.Priority priority, @Param("dueDate") LocalDateTime dueDate, Pageable pageable);
    
    @Query("SELECT t FROM ToDo t WHERE t.user.id = :userId AND t.completed = :completed AND t.dueDate <= :dueDate")
    Page<ToDo> findByUserAndCompletedAndDueDateBeforeOrEqual(@Param("userId") Long userId, @Param("completed") boolean completed, @Param("dueDate") LocalDateTime dueDate, Pageable pageable);
    
    @Query("SELECT t FROM ToDo t WHERE t.user.id = :userId AND t.completed = :completed AND t.dueDate > :dueDate")
    Page<ToDo> findByUserAndCompletedAndDueDateAfter(@Param("userId") Long userId, @Param("completed") boolean completed, @Param("dueDate") LocalDateTime dueDate, Pageable pageable);
}
