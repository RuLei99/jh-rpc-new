package com.rulei.rpc.example;

import com.rulei.rpc.JhRpcApplication;
import com.rulei.rpc.bootstrap.ProviderBootstrap;
import com.rulei.rpc.config.RegistryConfig;
import com.rulei.rpc.config.RpcConfig;
import com.rulei.rpc.model.ServiceMetaInfo;
import com.rulei.rpc.model.ServiceRegisterInfo;
import com.rulei.rpc.register.LocalRegister;
import com.rulei.rpc.register.Registry;
import com.rulei.rpc.register.RegistryFactory;
import com.rulei.rpc.server.VertxHttpServer;
import com.rulei.rpc.service.UserService;
import com.rulei.rpc.service.impl.UserServiceImpl;

import java.util.ArrayList;

/**
 * @author Rulei
 */
public class EasyProviderExample {
    public static void main(String[] args) {

        ArrayList<ServiceRegisterInfo<?>> serviceRegisterInfoList = new ArrayList<>();
        ServiceRegisterInfo<UserServiceImpl> serviceRegisterInfo = new ServiceRegisterInfo<>();
        serviceRegisterInfo.setServiceName(UserService.class.getName());
        serviceRegisterInfo.setImplClass(UserServiceImpl.class);
        serviceRegisterInfoList.add(serviceRegisterInfo);
        //rpc初始化
        ProviderBootstrap.init(serviceRegisterInfoList);

    }
}
