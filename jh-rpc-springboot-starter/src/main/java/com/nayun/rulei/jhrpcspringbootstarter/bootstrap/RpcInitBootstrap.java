package com.nayun.rulei.jhrpcspringbootstarter.bootstrap;

import com.nayun.rulei.jhrpcspringbootstarter.annotation.EnableJhRpc;
import com.rulei.rpc.JhRpcApplication;
import com.rulei.rpc.config.RpcConfig;
import com.rulei.rpc.server.VertxHttpServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author Rulei
 */
@Slf4j
public class RpcInitBootstrap implements ImportBeanDefinitionRegistrar {
    /**
     * Spring 初始化时执行，初始化 RPC 框架
     * * @param importingClassMetadata
     * * @param registry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 获取 EnableRpc 注解的属性值
        boolean needServer = (boolean) importingClassMetadata.getAnnotationAttributes(EnableJhRpc.class.getName())
                .get("startServer");

        // 框架初始化全局配置
        final RpcConfig rpcConfig = JhRpcApplication.getRpcConfig();

        // 启动服务器
        if (needServer) {
            VertxHttpServer vertxHttpServer = new VertxHttpServer();
            vertxHttpServer.doStart(JhRpcApplication.getRpcConfig().getServerPort());
        } else {
            log.info("未启动Vertx.只可消费");
        }

    }
}
