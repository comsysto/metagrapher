package com.comsysto.metagrapher.client.config;

import com.comsysto.metagrapher.client.MetagrapherMetaDataProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetagrapherClientConfig {

    @Bean
    public MetagrapherMetaDataProvider metagrapherMetaDataProvider() {
        return new MetagrapherMetaDataProvider();
    }
}
