package com.flash.framework.gateway.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.flash.framework.gateway.DynamicRouteHandler;
import com.flash.framework.gateway.commons.CustomRouteDefinition;
import com.flash.framework.gateway.commons.RouteChangeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.stream.Collectors;

/**
 * @author zhurg
 * @date 2019/4/10 - 下午1:55
 */
@Slf4j
public class RouteChangeMessageListener implements MessageListener {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private DynamicRouteHandler dynamicRouteHandler;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String msg = redisTemplate.getStringSerializer().deserialize(message.getBody());
        if (log.isDebugEnabled()) {
            log.debug("[Api-Gateway] reveiced route change message : ", msg);
        }
        if (!StringUtils.isEmpty(msg)) {
            RouteChangeMessage changeMessage = JSON.parseObject(msg, RouteChangeMessage.class);
            CustomRouteDefinition customRouteDefinition = null;
            switch (changeMessage.getType()) {
                case ADD:
                    customRouteDefinition = ((JSONObject) changeMessage.getMessage()).toJavaObject(CustomRouteDefinition.class);
                    if (null != customRouteDefinition) {
                        RouteDefinition routeDefinition = assembleRouteDefinition(customRouteDefinition);
                        dynamicRouteHandler.add(routeDefinition);
                    }
                    break;
                case UPDATE:
                    customRouteDefinition = ((JSONObject) changeMessage.getMessage()).toJavaObject(CustomRouteDefinition.class);
                    if (null != customRouteDefinition) {
                        RouteDefinition routeDefinition = assembleRouteDefinition(customRouteDefinition);
                        dynamicRouteHandler.update(routeDefinition);
                    }
                    break;
                case DELETE:
                    String id = changeMessage.getMessage().toString();
                    dynamicRouteHandler.delete(id);
                    break;
            }
        }
    }

    /**
     * 转化成路由对象
     *
     * @param customRouteDefinition
     * @return
     */
    private RouteDefinition assembleRouteDefinition(CustomRouteDefinition customRouteDefinition) {
        RouteDefinition definition = new RouteDefinition();
        definition.setId(customRouteDefinition.getId());
        definition.setOrder(customRouteDefinition.getOrder());

        //设置断言
        definition.setPredicates(customRouteDefinition.getPredicates().stream().map(p -> {
            PredicateDefinition predicateDefinition = new PredicateDefinition();
            predicateDefinition.setArgs(p.getArgs());
            predicateDefinition.setName(p.getName());
            return predicateDefinition;
        }).collect(Collectors.toList()));

        //设置过滤器
        if (!CollectionUtils.isEmpty(customRouteDefinition.getFilters())) {
            definition.setFilters(customRouteDefinition.getFilters().stream().map(f -> {
                FilterDefinition filterDefinition = new FilterDefinition();
                filterDefinition.setArgs(f.getArgs());
                filterDefinition.setName(f.getName());
                return filterDefinition;
            }).collect(Collectors.toList()));
        }

        URI uri = null;
        if (customRouteDefinition.getUri().startsWith("http")) {
            uri = UriComponentsBuilder.fromHttpUrl(customRouteDefinition.getUri()).build().toUri();
        } else {
            uri = UriComponentsBuilder.fromUriString(customRouteDefinition.getUri()).build().toUri();
        }
        definition.setUri(uri);
        return definition;
    }
}