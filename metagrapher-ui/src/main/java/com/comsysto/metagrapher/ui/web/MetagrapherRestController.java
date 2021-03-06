package com.comsysto.metagrapher.ui.web;

import com.comsysto.metagrapher.ui.api.*;
import com.comsysto.metagrapher.ui.impl.GraphConfig;
import com.comsysto.metagrapher.ui.impl.MetagrapherUiConfigRepository;
import com.comsysto.metagrapher.ui.service.MetagrapherService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rest")
public class MetagrapherRestController {


    private final MetagrapherService metagrapherService;
    private final MetagrapherUiConfigRepository repository;


    @RequestMapping("/graph")
    public Graph getGraph() {
        Graph graph = createGraph(metagrapherService.getMap());
        return graph;
    }

    @RequestMapping(value = "/config", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void postPosition(@RequestBody GraphConfig config) {
        repository.storeUiConfig(config);
    }

    @RequestMapping(value = "/config", method = RequestMethod.GET)
    @ResponseBody
    public GraphConfig getPosition() {
        return repository.loadUiConfig().orElse(new GraphConfig(Collections.emptyMap()));
    }


    private Graph createGraph(MetagrapherMap map) {
        List<Element> nodes = map.getApplications()
                .stream()
                .flatMap(service -> mapApplication(service))
                .collect(toList());

        List<Element> externalSystem = map.getExternalImports()
                .stream()
                .map((imp) -> new Element(new ExportNode(imp.getName(), imp.getName(), imp.getTags()),
                        "external-system"))
                .collect(Collectors.toList());


        return new Graph(Stream.of(nodes, externalSystem).flatMap(List::stream).collect(toList()));
    }

    private Stream<Element> mapApplication(Application application) {
        return Stream.of(
                Stream.of(new Element(new ServiceNode(application.getId(), application.getName()), createApplicationClasses(application))),
                mapExports(application),
                mapExportEdges(application),
                mapExportImportEdges(application),
                mapPools(application.getName(), application.getPools())
        ).flatMap(Function.identity());
    }

    private Stream<Element> mapPools(String applicationId, SortedSet<Pool> pools) {
        return pools.stream()
                .flatMap(pool -> mapPool(applicationId, pool));
    }

    private Stream<? extends Element> mapPool(String applicationId, Pool pool) {
        String poolId = applicationId + ":" + pool.getName();

        Stream<Element> poolStream = Stream.of(
                new Element(new PoolNode(poolId, pool.getName(), new ArrayList<>(pool.getInstances()), pool.getLinks()), "pool"),
                new Element(new Edge(applicationId, poolId, Collections.emptySet()), "service-pool-edge")
        );

        return Stream.concat(poolStream, mapServiceState(poolId, pool));
    }

    private Stream<Element> mapServiceState(String poolId, Pool pool) {
        Map<InstanceState, Long> stateCounts = pool.getInstances()
                .stream()
                .map(Instance::getState)
                .collect(groupingBy(Function.identity(), Collectors.counting()));

        return Stream.of(InstanceState.values()).flatMap(state -> {
            String stateId = poolId + ":" + state.name();
            int count = stateCounts.getOrDefault(state, 0L).intValue();
            return count == 0
                    ? Stream.empty()
                    : Stream.of(
                    new Element(new StateNode(stateId, count, state.ordinal()), "state " + state.name().toLowerCase()),
                    new Element(new Edge(poolId, stateId, Collections.emptySet()), "state-edge")
            );
        });
    }

    private Stream<Element> mapExportEdges(Application application) {
        return application.getExportResources()
                .stream()
                .map(e -> new Element(new Edge(application.getId(), e.getName(), e.getTags()), "service-export-edge"));
    }

    private Stream<Element> mapExports(Application application) {
        return application.getExportResources()
                .stream()
                .map(e ->
                        new Element(
                                new ExportNode(
                                        e.getName(),
                                        e.getName(),
                                        e.getTags()
                                ),
                                "export")
                );
    }


    private String createApplicationClasses(Application application) {
        return "service";
    }


    private Stream<Element> mapExportImportEdges(Application application) {
        return application.getImportedResources()
                .stream()
                .map(importedService -> new Element(
                        new Edge(
                                application.getId(),
                                importedService.getName(),
                                importedService.getTags()
                        ),
                        "import-export-edge"
                ));
    }


    @Value
    public static class Element {
        private final ElementData data;
        private final String classes;

        public boolean isSelected() {
            return data.isSelected();
        }

        public boolean isSelectable() {
            return data.isSelectable();
        }

        public boolean isGrabbable() {
            return data.isGrabbable();
        }

        public boolean isLocked() {
            return data.isLocked();
        }
    }

    @Value
    public static class ServiceNode implements ElementData {
        private final String group = "nodes";

        protected final boolean selected = false;
        protected final boolean selectable = true;
        protected final boolean locked = false;
        protected final boolean grabbable = true;

        private final String id;
        private final String name;
        private final String type = "service";

    }

    @Value
    public static class ExportNode implements ElementData {
        private final String group = "nodes";

        protected final boolean selected = false;
        protected final boolean selectable = true;
        protected final boolean locked = false;
        protected final boolean grabbable = true;

        private final String id;
        private final String name;
        private final Set<String> tags;
        private final String type = "export";
    }

    @Value
    public static class StateNode implements ElementData {
        private final String group = "nodes";

        protected final boolean selected = false;
        protected final boolean selectable = false;
        protected final boolean locked = false;
        protected final boolean grabbable = false;

        private final String id;
        private final int count;
        private final int order;
        private final String type = "state";
    }

    @Value
    public static class PoolNode implements ElementData {
        private final String group = "nodes";

        protected final boolean selected = false;
        protected final boolean selectable = true;
        protected final boolean locked = false;
        protected final boolean grabbable = false;

        private final String id;
        private final String name;
        private final List<Instance> instances;
        private final Map<String, String> links;
        private final String type = "pool";
    }



    @Value
    public static class Edge implements ElementData {
        private final String group = "edges";

        protected final boolean selected = false;
        protected final boolean selectable = false;
        protected final boolean locked = true;
        protected final boolean grabbable = false;

        private final String source;
        private final String target;
        private final Set<String> tags;
    }

    @Value
    public static class Graph {
        private final List<Element> elements;
    }

    public interface ElementData{
        boolean isSelected();
        boolean isSelectable();
        boolean isGrabbable();
        boolean isLocked();
    }

}
