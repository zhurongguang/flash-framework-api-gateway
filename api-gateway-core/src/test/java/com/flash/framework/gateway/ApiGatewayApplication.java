package com.flash.framework.gateway;

import com.flash.framework.gateway.redis.RedisRouteDefinitionRepository;
import com.google.common.collect.Lists;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

    @Bean
    public RouteDefinitionRepository redisRouteDefinitionRepository() {
        return new RedisRouteDefinitionRepository();
    }

    @Bean
    public DynamicRouteHandler dynamicRouteHandler() {
        return new DynamicRouteHandler();
    }

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(ApiGatewayApplication.class, args);
        DynamicRouteHandler dynamicRouteHandler = applicationContext.getBean(DynamicRouteHandler.class);
        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.setId("demo");
        routeDefinition.setUri(UriComponentsBuilder.fromUriString("lb://zk-web").build().toUri());

        PredicateDefinition predicate = new PredicateDefinition();
        predicate.setName("Path");
        Map<String, String> predicateParams = new HashMap<>(8);
        predicateParams.put("_genkey_0", "/demo/**");
        predicate.setArgs(predicateParams);
        routeDefinition.setPredicates(Lists.newArrayList(predicate));

        dynamicRouteHandler.add(routeDefinition);

        routeDefinition = new RouteDefinition();
        routeDefinition.setId("demo2");
        routeDefinition.setUri(UriComponentsBuilder.fromUriString("lb://zk-web").build().toUri());

        predicate = new PredicateDefinition();
        predicate.setName("Path");
        predicateParams = new HashMap<>(8);
        predicateParams.put("_genkey_0", "/demo2/**");
        predicate.setArgs(predicateParams);
        routeDefinition.setPredicates(Lists.newArrayList(predicate));

        dynamicRouteHandler.add(routeDefinition);
    }

    /*
    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        RouteLocator routeLocator = builder.routes()
                .route(p -> p
                        .path("/zk-web")
                        .uri("lb://zk-web"))
                .build();
        return routeLocator;
    }
    */

}
