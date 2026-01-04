package com.application.todoapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "todo")
public class ToDo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    private String description;

    @Column(nullable = false)
    private boolean completed;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public enum Status {
        PENDING,
        IN_PROGRESS,
        COMPLETED;
    }
}
