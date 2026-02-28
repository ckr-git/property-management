package com.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.community.entity.User;

/**
 * 用户服务接口
 * 
 * @author system
 */
public interface UserService extends IService<User> {
    
    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 用户信息
     */
    User login(String username, String password);
    
    /**
     * 用户注册
     * @param user 用户信息
     * @return 是否注册成功
     */
    boolean register(User user);
    
    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    User getUserByUsername(String username);

    /**
     * 修改密码
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否修改成功
     */
    boolean changePassword(Long userId, String oldPassword, String newPassword);

}