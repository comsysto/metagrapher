package com.comsysto.metagrapher.ui.impl;

import com.comsysto.metagrapher.ui.spi.MetagrapherClientInfo;
import com.comsysto.metagrapher.ui.spi.MetagrapherClientInfoProvider;

public interface MetagrapherClientInfoRepository extends MetagrapherClientInfoProvider {

    void storeClientInfo(String application, String instanceId, MetagrapherClientInfo clientInfo);
    void deleteClientInfo(String application, String instanceId);

}
