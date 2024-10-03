package com.nayun.rulei.springbootconsumer;

import com.nayun.rulei.jhrpcspringbootstarter.annotation.EnableJhRpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableJhRpc(startServer = false)
public class SpringbootConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootConsumerApplication.class, args);
    }

}
