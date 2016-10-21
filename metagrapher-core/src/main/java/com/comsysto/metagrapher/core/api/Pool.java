package com.comsysto.metagrapher.core.api;

import lombok.NonNull;
import lombok.Value;

import java.util.SortedSet;

@Value
public class Pool implements Comparable<Pool>{
    @NonNull
    String name;

    @NonNull
    SortedSet<Instance> instances;

    @NonNull
    ArtifactLinks links;

    @Override
    public int compareTo(Pool o) {
        return name.compareTo(o.name);
    }
}
