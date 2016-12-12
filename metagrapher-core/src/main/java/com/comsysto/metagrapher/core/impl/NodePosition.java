package com.comsysto.metagrapher.core.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class NodePosition {
    private final double x;
    private final double y;

    public NodePosition(@JsonProperty("x") double x, @JsonProperty("y") double y) {
        this.x = x;
        this.y = y;
    }
}
