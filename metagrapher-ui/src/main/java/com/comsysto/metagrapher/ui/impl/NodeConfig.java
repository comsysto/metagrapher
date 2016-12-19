package com.comsysto.metagrapher.ui.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class NodeConfig {
    private final NodePosition position;

    public NodeConfig(@JsonProperty("position") NodePosition position) {
        this.position = position;
    }
}
