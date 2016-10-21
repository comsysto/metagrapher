package com.comsysto.metagrapher.core.service;

import com.comsysto.metagrapher.core.api.*;
import com.comsysto.metagrapher.core.spi.MetagrapherClientInfoProvider;
import com.comsysto.metagrapher.core.spi.MetagrapherClientInfo;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@AllArgsConstructor
@Slf4j
public class MetagrapherService {

    public static final String POOL = "metagrapher.pool";
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

        return Optional.of(
                new Application(
                        entry.getKey(),
                        entry.getKey(),
                        mapPools(infoList),
                        createImports(infoList),
                        createExports(infoList)
                )
        );
    }

    private SortedSet<Pool> mapPools(Collection<MetagrapherClientInfo> infoList) {
        Map<String, List<MetagrapherClientInfo>> infosByPool = infoList.stream()
                .collect(groupingBy(this::getPoolName));

        Map<String, String> consolidatedProps = mapProperties(infoList, "");

        return infosByPool.entrySet().stream()
                .map(e -> new Pool(e.getKey(), mapInstances(e.getValue()), createApplicationLinks(consolidatedProps)))
                .collect(toCollection(TreeSet::new));
    }

    private String getPoolName(MetagrapherClientInfo info) {
        String poolName = info.getMetadata().get(POOL);
        return poolName == null ? "<default>" : poolName;
    }

    private ArtifactLinks createApplicationLinks(Map<String, String> consolidatedProps) {
        return new ArtifactLinks(
                consolidatedProps.get(JENKINS_LINK),
                consolidatedProps.get(STASH_LINK),
                consolidatedProps.get(HOME_PAGE_LINK)
        );
    }

    private Map<String, String> mapProperties(Collection<MetagrapherClientInfo> infoList, String prefix) {
        return getConsolidatedPropertiesWithPrefix(infoList, prefix).entrySet()
                .stream()
                //TODO use contains ...
                .filter(e -> !(e.getKey().startsWith(IMPORT_PREFIX) || e.getKey().startsWith(JENKINS_LINK) || e.getKey().startsWith(EXPORT_PREFIX) || e.getKey().startsWith(POOL)  ))
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

    private SortedSet<Instance> mapInstances(Collection<MetagrapherClientInfo> infoList) {
        return infoList
                .stream()
                .map(info -> {
                    InstanceState state = InstanceState.valueOf(info.getState().name());
                    return new Instance(info.getId(), state, info.getHostName(), info.getMetadata(), info.getPort(), info.getHomePageUrl());
                })
                .collect(Collectors.toCollection(TreeSet::new));
    }



    public MetagrapherMap getMap() {
        return new MetagrapherMap(getApplications());
    }
}
