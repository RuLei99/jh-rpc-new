package com.rulei.rpc.example;

import com.rulei.rpc.model.User;
import com.rulei.rpc.proxy.ServiceProxyFactory;
import com.rulei.rpc.proxy.UserServiceProxy;
import com.rulei.rpc.service.UserService;

/**
 * @author Rulei
 */
public class EasyConsumerExample {
    public static void main(String[] args) {

        //初始化
        UserService userService = (UserService) ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("Rulei测试");

        //调用
        User user1 = userService.getUser(user);
        if (user1 == null) {
            throw new RuntimeException("调用失败");
        } else {
            System.out.println(user1.getName());
        }
    }
}
