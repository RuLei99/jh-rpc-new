package com.rulei.rpc.register;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import com.rulei.rpc.config.RegistryConfig;
import com.rulei.rpc.model.ServiceMetaInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author Rulei
 */
@Slf4j
public class EtcdRegistry implements Registry {
    private Client client;

    private KV kvClient;

    private static final String RPC_ROOT_PATH = "/rpc";

    /**
     * 本机注册的节点 key 集合（用于维护续期）
     */
    private final Set<String> localRegisterNodeKeySet = new HashSet<>();

    @Override
    public void init(RegistryConfig registryConfig) {
        client = Client.builder().endpoints(registryConfig.getAddress()).connectTimeout(Duration.ofMillis(registryConfig.getTimeout())).build();
        kvClient = client.getKVClient();
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws ExecutionException, InterruptedException {
        Lease leaseClient = client.getLeaseClient();
//        String registerKey = String.format("%s/%s", RPC_ROOT_PATH, serviceMetaInfo.getServiceKey());
        ByteSequence key = ByteSequence.from(serviceMetaInfo.getServiceKey(), StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);
        //60s*5过期
        long leaseId = leaseClient.grant(60 * 5).get().getID();
        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
        kvClient.put(key, value, putOption).get();
        localRegisterNodeKeySet.add(serviceMetaInfo.getServiceKey());
    }

    @Override
    public void deRegister(ServiceMetaInfo serviceMetaInfo) {
//        String registerKey = String.format("%s%s", RPC_ROOT_PATH, serviceMetaInfo.getServiceKey());
        ByteSequence key = ByteSequence.from(serviceMetaInfo.getServiceKey(), StandardCharsets.UTF_8);
        kvClient.delete(key);
        localRegisterNodeKeySet.remove(serviceMetaInfo.getServiceKey());
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) throws ExecutionException, InterruptedException {
        //启用前缀查询
        try {
            GetOption getOption = GetOption.builder().isPrefix(true).build();
//            String registerKey = String.format("%s%s/", RPC_ROOT_PATH, serviceKey);
            ByteSequence key = ByteSequence.from(serviceKey, StandardCharsets.UTF_8);
            List<KeyValue> valuesList = kvClient.get(key, getOption).get().getKvs();
            //json转对象
            return valuesList.stream().map(keyValue -> {
                String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                return JSONUtil.toBean(value, ServiceMetaInfo.class);
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("获取服务列表失败", e);
        }
    }


    @Override
    public void destory() {

        for (String key : localRegisterNodeKeySet) {
            try {
                kvClient.delete(ByteSequence.from(key, StandardCharsets.UTF_8)).get();
                System.out.println(key+"节点下线");
            } catch (Exception e) {
                throw new RuntimeException(key + "节点下线失败");
            }
        }

        // 释放资源
        if (kvClient != null) {
            kvClient.close();
        }
        if (client != null) {
            client.close();
        }
    }

    @Override
    public void heartBeat() {
        CronUtil.schedule("*/10 * * * * *", new Task() {
            @Override
            public void execute() {
                for (String key : localRegisterNodeKeySet) {
                    try {
                        List<KeyValue> keyValues = kvClient.get(ByteSequence.from(key, StandardCharsets.UTF_8))
                                .get()
                                .getKvs();
                        //未过期
                        if (!CollUtil.isEmpty(keyValues)) {
                            KeyValue keyValue = keyValues.get(0);
                            String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                            ServiceMetaInfo serviceMetaInfo = JSONUtil.toBean(value, ServiceMetaInfo.class);
                            register(serviceMetaInfo);
                        }
                    } catch (Exception e) {
                        log.error(KeyValue.class + "续签失败");
                        throw new RuntimeException(e);
                    }
                }

            }
        });
        // 支持秒级别定时任务
        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }
}
