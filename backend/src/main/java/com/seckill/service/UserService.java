package com.seckill.service;

import com.seckill.common.Result;
import com.seckill.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    Result<Map<String, Object>> login(String username, String password);
    Result<Void> register(User user);
    Result<Void> updateProfile(User user);
    Result<Void> changePassword(Long userId, String oldPassword, String newPassword);
    Result<User> getCurrentUser();
    Result<List<User>> listUsers(Integer role, String keyword, int page, int size);
    Result<Void> updateUserStatus(Long id, Integer status);
    Result<Void> resetPassword(Long id);
}
