package com.comsysto.metagrapher.client.config;

import com.comsysto.metagrapher.client.MetagrapherMetaDataProvider;
import com.comsysto.metagrapher.client.spi.MetagrapherServiceMetaDataExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
public class MetagrapherRegisterExtractorConfig {

    @Autowired
    private List<MetagrapherServiceMetaDataExtractor> metagrapherServiceMetaDataExtractorList;

    @Autowired
    private MetagrapherMetaDataProvider metaDataProvider;

    @PostConstruct
    public void registerMetaDataExtractors(){
        metagrapherServiceMetaDataExtractorList.forEach(metaDataProvider::addExtractors);
    }

}
