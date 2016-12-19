package com.comsysto.metagrapher.ui.api;

import lombok.Value;

import java.util.Set;

@Value
public class ExportResource {
    private final String name;
    private final Set<String> tags;
}
