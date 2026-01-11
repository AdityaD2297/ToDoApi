package com.application.todoapi.controller;

import com.application.todoapi.common.request.ToDoRequest;
import com.application.todoapi.common.response.ToDoResponse;
import com.application.todoapi.entity.ToDo;
import com.application.todoapi.service.ToDoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/todo")
@Tag(name = "ToDo Management", description = "CRUD operations for managing ToDo items")
@SecurityRequirement(name = "Bearer Authentication")
public class ToDoController {

    private final ToDoService toDoService;

    public ToDoController(ToDoService toDoService) {
        this.toDoService = toDoService;
    }

    @PostMapping
    @Operation(summary = "Create a new ToDo item", description = "Create a new ToDo item for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "ToDo item created successfully"),
        @ApiResponse(responseCode = "400", description = "Validation errors"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ToDoResponse> createToDo(@RequestBody ToDoRequest toDo) {
        return new ResponseEntity<>(toDoService.createToDo(toDo), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all ToDo items", description = "Get all ToDo items for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of ToDo items retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Page<ToDoResponse>> getAllToDos(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) ToDo.Status status,
            @RequestParam(required = false) ToDo.Priority priority,
            @RequestParam(required = false) LocalDateTime dueDate,
            @RequestParam(required = false) Boolean completed,
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(toDoService.getAllLists(search, status, priority, completed, dueDate, userId, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ToDo item by id", description = "Get ToDo item by id for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "ToDo item retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ToDoResponse> getToDoById(@PathVariable Long id) {
        return new ResponseEntity<>(toDoService.getToDoById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update ToDo item by id", description = "Update ToDo item by id for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "ToDo item updated successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ToDoResponse> updateToDo(@PathVariable Long id, @RequestBody ToDoRequest toDo) {
        return new ResponseEntity<>(toDoService.updateToDo(id, toDo), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete ToDo item by id", description = "Delete ToDo item by id for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "ToDo item deleted successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Void> deleteToDo(@PathVariable Long id) {
        toDoService.deleteToDo(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Patch ToDo item by id", description = "Patch ToDo item by id for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "ToDo item patched successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ToDoResponse> patchToDo(@PathVariable Long id, @RequestBody ToDoRequest toDo) {
        return new ResponseEntity<>(toDoService.patchToDo(id, toDo), HttpStatus.OK);
    }
}
