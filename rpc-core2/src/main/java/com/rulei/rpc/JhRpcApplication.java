package com.rulei.rpc;

import com.rulei.rpc.config.RegistryConfig;
import com.rulei.rpc.config.RpcConfig;
import com.rulei.rpc.contant.RpcConstant;
import com.rulei.rpc.loader.ConfigLoader;
import com.rulei.rpc.register.Registry;
import com.rulei.rpc.register.RegistryFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Rulei
 */
@Slf4j
public class JhRpcApplication {
    private static volatile RpcConfig rpcConfig;

    //容器初始化
    //支持自定义配置文件
    public static void init(RpcConfig newRpcConfig) {
        rpcConfig = newRpcConfig;
        log.info("配置生效：" + rpcConfig.getServerHost() + ":" + rpcConfig.getServerPort());
    }

    public static void init() {
        RpcConfig rpcReadConfig = ConfigLoader.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        //加载失败使用默认值
        if (rpcReadConfig == null) {
//            throw new RuntimeException("rpcReadConfig配置读取失败");
            log.info("未找到配置文件，使用默认配置");
        }
        rpcConfig = rpcReadConfig;
//        log.info("当前服务启动地址：" + rpcConfig.getServerHost() +":"+rpcConfig.getServerPort());
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getRegistry(registryConfig.getRegistry());
        registry.init(registryConfig);
        log.info(registryConfig.getAddress()+"：注册中心初始化完成");
        // 创建并注册 Shutdown Hook，JVM 退出时执行操作
        Runtime.getRuntime().addShutdownHook(new Thread(registry::destory));
    }


    //获取配置
    //todo 双重检查锁定
    public static RpcConfig getRpcConfig() {
        if (rpcConfig == null) {
            synchronized (JhRpcApplication.class) {
                if (rpcConfig == null) {
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
