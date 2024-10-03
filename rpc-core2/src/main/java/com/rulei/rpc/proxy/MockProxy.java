package com.rulei.rpc.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Rulei
 */
@Slf4j
public class MockProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> returnType = method.getReturnType();
        log.info("启用Mock：" + method.getName());
        return getDefaultObject(returnType);
    }

    public Object getDefaultObject(Class<?> type) {
        if (type == boolean.class) {
            return true;
        } else if (type == int.class) {
            return 1;
        } else if (type == String.class) {
            return "gjh";
        }
        return null;
    }
}
