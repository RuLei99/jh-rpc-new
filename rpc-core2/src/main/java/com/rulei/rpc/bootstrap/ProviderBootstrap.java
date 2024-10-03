package com.rulei.rpc.bootstrap;

import com.rulei.rpc.JhRpcApplication;
import com.rulei.rpc.config.RegistryConfig;
import com.rulei.rpc.config.RpcConfig;
import com.rulei.rpc.model.ServiceMetaInfo;
import com.rulei.rpc.model.ServiceRegisterInfo;
import com.rulei.rpc.register.LocalRegister;
import com.rulei.rpc.register.Registry;
import com.rulei.rpc.register.RegistryFactory;
import com.rulei.rpc.server.VertxHttpServer;

import java.util.List;

/**
 * @author Rulei
 */
public class ProviderBootstrap {
    public static void init(List<ServiceRegisterInfo<?>> serviceRegisterInfoList) {
        //遍历serviceRegisterInfoList
        for (ServiceRegisterInfo<?> serviceRegisterInfo : serviceRegisterInfoList) {
            //注册服务本地
            LocalRegister.register(serviceRegisterInfo.getServiceName(), serviceRegisterInfo.getImplClass());
            //JhRpcApplication初始化
            RpcConfig rpcConfig = JhRpcApplication.getRpcConfig();
            //注册服务到服务中心，开启心跳检测
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceRegisterInfo.getServiceName());
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            Registry registry = RegistryFactory.getRegistry(registryConfig.getRegistry());

            try {
                registry.register(serviceMetaInfo);
                registry.heartBeat();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        //启动web
        VertxHttpServer vertxHttpServer = new VertxHttpServer();
        vertxHttpServer.doStart(JhRpcApplication.getRpcConfig().getServerPort());

    }
}
