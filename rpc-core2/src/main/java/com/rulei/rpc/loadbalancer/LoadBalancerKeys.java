package com.rulei.rpc.loadbalancer;

/**
 * 负载均衡器键名常量
 * @author Rulei
 */
public interface LoadBalancerKeys {

    /**
     * 轮询
     */
    String ROUND_ROBIN = "roundRobin";

    String RANDOM = "random";

    String CONSISTENT_HASH = "consistentHash";

}
