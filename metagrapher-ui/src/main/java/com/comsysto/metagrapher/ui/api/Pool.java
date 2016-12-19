package com.comsysto.metagrapher.ui.api;

import lombok.NonNull;
import lombok.Value;

import java.util.Map;
import java.util.SortedSet;

@Value
public class Pool implements Comparable<Pool>{
    @NonNull
    String name;

    @NonNull
    SortedSet<Instance> instances;

    @NonNull
    Map<String, String> links;

    @Override
    public int compareTo(Pool o) {
        return name.compareTo(o.name);
    }
}
