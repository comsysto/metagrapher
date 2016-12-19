package com.comsysto.metagrapher.ui.job;

import com.comsysto.metagrapher.ui.impl.MetagrapherClientInfoRepository;
import com.comsysto.metagrapher.ui.spi.MetagrapherClientInfo;
import com.comsysto.metagrapher.ui.spi.MetagrapherClientInfoProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public class FetchJob {

    private final MetagrapherClientInfoRepository repository;
    private final MetagrapherClientInfoProvider remoteClientInfoProvider;


    @Scheduled(fixedDelayString = "${fetchClientInfo.interval}")
    public void fetch(){
        Map<String, Set<MetagrapherClientInfo>> clientsByName =
                remoteClientInfoProvider.getClientsByApplicationName();

        for (Map.Entry<String, Set<MetagrapherClientInfo>> entry : clientsByName.entrySet()) {
            String applicationName = entry.getKey();

            for (MetagrapherClientInfo clientInfo : entry.getValue()) {
                repository.storeClientInfo(applicationName, clientInfo.getId(), clientInfo);
            }
        }

    }
}
