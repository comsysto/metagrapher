package com.comsysto.metagrapher.ui.spi;

import java.util.Map;
import java.util.Set;

public interface MetagrapherClientInfoProvider {

    Map<String, Set<MetagrapherClientInfo>> getClientsByApplicationName();
}
