package com.rulei.rpc.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.rulei.rpc.JhRpcApplication;
import com.rulei.rpc.model.RpcRequest;
import com.rulei.rpc.model.RpcResponse;
import com.rulei.rpc.model.User;
import com.rulei.rpc.serializer.SerializerFactory;
import com.rulei.rpc.service.UserService;
import com.rulei.rpc.serializer.JdkSerializer;
import com.rulei.rpc.serializer.Serializer;

import java.io.IOException;

/**
 * @author Rulei
 */

public class UserServiceProxy implements UserService {
    @Override
    public User getUser(User user) {
        //序列化器
        Serializer serializer = SerializerFactory.getSerializer(JhRpcApplication.getRpcConfig().getSerializer());
        //发送请求
        //todo 学习建造者模式和蛮多的内容
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(UserService.class.getName())
                .methodName("getUser")
                .parameterTypes(new Class[]{User.class})
                .args(new Object[]{user})
                .build();
        try {
            byte[] rpcRequestForSer = serializer.serialize(rpcRequest);
            byte[] result = null;
            //todo try-with-resource用法的总结
            try (
                    //todo http请求
                    HttpResponse httpResponse = HttpRequest.post("localhost:8081")
                            .body(rpcRequestForSer)
                            .execute()) {
                result = httpResponse.bodyBytes();
            }
            //反序列化
            RpcResponse response = serializer.deserialize(result, RpcResponse.class);
            return (User) response.getData();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
