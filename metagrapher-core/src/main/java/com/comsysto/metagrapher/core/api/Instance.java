package com.comsysto.metagrapher.core.api;

import lombok.Value;

@Value
public class Instance {
    private final String hostName;
    private final int port;
    private final String homePage;
    private final InstanceState state;
}
