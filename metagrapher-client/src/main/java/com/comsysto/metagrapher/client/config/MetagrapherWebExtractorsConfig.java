package com.comsysto.metagrapher.client.config;

import com.comsysto.metagrapher.client.spi.MetagrapherServiceMetaDataExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import static com.comsysto.metagrapher.annotations.MetagrapherServiceType.EXPORT;

@Configuration
public class MetagrapherWebExtractorsConfig {


    @Bean
    public MetagrapherServiceMetaDataExtractor controllerExtractor() {
        return (bean, metaData) -> {

            Class<?> beanClass = bean.getClass();
            if (beanClass.isAnnotationPresent(Controller.class) || beanClass.isAnnotationPresent(RestController.class)) {
                metaData = metaData.type(EXPORT);
            }

            return metaData;
        };
    }
}
