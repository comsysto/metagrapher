package com.comsysto.metagrapher.ui.service;

import com.comsysto.metagrapher.ui.api.*;
import com.comsysto.metagrapher.ui.spi.MetagrapherClientInfo;
import com.comsysto.metagrapher.ui.spi.MetagrapherClientInfoProvider;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.comsysto.metagrapher.core.MetagrapherConstants.*;
import static java.util.stream.Collectors.*;

@AllArgsConstructor
@Slf4j
public class MetagrapherService {

    private final MetagrapherClientInfoProvider clientInfoProvider;
//    private Pattern importPattern = Pattern.compile(IMPORT_PREFIX.replaceAll("\\.", "\\.") + "(.*)$");
//    private Pattern exportPattern = Pattern.compile(EXPORT_PREFIX.replaceAll("\\.", "\\.") + "(.*)$");


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
                .map(e -> new Pool(e.getKey(), mapInstances(e.getValue()), filterMap(consolidatedProps, APP_LINK_PREFIX)))
                .collect(toCollection(TreeSet::new));
    }

    private Map<String, String> filterMap(Map<String, String> consolidatedProps, String prefix) {
        return consolidatedProps.entrySet()
                .stream()
                .filter(e-> e.getKey().startsWith(prefix))
                .collect(toMap(e -> e.getKey().substring(prefix.length(), e.getKey().length()), Map.Entry::getValue));
    }

    private String getPoolName(MetagrapherClientInfo info) {
        String poolName = info.getMetadata().get(POOL);
        return poolName == null ? "<default>" : poolName;
    }


    private Map<String, String> mapProperties(Collection<MetagrapherClientInfo> infoList, String prefix) {
        return getConsolidatedPropertiesWithPrefix(infoList, prefix).entrySet()
                .stream()
                //TODO use contains ...
                .filter(e -> !(e.getKey().startsWith(IMPORT_PREFIX) || e.getKey().startsWith(INSTANCE_LINK_PREFIX) || e.getKey().startsWith(EXPORT_PREFIX) || e.getKey().startsWith(POOL)  ))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Set<ImportedResource> createImports(Collection<MetagrapherClientInfo> infoList) {

        Map<String, String> nameForUri = getConsolidatedPropertiesWithPrefix(infoList, IMPORT_PREFIX);

        Set<ImportedResource> result = nameForUri.entrySet()
                .stream()
                .filter(entry-> !entry.getKey().contains("."))
                .map(entry -> new ImportedResource(entry.getKey(), Sets.newHashSet(entry.getValue().split(","))))
                .collect(toSet());
        return result;


    }

    private Set<ExportResource> createExports(Collection<MetagrapherClientInfo> infoList) {

        Map<String, String> nameForUri = getConsolidatedPropertiesWithPrefix(infoList, EXPORT_PREFIX);

        Set<ExportResource> result = nameForUri.entrySet()
                .stream()
                .filter(entry-> !entry.getKey().contains("."))
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
                    return new Instance(info.getId(), state, info.getHostName(), info.getMetadata(),
                            filterMap(info.getMetadata(), INSTANCE_LINK_PREFIX), info.getPort(), info.getHomePageUrl());
                })
                .collect(Collectors.toCollection(TreeSet::new));
    }



    public MetagrapherMap getMap() {
        return new MetagrapherMap(getApplications());
    }
}
