package com.rulei.rpc.proxy;

import java.lang.reflect.Proxy;

/**
 * @author Rulei
 */
public class ServiceProxyFactory {
    public static Object getProxy(Class<?> clazz) {
        return Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[]{clazz},
                new ServiceProxy());
    }
}
