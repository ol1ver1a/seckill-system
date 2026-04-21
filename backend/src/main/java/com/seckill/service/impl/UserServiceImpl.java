package com.seckill.service.impl;

import com.seckill.common.Result;
import com.seckill.common.UserContext;
import com.seckill.config.JwtConfig;
import com.seckill.mapper.UserMapper;
import com.seckill.pojo.User;
import com.seckill.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final JwtConfig jwtConfig;

    public UserServiceImpl(UserMapper userMapper, JwtConfig jwtConfig) {
        this.userMapper = userMapper;
        this.jwtConfig = jwtConfig;
    }

    @Override
    public Result<Map<String, Object>> login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            return Result.error(401, "用户不存在");
        }
        if (user.getStatus() == 0) {
            if (user.getRole() == 1) {
                return Result.error(403, "商家账号待审核，请等待管理员审核通过");
            }
            return Result.error(403, "该账号已被禁用");
        }
        String md5 = DigestUtils.md5Hex(password + user.getSalt());
        if (!md5.equals(user.getPassword())) {
            return Result.error(401, "密码错误");
        }
        String token = jwtConfig.generateToken(user.getId(), user.getUsername(), user.getRole());
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", sanitizeUser(user));
        return Result.success(data);
    }

    @Override
    public Result<Void> register(User user) {
        if (userMapper.findByUsername(user.getUsername()) != null) {
            return Result.error(400, "用户名已存在");
        }
        String salt = RandomStringUtils.randomAlphanumeric(8);
        user.setSalt(salt);
        user.setPassword(DigestUtils.md5Hex(user.getPassword() + salt));
        user.setStatus(1);
        if (user.getRole() == null) user.setRole(0);
        // 商家注册需要管理员审核，初始状态为0(待审核)
        if (user.getRole() == 1) {
            user.setStatus(0);
        }
        userMapper.insert(user);
        return Result.success();
    }

    @Override
    public Result<Void> updateProfile(User user) {
        user.setId(UserContext.getUserId());
        userMapper.update(user);
        return Result.success();
    }

    @Override
    public Result<Void> changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.findById(userId);
        if (user == null) return Result.error(404, "用户不存在");
        String oldMd5 = DigestUtils.md5Hex(oldPassword + user.getSalt());
        if (!oldMd5.equals(user.getPassword())) {
            return Result.error(400, "原密码错误");
        }
        String newSalt = RandomStringUtils.randomAlphanumeric(8);
        String newMd5 = DigestUtils.md5Hex(newPassword + newSalt);
        userMapper.updatePassword(userId, newMd5, newSalt);
        return Result.success();
    }

    @Override
    public Result<User> getCurrentUser() {
        User user = userMapper.findById(UserContext.getUserId());
        if (user == null) return Result.error(404, "用户不存在");
        return Result.success(sanitizeUser(user));
    }

    @Override
    public Result<List<User>> listUsers(Integer role, String keyword, int page, int size) {
        List<User> users = userMapper.findByRole(role, keyword, (page - 1) * size, size);
        users.forEach(this::sanitizeUser);
        return Result.success(users);
    }

    @Override
    public Result<Void> updateUserStatus(Long id, Integer status) {
        userMapper.updateStatus(id, status);
        return Result.success();
    }

    @Override
    public Result<Void> resetPassword(Long id) {
        String salt = RandomStringUtils.randomAlphanumeric(8);
        String password = DigestUtils.md5Hex("123456" + salt);
        userMapper.updatePassword(id, password, salt);
        return Result.success();
    }

    private User sanitizeUser(User user) {
        user.setPassword(null);
        user.setSalt(null);
        return user;
    }
}
