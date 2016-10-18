package com.comsysto.metagrapher.core.api;

import lombok.Value;

import java.util.Map;
import java.util.Set;

@Value
public class Application {
    private final String id;
    private final String name;
    private final Set<Instance> instances;
    private final Map<String, String> properties;
    private final Set<ImportedResource> importedResources;
    private final Set<ExportResource> exportResources;
    private final ApplicationLinks links;
}
