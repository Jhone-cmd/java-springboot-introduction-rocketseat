package com.jhone_cmd.TodoList.users;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController()
@RequestMapping("/users")
public class UserController {

    @PostMapping("/")
    public void createUser(@RequestBody UserModel userModel) {
        System.out.println(userModel.getName());
    }

}
