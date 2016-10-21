package com.comsysto.metagrapher.core.api;

import lombok.Value;

import java.util.Set;
import java.util.SortedSet;

@Value
public class Application {
    private final String id;
    private final String name;
    private final SortedSet<Pool> pools;
    private final Set<ImportedResource> importedResources;
    private final Set<ExportResource> exportResources;
}
