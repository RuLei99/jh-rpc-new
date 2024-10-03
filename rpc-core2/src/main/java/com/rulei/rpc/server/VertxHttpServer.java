package com.rulei.rpc.server;

import io.vertx.core.Vertx;

/**
 * @author Rulei
 */
public class VertxHttpServer implements HttpServer{
    @Override
    public void doStart(int port) {
        //创建Vertx容器
        Vertx vertx = Vertx.vertx();
        //创建Http服务
        io.vertx.core.http.HttpServer httpServer = vertx.createHttpServer();
        //绑定处理请求
        httpServer.requestHandler(new HttpServerHandler());
        //监听端口
        httpServer.listen(port,result->{
            if (result.succeeded()) {
                System.out.println("目前正在监听："+port);
            }else {
                System.out.println("监听失败"+result.cause());
            }
        });


    }
}
