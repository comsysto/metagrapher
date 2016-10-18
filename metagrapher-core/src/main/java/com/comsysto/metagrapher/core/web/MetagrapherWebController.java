package com.comsysto.metagrapher.core.web;

import com.comsysto.metagrapher.core.api.*;
import com.comsysto.metagrapher.core.impl.MetagrapherUiConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Controller
@RequiredArgsConstructor
public class MetagrapherWebController {


    private final MetagrapherService metagrapherService;
    private final MetagrapherUiConfigRepository repository;


    @ResponseBody
    @RequestMapping("/metagrapher/graph.json")
    public Graph getGraph() {
        Graph graph = createGraph(metagrapherService.getMap());
        return graph;
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
                Stream.of(new Element(new ServiceNode(application.getId(), application.getName(), new ArrayList<>(application.getInstances()), application.getLinks(), application.getProperties()), createApplicationClasses(application))),
                mapExports(application),
                mapExportEdges(application),
                mapExportImportEdges(application),
                mapServiceState(application)
        ).flatMap(Function.identity());
    }

    private Stream<Element> mapServiceState(Application application) {
        Map<InstanceState, Long> stateCounts = application.getInstances()
                .stream()
                .map(Instance::getState)
                .collect(groupingBy(Function.identity(), Collectors.counting()));

        return Stream.of(InstanceState.values()).flatMap(state -> {
            String stateId = application.getId() + ":" + state.name();
            int count = stateCounts.getOrDefault(state, 0L).intValue();
            return count == 0
                    ? Stream.empty()
                    : Stream.of(
                    new Element(new StateNode(stateId, count, state.ordinal()), "state " + state.name().toLowerCase()),
                    new Element(new Edge(application.getId(), stateId, Collections.emptySet()), "state-edge")
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
        String stateClass = application.getInstances()
                .stream()
                .map(Instance::getState)
                .max(Comparator.naturalOrder())
                .get()
                .name()
                .toLowerCase();
        return "service " + stateClass;
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


    @RequestMapping("/metagrapher")
    public ModelAndView internalIndexHtml(HttpServletRequest request) throws IOException {
        ModelAndView modelAndView = new ModelAndView("metagrapher");
        // This variable is used to build the abstract resource urls, this can be done in a more sophisticated way ...
        modelAndView.addObject("applicationBasePath", request.getContextPath());
        return modelAndView;
    }

    @RequestMapping(value = "/metagrapher/config", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void postPosition(@RequestBody Map<String, Object> config) {
        repository.storeUiConfig(config);
    }

    @RequestMapping(value = "/metagrapher/config", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getPosition() {
        return repository.loadUiConfig().orElse(Collections.emptyMap());
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
        private final List<Instance> instances;
        private final ApplicationLinks appLinks;
        private final Map<String, String> properties;

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
