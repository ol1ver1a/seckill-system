package com.seckill.controller;

import com.seckill.common.Result;
import com.seckill.common.UserContext;
import com.seckill.pojo.User;
import com.seckill.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        return userService.login(params.get("username"), params.get("password"));
    }

    @PostMapping("/register")
    public Result<Void> register(@RequestBody User user) {
        return userService.register(user);
    }

    @GetMapping("/current")
    public Result<User> getCurrentUser() {
        return userService.getCurrentUser();
    }

    @PutMapping("/profile")
    public Result<Void> updateProfile(@RequestBody User user) {
        return userService.updateProfile(user);
    }

    @PutMapping("/password")
    public Result<Void> changePassword(@RequestBody Map<String, String> params) {
        return userService.changePassword(
                UserContext.getUserId(),
                params.get("oldPassword"),
                params.get("newPassword")
        );
    }
}
