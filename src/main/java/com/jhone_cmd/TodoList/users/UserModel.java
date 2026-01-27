package com.jhone_cmd.TodoList.users;

import java.util.UUID;

import lombok.Data;

@Data
public class UserModel {
    private UUID id;
    private String name;
    private String email;
    private String password;
}
