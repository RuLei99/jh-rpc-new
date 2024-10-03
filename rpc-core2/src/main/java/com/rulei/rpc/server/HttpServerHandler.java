package com.rulei.rpc.server;

import com.rulei.rpc.JhRpcApplication;
import com.rulei.rpc.model.RpcRequest;
import com.rulei.rpc.model.RpcResponse;
import com.rulei.rpc.register.LocalRegister;
import com.rulei.rpc.serializer.JdkSerializer;
import com.rulei.rpc.serializer.Serializer;
import com.rulei.rpc.serializer.SerializerFactory;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author Rulei
 */

public class HttpServerHandler implements Handler<io.vertx.core.http.HttpServerRequest> {

    @Override
    public void handle(io.vertx.core.http.HttpServerRequest request) {
        //todo final
        //序列化器
        final Serializer serializer = SerializerFactory.getSerializer(JhRpcApplication.getRpcConfig().getSerializer());
        //记录日志
        System.out.println("接受请求:" + request.uri() + request.method());
        //异步处理Http请求
        //响应
        request.bodyHandler(body -> {
            //请求对象（反序列化）
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest = null;
            try {
                rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //构建响应对象
            RpcResponse rpcResponse = new RpcResponse();
            //空处理
            if (rpcRequest == null) {
                rpcResponse.setMessage("请求为空");
                doResponse(request, rpcResponse, serializer);
                throw new RuntimeException("请求为空");
            }
            //初始化
            try {
                Class<?> service = LocalRegister.getService(rpcRequest.getServiceName());
                Method method = service.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                Object result = method.invoke(service.newInstance(), rpcRequest.getArgs());
                rpcResponse.setData(result);
                rpcResponse.setType(method.getReturnType());
                rpcResponse.setMessage("成功");

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            //响应
            doResponse(request, rpcResponse, serializer);
        });
    }

    /**
     * 响应
     *
     * @param request
     * @param rpcResponse
     * @param serializer
     */

    void doResponse(HttpServerRequest request, RpcResponse rpcResponse, Serializer serializer) {
        HttpServerResponse httpServerResponse = request.response().putHeader("content-type", "application/json");
        try {
            //序列化
            byte[] response = serializer.serialize(rpcResponse);
            //Buffer.buffer是vertx提供的用于处理字节流的类
            httpServerResponse.end(Buffer.buffer(response));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
