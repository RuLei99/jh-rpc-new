package com.rulei.rpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.rulei.rpc.JhRpcApplication;
import com.rulei.rpc.config.RpcConfig;
import com.rulei.rpc.loadbalancer.LoadBalancer;
import com.rulei.rpc.loadbalancer.LoadBalancerFactory;
import com.rulei.rpc.model.RpcRequest;
import com.rulei.rpc.model.RpcResponse;
import com.rulei.rpc.model.ServiceMetaInfo;
import com.rulei.rpc.register.Registry;
import com.rulei.rpc.register.RegistryFactory;
import com.rulei.rpc.serializer.JdkSerializer;
import com.rulei.rpc.serializer.Serializer;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //序列化器
        Serializer serializer = new JdkSerializer();
        RpcConfig rpcConfig = JhRpcApplication.getRpcConfig();
        Registry registry = RegistryFactory.getRegistry(
                rpcConfig.getRegistryConfig().getRegistry());

        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        String serviceName = method.getDeclaringClass().getName();
        serviceMetaInfo.setServiceName(serviceName);
        List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
        if(serviceMetaInfoList == null || CollUtil.isEmpty(serviceMetaInfoList)){
            throw new RuntimeException("未找到相关服务，serviceMetaInfoList为空");
        }
        // 负载均衡器
//        ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfoList.get(0);
        LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("methodName", method.getName());
        ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);
        String serviceAddress = selectedServiceMetaInfo.getServiceAddress();

        //发送请求
        //todo 学习建造者模式和蛮多的内容
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        try {
            byte[] rpcRequestForSer = serializer.serialize(rpcRequest);
            byte[] result = null;
            //todo try-with-resource用法的总结
            try (
                    //todo http请求
                    HttpResponse httpResponse = HttpRequest.post(serviceAddress)
                            .body(rpcRequestForSer)
                            .execute()) {
                result = httpResponse.bodyBytes();
            }
            //反序列化
            RpcResponse response = serializer.deserialize(result, RpcResponse.class);
            return response.getData();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
