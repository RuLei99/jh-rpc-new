package com.nayun.rulei.springbootprovider.service.impl;

import com.nayun.rulei.jhrpcspringbootstarter.annotation.JhRpcService;
import com.rulei.rpc.model.User;
import com.rulei.rpc.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author Rulei
 */
@Service
@JhRpcService
public class UserServiceImpl implements UserService {


    /**
     * 获取用户信息
     * @param user 用户
     * @return user
     */
    @Override
    public User getUser(User user) {
        System.out.println("用户名：" + user.getName());
        System.out.println("生产者提供");
        user.setName("生产者提供/Rulei");
        return user;
    }
}
