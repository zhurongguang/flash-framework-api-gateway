package com.flash.framework.gateway.sdk;

import com.flash.framework.gateway.commons.CustomRouteDefinition;

/**
 * @author zhurg
 * @date 2019/4/10 - 上午11:35
 */
public interface GatewayRouteWriter {

    /**
     * 添加路由
     *
     * @param routeDefinition
     * @return
     */
    boolean addRoute(CustomRouteDefinition routeDefinition);


    /**
     * 更新路由
     *
     * @param routeDefinition
     * @return
     */
    boolean updateRoute(CustomRouteDefinition routeDefinition);

    /**
     * 删除路由
     *
     * @param id
     * @return
     */
    boolean deleteRoute(String id);
}