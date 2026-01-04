package com.application.todoapi.repository;

import com.application.todoapi.entity.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Long> {
}
