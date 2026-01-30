package com.jhone_cmd.TodoList.tasks;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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
        var task = taskRepository.save(taskModel);

        var currentDate = LocalDateTime.now();

        if (currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(400).body("Task is not in the correct time range");
        }

        if (taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(400).body("Task start time must be before end time");
        }

        return ResponseEntity.status(201).body(task);
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
            return ResponseEntity.status(404).body("Task not found");
        }

        Utils.copyNullProperties(taskModel, task);

        var taskUpdated = taskRepository.save(task);
        return ResponseEntity.status(200).body(taskUpdated);
    }
}
