package com.rulei.rpc.register;

import com.rulei.rpc.config.RegistryConfig;
import com.rulei.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author Rulei
 * 注册中心接口
 */

public interface Registry {
    /**
     * 初始化
     */
    void init(RegistryConfig registryConfig);

    /**
     * 服务注册
     */
    void register(ServiceMetaInfo serviceMetaInfo) throws ExecutionException, InterruptedException;

    /**
     * 服务注销
     */

    void deRegister(ServiceMetaInfo serviceMetaInfo);

    /**
     * 服务发现
     */
    List<ServiceMetaInfo> serviceDiscovery(String serviceKey) throws ExecutionException, InterruptedException;

    /**
     * 服务销毁
     */

    void destory();

    /**
     * 心跳检测
     */
    void heartBeat();
}
