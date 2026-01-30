package com.jhone_cmd.TodoList.tasks;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhone_cmd.TodoList.Utils.Utils;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity<Object> create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        taskModel.setIdUser((UUID) idUser);

        var currentDate = LocalDateTime.now();

        if (currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task is not in the correct time range");
        }

        if (taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task start time must be before end time");
        }

        var task = taskRepository.save(taskModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @GetMapping("/")
    public Iterable<TaskModel> list(HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        var tasks = taskRepository.findByIdUser((UUID) idUser);
        return tasks;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@RequestBody TaskModel taskModel, HttpServletRequest request,
            @PathVariable UUID id) {
        var task = taskRepository.findById(id).orElse(null);

        if (task == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task not found");
        }

        var idUser = request.getAttribute("idUser");

        if (!task.getIdUser().equals(idUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to update this task");
        }

        Utils.copyNullProperties(taskModel, task);

        var taskUpdated = taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.OK).body(taskUpdated);
    }
}
