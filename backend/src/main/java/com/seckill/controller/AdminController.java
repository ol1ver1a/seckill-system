package com.seckill.controller;

import com.seckill.common.Result;
import com.seckill.common.UserContext;
import com.seckill.pojo.User;
import com.seckill.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public Result<List<User>> listUsers(
            @RequestParam(required = false) Integer role,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (UserContext.getUserRole() != 2) {
            return Result.error(403, "无管理员权限");
        }
        return userService.listUsers(role, keyword, page, size);
    }

    @PutMapping("/user/{id}/status")
    public Result<Void> updateUserStatus(@PathVariable Long id, @RequestParam Integer status) {
        if (UserContext.getUserRole() != 2) {
            return Result.error(403, "无管理员权限");
        }
        return userService.updateUserStatus(id, status);
    }

    @PutMapping("/user/{id}/reset-password")
    public Result<Void> resetPassword(@PathVariable Long id) {
        if (UserContext.getUserRole() != 2) {
            return Result.error(403, "无管理员权限");
        }
        return userService.resetPassword(id);
    }

    @PutMapping("/merchant/{id}/approve")
    public Result<Void> approveMerchant(@PathVariable Long id) {
        if (UserContext.getUserRole() != 2) {
            return Result.error(403, "无管理员权限");
        }
        return userService.updateUserStatus(id, 1);
    }
}
