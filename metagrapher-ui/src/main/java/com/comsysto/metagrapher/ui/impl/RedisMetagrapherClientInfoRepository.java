package com.comsysto.metagrapher.ui.impl;

import com.comsysto.metagrapher.ui.spi.MetagrapherClientInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

@RequiredArgsConstructor
public class RedisMetagrapherClientInfoRepository implements MetagrapherClientInfoRepository{

    private final RedisTemplate<String, MetagrapherClientInfo> redisTemplate;
    private final String redisKey;

    @Override
    public void storeClientInfo(String application, String instanceId, MetagrapherClientInfo clientInfo) {
        redisTemplate.opsForHash().put(redisKey, mapKey(application, instanceId), clientInfo);
    }

    private String mapKey(String application, String instanceId) {
        return application + ":" + instanceId;
    }

    @Override
    public void deleteClientInfo(String application, String instanceId) {
        redisTemplate.opsForHash().delete(redisKey, mapKey(application, instanceId));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Set<MetagrapherClientInfo>> getClientsByApplicationName() {
        return  redisTemplate.opsForHash().values(redisKey)
                .stream()
                .map(MetagrapherClientInfo.class::cast)
                .collect(groupingBy(MetagrapherClientInfo::getApplicationName, toSet()));

    }



}
