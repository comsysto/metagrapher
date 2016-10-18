package com.comsysto.metagrapher.core.impl;

import java.util.Map;
import java.util.Optional;

public interface MetagrapherUiConfigRepository {
    void storeUiConfig(Map<String, Object> config);

    Optional<Map<String,Object>> loadUiConfig();


}
