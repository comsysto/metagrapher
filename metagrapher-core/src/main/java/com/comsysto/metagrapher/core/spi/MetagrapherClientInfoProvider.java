package com.comsysto.metagrapher.core.spi;

import java.util.Map;
import java.util.Set;

public interface MetagrapherClientInfoProvider {

    Map<String, Set<MetagrapherClientInfo>> getClientsByApplicationName();
}
