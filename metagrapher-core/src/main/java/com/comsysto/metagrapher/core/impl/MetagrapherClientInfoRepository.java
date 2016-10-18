package com.comsysto.metagrapher.core.impl;

import com.comsysto.metagrapher.core.spi.MetagrapherClientInfo;
import com.comsysto.metagrapher.core.spi.MetagrapherClientInfoProvider;

public interface MetagrapherClientInfoRepository extends MetagrapherClientInfoProvider {

    void storeClientInfo(String application, String instanceId, MetagrapherClientInfo clientInfo);
    void deleteClientInfo(String application, String instanceId);

}
