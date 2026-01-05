package com.application.todoapi.controller;

import com.application.todoapi.common.request.ToDoRequest;
import com.application.todoapi.common.response.ToDoResponse;
import com.application.todoapi.entity.ToDo;
import com.application.todoapi.service.ToDoService;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/todo")
@NoArgsConstructor
public class ToDoController {

    private ToDoService toDoService;

    public ToDoController(ToDoService toDoService) {
        this.toDoService = toDoService;
    }

    @PostMapping
    public ResponseEntity<ToDoResponse> createToDo(@RequestBody ToDoRequest toDo) {
        return new ResponseEntity<>(toDoService.createToDo(toDo), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<ToDoResponse>> getAllToDos(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) ToDo.Status status,
            @RequestParam(required = false) ToDo.Priority priority,
            @RequestParam(required = false) Boolean completed,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(toDoService.getAllLists(search, status, priority, completed, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ToDoResponse> getToDoById(@PathVariable Long id) {
        return new ResponseEntity<>(toDoService.getToDoById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ToDoResponse> updateToDo(@PathVariable Long id, @RequestBody ToDoRequest toDo) {
        return new ResponseEntity<>(toDoService.updateToDo(id, toDo), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteToDo(@PathVariable Long id) {
        toDoService.deleteToDo(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ToDoResponse> patchToDo(@PathVariable Long id, @RequestBody ToDoRequest toDo) {
        return new ResponseEntity<>(toDoService.patchToDo(id, toDo), HttpStatus.OK);
    }
}
