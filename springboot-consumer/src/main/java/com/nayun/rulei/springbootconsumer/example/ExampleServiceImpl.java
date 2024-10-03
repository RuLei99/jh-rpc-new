package com.nayun.rulei.springbootconsumer.example;

import com.nayun.rulei.jhrpcspringbootstarter.annotation.JhRpcReference;
import com.rulei.rpc.model.User;
import com.rulei.rpc.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author Rulei
 */
@Service
public class ExampleServiceImpl {

    @JhRpcReference
    private UserService userService;

    public void test() {
        User user = new User();
        user.setName("yupi");
        User resultUser = userService.getUser(user);
        System.out.println(resultUser.getName());
    }

}
