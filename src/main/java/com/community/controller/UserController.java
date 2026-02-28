package com.community.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.community.common.Result;
import com.community.dto.ChangePasswordDTO;
import com.community.entity.User;
import com.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public Result<IPage<User>> listUsers(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        Page<User> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(User::getUsername, keyword)
                    .or().like(User::getRealName, keyword)
                    .or().like(User::getPhone, keyword));
        }
        wrapper.orderByDesc(User::getCreateTime);
        IPage<User> result = userService.page(page, wrapper);
        result.getRecords().forEach(u -> u.setPassword(null));
        return Result.success(result);
    }

    @PostMapping("/login")
    public Result<User> login(@RequestParam String username, @RequestParam String password) {
        User user = userService.login(username, password);
        if (user != null) {
            // 隐藏密码
            user.setPassword(null);
            return Result.success("登录成功", user);
        } else {
            return Result.error("用户名或密码错误");
        }
    }
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<User> register(@RequestBody User user) {
        boolean success = userService.register(user);
        if (success) {
            user.setPassword(null); // 隐藏密码
            return Result.success("注册成功", user);
        } else {
            return Result.error("注册失败，用户名已存在");
        }
    }
    
    /**
     * 获取用户信息
     */
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user != null) {
            user.setPassword(null); // 隐藏密码
            return Result.success(user);
        } else {
            return Result.error("用户不存在");
        }
    }
    
    /**
     * 更新用户信息（仅允许修改安全字段）
     */
    @PutMapping("/{id}")
    public Result<String> updateUser(@PathVariable Long id, @RequestBody User user) {
        User existing = userService.getById(id);
        if (existing == null) {
            return Result.error("用户不存在");
        }
        // 只允许修改安全字段，仅更新非空值
        if (user.getRealName() != null) existing.setRealName(user.getRealName());
        if (user.getPhone() != null) existing.setPhone(user.getPhone());
        if (user.getIdCard() != null) existing.setIdCard(user.getIdCard());
        boolean success = userService.updateById(existing);
        if (success) {
            return Result.success("更新成功");
        } else {
            return Result.error("更新失败");
        }
    }

    /**
     * 修改密码
     */
    @PutMapping("/{id}/password")
    public Result<String> changePassword(@PathVariable Long id, @RequestBody ChangePasswordDTO dto) {
        if (dto.getOldPassword() == null || dto.getOldPassword().isEmpty()) {
            return Result.error("请输入原密码");
        }
        if (dto.getNewPassword() == null || dto.getNewPassword().length() < 6) {
            return Result.error("新密码至少6位");
        }
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            return Result.error("两次密码不一致");
        }
        boolean success = userService.changePassword(id, dto.getOldPassword(), dto.getNewPassword());
        if (success) {
            return Result.success("密码修改成功");
        } else {
            return Result.error("原密码错误");
        }
    }

}