package com.jhone_cmd.TodoList.tasks;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ITaskRepository extends JpaRepository<TaskModel, UUID> {
    Iterable<TaskModel> findByIdUser(UUID idUser);
}
