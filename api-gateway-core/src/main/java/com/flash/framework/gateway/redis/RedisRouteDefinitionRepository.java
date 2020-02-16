package com.flash.framework.gateway.redis;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author zhurg
 * @date 2019/4/9 - 下午5:56
 */
public class RedisRouteDefinitionRepository implements RouteDefinitionRepository {

    private static final String GATEWAY_ROUTES = "gateway:routes";

    private static Map<String, RouteDefinition> ROUTE_CACHE = Maps.newConcurrentMap();

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        if (CollectionUtils.isEmpty(ROUTE_CACHE)) {
            redisTemplate.opsForHash().values(GATEWAY_ROUTES).stream().forEach(routeDefinition -> {
                RouteDefinition rs = JSON.parseObject(routeDefinition.toString(), RouteDefinition.class);
                ROUTE_CACHE.put(rs.getId(), rs);
            });
        }
        return Flux.fromIterable(ROUTE_CACHE.values());
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return route
                .flatMap(routeDefinition -> {
                    redisTemplate.opsForHash().put(GATEWAY_ROUTES, routeDefinition.getId(),
                            JSON.toJSONString(routeDefinition));
                    ROUTE_CACHE.put(routeDefinition.getId(), routeDefinition);
                    return Mono.empty();
                });
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        routeId.subscribe(id -> {
            redisTemplate.opsForHash().delete(GATEWAY_ROUTES, id);
            ROUTE_CACHE.remove(id);
        });
        return Mono.empty();
    }
}