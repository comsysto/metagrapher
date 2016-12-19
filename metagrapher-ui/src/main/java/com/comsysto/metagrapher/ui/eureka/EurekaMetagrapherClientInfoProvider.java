package com.comsysto.metagrapher.ui.eureka;

import com.comsysto.metagrapher.ui.api.InstanceState;
import com.comsysto.metagrapher.ui.spi.MetagrapherClientInfo;
import com.comsysto.metagrapher.ui.spi.MetagrapherClientInfoProvider;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class EurekaMetagrapherClientInfoProvider implements MetagrapherClientInfoProvider {

    private final EurekaClient client;
    private final String region;

    @Override
    public Map<String, Set<MetagrapherClientInfo>> getClientsByApplicationName() {
        Optional<Applications> applications = Optional.ofNullable(client.getApplications());

        Map<String, Set<MetagrapherClientInfo>> clientInfosForApplicationName = applications
                .map(Applications::getRegisteredApplications)
                .orElse(Collections.emptyList())
                .stream()
                .collect(Collectors.toMap(Application::getName, this::loadClients));

        return clientInfosForApplicationName;

    }

    public Set<MetagrapherClientInfo> loadClients(Application application) {
        Set<MetagrapherClientInfo> clientInfos = application.getInstancesAsIsFromEureka()
                .stream()
                .map(instance -> new MetagrapherClientInfo(
                        application.getName(),
                        instance.getInstanceId(),
                        instance.getHostName(),
                        instance.getPort(),
                        instance.getHomePageUrl(),
                        mapStatus(instance),
                        new TreeMap<>(instance.getMetadata())
                ))
                .collect(Collectors.toSet());

        return clientInfos;
    }

    private InstanceState mapStatus(InstanceInfo instance) {
        switch (instance.getStatus()) {
            case DOWN:
                return InstanceState.DOWN;
            case OUT_OF_SERVICE:
                return InstanceState.OUT_OF_SERVICE;
            case STARTING:
                return InstanceState.STARTING;
            case UP:
                return InstanceState.UP;
            case UNKNOWN:
                return InstanceState.UNKNOWN;
            default:
                throw new IllegalStateException("Unknown status: " + instance.getStatus());
        }
    }

}
