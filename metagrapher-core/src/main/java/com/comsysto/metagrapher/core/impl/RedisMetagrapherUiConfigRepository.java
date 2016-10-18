package com.comsysto.metagrapher.core.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class RedisMetagrapherUiConfigRepository implements MetagrapherUiConfigRepository {

    private final RedisTemplate<String, Map<String, Object>> redisTemplate;
    private final String uiConfigKey;

    @Override
    public void storeUiConfig(Map<String, Object> config) {
        redisTemplate.opsForValue().set(uiConfigKey, config);
    }

    @Override
    public Optional<Map<String, Object>> loadUiConfig() {
        return Optional.ofNullable(redisTemplate.opsForValue().get(uiConfigKey));
    }


}
