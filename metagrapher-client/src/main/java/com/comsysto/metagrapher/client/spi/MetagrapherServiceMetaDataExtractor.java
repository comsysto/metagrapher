package com.comsysto.metagrapher.client.spi;

public interface MetagrapherServiceMetaDataExtractor {

    MetagrapherMetaData extractMetaData(Object bean, MetagrapherMetaData metaData);

}
