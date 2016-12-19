package com.comsysto.metagrapher.client.spi;

import com.comsysto.metagrapher.annotations.MetagrapherServiceType;
import com.google.common.collect.Sets;
import lombok.Value;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Value
public class MetagrapherMetaData {

    final Set<String> tags;
    final Map<String, String> properties;
    final MetagrapherServiceType type;

    public MetagrapherMetaData() {
        this(Collections.emptySet(), Collections.emptyMap(), MetagrapherServiceType.CONTEXT_DEPENDENT);
    }

    public MetagrapherMetaData(Set<String> tags, Map<String, String> properties, MetagrapherServiceType type) {
        this.tags = tags;
        this.properties = properties;
        this.type = type;
    }

    public MetagrapherMetaData merge(MetagrapherMetaData data){

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.putAll(properties);
        map.putAll(data.getProperties());

        return new MetagrapherMetaData(
                Sets.union(this.tags, data.tags),
                Collections.unmodifiableMap(map),
                type.merge(data.type));
    }

    public MetagrapherMetaData type(MetagrapherServiceType type){
        return merge(new MetagrapherMetaData(Collections.emptySet(), Collections.emptyMap(), type));
    }

    public MetagrapherMetaData tags(Set<String> tags){
        return merge(new MetagrapherMetaData(tags, Collections.emptyMap(), MetagrapherServiceType.CONTEXT_DEPENDENT));
    }

    public MetagrapherMetaData property(String name, String value){
        return merge(new MetagrapherMetaData(Collections.emptySet(),
                Collections.singletonMap(name, value),
                MetagrapherServiceType.CONTEXT_DEPENDENT)
        );
    }
}
