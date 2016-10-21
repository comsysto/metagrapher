package com.comsysto.metagrapher.core.api;

import lombok.NonNull;
import lombok.Value;

import java.util.Map;

@Value
public class Instance implements Comparable<Instance>{
    @NonNull
    String id;

    @NonNull
    InstanceState state;

    @NonNull
    String hostName;

    @NonNull
    Map<String, String> properties;

    int port;

    String homePage;


    @Override
    public int compareTo(Instance o) {
        return id.compareTo(o.id);
    }
}
