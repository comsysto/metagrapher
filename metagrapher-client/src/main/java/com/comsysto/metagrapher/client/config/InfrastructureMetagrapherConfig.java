package com.comsysto.metagrapher.client.config;

import com.comsysto.metagrapher.client.MetagrapherPropertySource;
import com.comsysto.metagrapher.client.spi.MetagrapherServiceMetaDataExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
public class InfrastructureMetagrapherConfig {

    @Autowired()
    private List<MetagrapherServiceMetaDataExtractor> metagrapherServiceMetaDataExtractorList;

    @Autowired
    private ConfigurableEnvironment environment;

    @PostConstruct
    public void registerPropertySource(){
        MetagrapherPropertySource propertySource = new MetagrapherPropertySource();
        metagrapherServiceMetaDataExtractorList.forEach(propertySource::addExtractors);
        environment.getPropertySources().addLast(propertySource);
    }
}
