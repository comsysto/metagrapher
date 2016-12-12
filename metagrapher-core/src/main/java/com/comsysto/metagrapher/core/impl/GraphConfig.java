package com.comsysto.metagrapher.core.impl;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.Map;

@Value
public class GraphConfig {

    public GraphConfig(@JsonProperty("nodeConfigs") Map<String, NodeConfig> nodeConfigs) {
        this.nodeConfigs = nodeConfigs;
    }

    private final Map<String, NodeConfig> nodeConfigs;
}
