package com.jhone_cmd.TodoList.users;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController()
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserRepository userRepository;

    @SuppressWarnings("null")
    @PostMapping("/")
    public UserModel createUser(@RequestBody UserModel userModel) {
        var userCreated = this.userRepository.save(userModel);
        return userCreated;
    }

}
