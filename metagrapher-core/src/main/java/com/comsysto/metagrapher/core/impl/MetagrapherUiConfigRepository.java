package com.comsysto.metagrapher.core.impl;

import java.util.Optional;

public interface MetagrapherUiConfigRepository {
    void storeUiConfig(GraphConfig config);

    Optional<GraphConfig> loadUiConfig();


}
