package com.rulei.rpc.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.rulei.rpc.model.RpcRequest;
import com.rulei.rpc.model.RpcResponse;
import com.rulei.rpc.util.JdkSerializer;
import com.rulei.rpc.util.Serializer;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //序列化器
        Serializer serializer = new JdkSerializer();
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
                    HttpResponse httpResponse = HttpRequest.post("localhost:8080")
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
