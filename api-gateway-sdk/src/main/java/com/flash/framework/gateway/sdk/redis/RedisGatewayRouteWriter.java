package com.flash.framework.gateway.sdk.redis;

import com.alibaba.fastjson.JSON;
import com.flash.framework.gateway.commons.CustomRouteDefinition;
import com.flash.framework.gateway.commons.RouteChangeMessage;
import com.flash.framework.gateway.commons.RouteChangeType;
import com.flash.framework.gateway.sdk.GatewayRouteWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author zhurg
 * @date 2019/4/10 - 上午11:41
 */
public class RedisGatewayRouteWriter implements GatewayRouteWriter {

    @Value("${gateway.route.change.topic:gateway}")
    private String topic;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean addRoute(CustomRouteDefinition routeDefinition) {
        RouteChangeMessage addMessage = new RouteChangeMessage(routeDefinition, RouteChangeType.ADD);
        redisTemplate.convertAndSend(topic, JSON.toJSONString(addMessage));
        return true;
    }

    @Override
    public boolean updateRoute(CustomRouteDefinition routeDefinition) {
        RouteChangeMessage updateMessage = new RouteChangeMessage(routeDefinition, RouteChangeType.UPDATE);
        redisTemplate.convertAndSend(topic, JSON.toJSONString(updateMessage));
        return true;
    }

    @Override
    public boolean deleteRoute(String id) {
        RouteChangeMessage deleteMessage = new RouteChangeMessage(id, RouteChangeType.DELETE);
        redisTemplate.convertAndSend(topic, JSON.toJSONString(deleteMessage));
        return true;
    }
}