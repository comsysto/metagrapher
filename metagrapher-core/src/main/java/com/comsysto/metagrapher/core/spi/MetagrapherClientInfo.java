package com.comsysto.metagrapher.core.spi;

import com.comsysto.metagrapher.core.api.InstanceState;
import lombok.Value;

import java.util.Map;


@Value
public class MetagrapherClientInfo {

    private final String applicationName;
    private final String id;
    private final String hostName;
    private final int port;
    private final String homePageUrl;
    private final InstanceState state;
    private final Map<String, String> metadata;


}
