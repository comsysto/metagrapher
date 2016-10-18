package com.comsysto.metagrapher.core.api;

import lombok.Value;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Value
public class MetagrapherMap {
    private final Set<Application> applications;

    public Set<ImportedResource> getExternalImports(){
        Set<String> exportNames = applications
                .stream()
                .flatMap(s -> s.getExportResources().stream())
                .map(ExportResource::getName)
                .collect(toSet());

        return applications
                .stream()
                .flatMap(s -> s.getImportedResources().stream())
                .filter(i -> !exportNames.contains(i.getName()))
                .collect(toSet());
    }

}




