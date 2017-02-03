package com.comsysto.metagrapher.client;

import com.comsysto.metagrapher.annotations.MetagrapherService;
import com.comsysto.metagrapher.annotations.MetagrapherServiceType;
import com.comsysto.metagrapher.client.spi.MetagrapherMetaData;
import com.comsysto.metagrapher.client.spi.MetagrapherServiceMetaDataExtractor;
import com.comsysto.metagrapher.core.MetagrapherConstants;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class MetagrapherMetaDataProvider implements ApplicationContextAware {

    private final static String EXPORT_PROPERTY_PREFIX =  MetagrapherConstants.EXPORT_PREFIX;

    private final static String IMPORT_PROPERTY_PREFIX =  MetagrapherConstants.IMPORT_PREFIX;


    private volatile Map<String, String> collectedMetadata = Collections.emptyMap();
    private ApplicationContext applicationContext;
    private List<MetagrapherServiceMetaDataExtractor> extractors = new CopyOnWriteArrayList<>();

    public void addExtractors(MetagrapherServiceMetaDataExtractor extractor) {
        extractors.add(extractor);
    }

    @Scheduled(fixedDelay = 60000)
    public void refresh() {
        String[] beanNames = applicationContext.getBeanNamesForAnnotation(MetagrapherService.class);
        Map<String, String> properties = new LinkedHashMap<>();
        for (String beanName : beanNames) {
            Object bean = applicationContext.getBean(beanName);

            MetagrapherService annotation = AnnotationUtils.findAnnotation(bean.getClass(), MetagrapherService.class);
            if (annotation != null) {
                addProperties(properties, bean, annotation);
            }
        }
        collectedMetadata = Collections.unmodifiableMap(properties);
    }

    private void addProperties(Map<String, String> properties, Object bean, MetagrapherService annotation) {
        MetagrapherMetaData metaData = mergeMetaData(bean, metaDataFromAnnotation(annotation));
        Optional<String> propertyName = getPropertyName(annotation, metaData.getType());
        propertyName.ifPresent(name -> {
            addProperties(properties, name, metaData);
        });
    }

    private void addProperties(Map<String, String> properties, String name, MetagrapherMetaData metaData) {
        properties.put(name, Joiner.on(",").join(metaData.getTags()));
        for (Map.Entry<String, String> entry : metaData.getProperties().entrySet()) {
            properties.put(name + "." + entry.getKey(), entry.getValue());
        }
    }

    private MetagrapherMetaData metaDataFromAnnotation(MetagrapherService annotation) {
        return new MetagrapherMetaData(Sets.newHashSet(annotation.tags()), Collections.emptyMap(), annotation.type());
    }

    private MetagrapherMetaData mergeMetaData(Object bean, MetagrapherMetaData metaData) {
        return extractors.stream()
                .reduce(metaData,
                        (data, extractor) -> extractor.extractMetaData(bean, data),
                        MetagrapherMetaData::merge
                );

    }

    private Optional<String> getPropertyName(MetagrapherService annotation, MetagrapherServiceType type) {
        return propertyNameForImportOrExport(annotation.value(), type);

    }

    private Optional<String> propertyNameForImportOrExport(String serviceName, MetagrapherServiceType type) {
        if (type == MetagrapherServiceType.IMPORT) {
            return Optional.of(IMPORT_PROPERTY_PREFIX + serviceName);
        }

        if (type == MetagrapherServiceType.EXPORT) {
            return Optional.of(EXPORT_PROPERTY_PREFIX + serviceName);
        }

        return Optional.empty();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public Map<String, String> getCollectedMetaData(){
        return collectedMetadata;
    }
}
