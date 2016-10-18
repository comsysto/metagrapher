package com.comsysto.metagrapher.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@SpringBootApplication
@EnableEurekaClient
@Controller
public class Application {

    private final Logger logger = LoggerFactory.getLogger(Application.class);

    public static final String PREFIX = "eureka.instance.metadata-map.metagrapher.";

    @Autowired
    ConfigurableEnvironment environment;

    @RequestMapping("/")
    @ResponseBody
    public Map<String, String> listMetagrapherProperties() {
        Set<String> allPropertyNames = new TreeSet<>();
        for (PropertySource<?> propertySource : environment.getPropertySources()) {
            if (propertySource instanceof EnumerablePropertySource) {
                String[] propertyNames = ((EnumerablePropertySource) propertySource).getPropertyNames();
                allPropertyNames.addAll(Arrays.asList(propertyNames));
            }
        }

        Map<String, String> json = new HashMap<>();
        for (String name : allPropertyNames) {
            if (name.startsWith(PREFIX)) {
                json.put(
                        name.substring(PREFIX.length()),
                        environment.getProperty(name)
                );
            }
        }

        return json;
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class).web(true).run(args);
    }

}
