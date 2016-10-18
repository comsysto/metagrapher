package com.comsysto.metagrapher.core;

import com.comsysto.metagrapher.core.spi.MetagrapherClientInfo;
import com.comsysto.metagrapher.core.spi.MetagrapherClientInfoProvider;
import com.comsysto.metagrapher.core.impl.MetagrapherClientInfoRepository;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public class RepositoryMetagrapherClientInfoProvider implements MetagrapherClientInfoProvider {

    private final MetagrapherClientInfoRepository repository;

    @Override
    public Map<String, Set<MetagrapherClientInfo>> getClientsByApplicationName() {
        return repository.getClientsByApplicationName();
    }
}
