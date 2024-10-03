package com.rulei.rpc.config;

import com.rulei.rpc.loadbalancer.LoadBalancerKeys;
import com.rulei.rpc.serializer.SerializerKeys;
import lombok.Data;

/**
 * @author Rulei
 */
@Data
public class RpcConfig {
    /**
     * 名称
     */
    private String name = "jh-rpc";

    /**
     * 版本号
     */
    private String version = "2.0";

    /**
     * 服务器主机名
     */
    private String serverHost = "localhost";

    /**
     * 服务器端口号
     */
    private Integer serverPort = 8090;

    /**
     * Mock开关
     */
    private boolean mock = false;

    /**
     * 序列化器
     */
    private String serializer = SerializerKeys.JDK;

    /**
     * 注册中心配置
     */
    private RegistryConfig registryConfig = new RegistryConfig();

    /**
     * 负载均衡器
     */
    private String loadBalancer = LoadBalancerKeys.CONSISTENT_HASH;


}
