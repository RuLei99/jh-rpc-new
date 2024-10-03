package com.rulei.rpc.serializer;

import com.rulei.rpc.loader.SpiLoader;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Rulei
 */
@Slf4j
public class SerializerFactory {
    static {
        SpiLoader.load(Serializer.class);
    }

    /**
     * 默认序列化器
     */
    private static final Serializer DEFAULT_SERIALIZER = getSerializer(SerializerKeys.JDK);


    /**
     * 获取
     */
    public static Serializer getSerializer(String key) {
        return SpiLoader.getInstance(Serializer.class,key);
    }

    public static Serializer getSerializer() {
        return DEFAULT_SERIALIZER;
    }
}
