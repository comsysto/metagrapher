package com.comsysto.metagrapher.ui.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Optional;

@RequiredArgsConstructor
public class RedisMetagrapherUiConfigRepository implements MetagrapherUiConfigRepository {

    private final RedisTemplate<String, GraphConfig> redisTemplate;
    private final String uiConfigKey;

    @Override
    public void storeUiConfig(GraphConfig config) {
        redisTemplate.opsForValue().set(uiConfigKey, config);
    }

    @Override
    public Optional<GraphConfig> loadUiConfig() {
        return Optional.ofNullable(redisTemplate.opsForValue().get(uiConfigKey));
    }


}
