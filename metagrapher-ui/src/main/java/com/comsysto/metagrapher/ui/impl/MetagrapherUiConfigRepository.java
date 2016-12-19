package com.comsysto.metagrapher.ui.impl;

import java.util.Optional;

public interface MetagrapherUiConfigRepository {
    void storeUiConfig(GraphConfig config);

    Optional<GraphConfig> loadUiConfig();


}
