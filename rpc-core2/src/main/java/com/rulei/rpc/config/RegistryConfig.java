package com.rulei.rpc.config;

import lombok.Data;

/**
 * @author Rulei
 */
@Data
public class RegistryConfig {
    /**
     * 注册服务容器
     */
    private String registry = "etcd";
    /**
     * 地址
     */
    private String address = "http://localhost:2380";
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 超时时间
     */
    private long timeout = 1000L;

}
