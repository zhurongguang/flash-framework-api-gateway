package com.flash.framework.gateway.sdk.redis;

import com.alibaba.fastjson.JSON;
import com.flash.framework.gateway.commons.CustomRouteDefinition;
import com.flash.framework.gateway.sdk.GatewayRouteReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhurg
 * @date 2019/4/10 - 下午2:24
 */
public class RedisGatewayRouteReader implements GatewayRouteReader {

    private static final String GATEWAY_ROUTES = "gateway:routes";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<CustomRouteDefinition> getRoutes() {
        return stringRedisTemplate.opsForHash().values(GATEWAY_ROUTES).stream()
                .map(routeDefinition -> JSON.parseObject(routeDefinition.toString(), CustomRouteDefinition.class))
                .collect(Collectors.toList());
    }
}