package com.rulei.rpc.register;

import com.rulei.rpc.loader.SpiLoader;

/**
 * @author Rulei
 */
public class RegistryFactory {
    static {
        SpiLoader.load(Registry.class);
    }

    private static final Registry DEFAULT_REGISTRY = getRegistry(RegistryKey.ETCD);

    public static Registry getRegistry() {
        return DEFAULT_REGISTRY;
    }

    public static Registry getRegistry(String key) {
        return SpiLoader.getInstance(Registry.class, key);
    }
}
