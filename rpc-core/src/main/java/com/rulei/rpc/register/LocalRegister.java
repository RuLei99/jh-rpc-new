package com.rulei.rpc.register;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Rulei
 */
public class LocalRegister {
    private static final Map<String,Class<?>> MAP = new ConcurrentHashMap<>();

    //注册服务
    public static void register(String serviceName,Class<?> clazz){
        MAP.put(serviceName,clazz);
    }


    //获取服务
    public static Class<?> getService(String serviceName){
        return MAP.get(serviceName);
    }

    //删除服务
    public static void deleteService(String serviceName){
        MAP.remove(serviceName);
    }
}
