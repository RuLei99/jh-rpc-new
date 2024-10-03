package com.rulei.rpc.proxy;

import com.rulei.rpc.JhRpcApplication;

import java.lang.reflect.Proxy;

/**
 * @author Rulei
 */
public class MockProxyFactory {
    /**
     * 获取代理对象
     *
     * @param clazz
     * @return
     */
    public static Object getProxy(Class<?> clazz) {
        if (JhRpcApplication.getRpcConfig().isMock()) {
            return getMockProxy(clazz);
        }
        return Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[]{clazz},
                new ServiceProxy());
    }


    /**
     * 获取Nock对象
     *
     * @return
     */
    public static Object getMockProxy(Class<?> clazz) {
        return Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new MockProxy());
    }
}
