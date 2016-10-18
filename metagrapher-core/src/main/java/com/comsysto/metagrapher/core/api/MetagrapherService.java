package com.comsysto.metagrapher.core.api;

import com.comsysto.metagrapher.core.spi.MetagrapherClientInfoProvider;
import com.comsysto.metagrapher.core.spi.MetagrapherClientInfo;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

@AllArgsConstructor
@Slf4j
public class MetagrapherService {

    public static final String IMPORT_PREFIX = "metagrapher.import.";
    public static final String EXPORT_PREFIX = "metagrapher.export.";
    public static final String JENKINS_LINK = "metagrapher.app.links.jenkins";
    public static final String STASH_LINK = "metagrapher.app.links.stash";
    public static final String HOME_PAGE_LINK = "metagrapher.app.links.homepage";
    private final MetagrapherClientInfoProvider clientInfoProvider;


    private Set<Application> getApplications() {
        return clientInfoProvider.getClientsByApplicationName().entrySet()
                .stream()
                .map(this::mapClientInfo)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    private Optional<Application> mapClientInfo(Map.Entry<String, Set<MetagrapherClientInfo>> entry) {
        Collection<MetagrapherClientInfo> infoList = entry.getValue();
        Map<String, String> consolidatedProps = mapProperties(infoList, "");

        return Optional.of(
                new Application(
                        entry.getKey(),
                        entry.getKey(),
                        mapInstances(infoList),
                        consolidatedProps,
                        createImports(infoList),
                        createExports(infoList),
                        createApplicationLinks(consolidatedProps)
                )
        );
    }

    private ApplicationLinks createApplicationLinks(Map<String, String> consolidatedProps) {
        return new ApplicationLinks(
                consolidatedProps.get(JENKINS_LINK),
                consolidatedProps.get(STASH_LINK),
                consolidatedProps.get(HOME_PAGE_LINK)
        );
    }

    private Map<String, String> mapProperties(Collection<MetagrapherClientInfo> infoList, String prefix) {
        return getConsolidatedPropertiesWithPrefix(infoList, prefix).entrySet()
                .stream()
                .filter(e -> !(e.getKey().startsWith(IMPORT_PREFIX) || e.getKey().startsWith(JENKINS_LINK)))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Set<ImportedResource> createImports(Collection<MetagrapherClientInfo> infoList) {

        Map<String, String> nameForUri = getConsolidatedPropertiesWithPrefix(infoList, IMPORT_PREFIX);

        Set<ImportedResource> result = nameForUri.entrySet()
                .stream()
                .map(entry -> new ImportedResource(entry.getKey(), Sets.newHashSet(entry.getValue().split(","))))
                .collect(toSet());
        return result;


    }

    private Set<ExportResource> createExports(Collection<MetagrapherClientInfo> infoList) {

        Map<String, String> nameForUri = getConsolidatedPropertiesWithPrefix(infoList, EXPORT_PREFIX);

        Set<ExportResource> result = nameForUri.entrySet()
                .stream()
                .map(entry -> new ExportResource(entry.getKey(), Sets.newHashSet(entry.getValue().split(","))))
                .collect(toSet());
        return result;


    }

    private Map<String, String> getConsolidatedPropertiesWithPrefix(Collection<MetagrapherClientInfo> infoList, String prefix) {
        Map<String, List<String>> allDependencyInfos = getPropertiesFromEachInstanceWithPrefix(infoList, prefix);
        Map<String, Map<String, Long>> depedencyWithValueCount = allDependencyInfos
                .entrySet()
                .stream()
                .collect(
                        toMap(
                                Map.Entry::getKey,
                                entry ->
                                        entry.getValue()
                                                .stream()
                                                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))

                        )
                );

        return depedencyWithValueCount.entrySet()
                .stream()
                .collect(
                        toMap(
                                Map.Entry::getKey,
                                entry ->
                                        entry.getValue().entrySet()
                                                .stream()
                                                .max(Comparator.comparing(Map.Entry::getValue))
                                                .map(Map.Entry::getKey)
                                                .get()
                        ));
    }

    private Map<String, List<String>> getPropertiesFromEachInstanceWithPrefix(Collection<MetagrapherClientInfo> infoList, String prefix) {
        Map<String, List<String>> map = new HashMap<>();
        for (MetagrapherClientInfo clientInfo : infoList) {
            Map<String, String> metadata = clientInfo.getMetadata();
            for (Map.Entry<String, String> entry : metadata.entrySet()) {
                String name = entry.getKey();
                if (name.startsWith(prefix)) {
                    String dependencyName = name.substring(prefix.length());
                    map.computeIfAbsent(dependencyName, (key) -> new ArrayList<>());
                    map.get(dependencyName).add(entry.getValue());
                }
            }
        }

        return map;
    }

    private Set<Instance> mapInstances(Collection<MetagrapherClientInfo> infoList) {
        return infoList
                .stream()
                .map(info -> new Instance(info.getHostName(), info.getPort(), info.getHomePageUrl(), InstanceState.valueOf(info.getState().name())))
                .collect(Collectors.toSet());
    }



    public MetagrapherMap getMap() {
        return new MetagrapherMap(getApplications());
    }
}
