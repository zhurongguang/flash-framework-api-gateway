package com.flash.framework.gateway.sdk;

import com.flash.framework.gateway.commons.CustomRouteDefinition;

import java.util.List;

/**
 * @author zhurg
 * @date 2019/4/10 - 上午11:42
 */
public interface GatewayRouteReader {

    /**
     * 获取所有路由规则
     *
     * @return
     */
    List<CustomRouteDefinition> getRoutes();
}