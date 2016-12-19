package com.comsysto.metagrapher.ui.api;

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

    @NonNull
    Map<String, String> links;


    int port;

    String homePage;


    @Override
    public int compareTo(Instance o) {
        return id.compareTo(o.id);
    }
}
