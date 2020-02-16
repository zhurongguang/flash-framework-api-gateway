package com.flash.framework.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import reactor.core.publisher.Mono;

/**
 * @author zhurg
 * @date 2019/4/10 - 上午9:27
 */
@Slf4j
public class DynamicRouteHandler implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;

    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;


    private void notifyChanged() {
        publisher.publishEvent(new RefreshRoutesEvent(this));
    }

    /**
     * 增加路由
     */
    public boolean add(RouteDefinition definition) {
        routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        notifyChanged();
        return true;
    }


    /**
     * 更新路由
     */
    public boolean update(RouteDefinition definition) {
        try {
            delete(definition.getId());
            add(definition);
            notifyChanged();
            return true;
        } catch (Exception e) {
            log.error("[Api-Gateway] update route fail ", e);
            return false;
        }


    }

    /**
     * 删除路由
     */
    public boolean delete(String id) {
        try {
            routeDefinitionWriter.delete(Mono.just(id));
            notifyChanged();
            return true;
        } catch (Exception e) {
            log.error("[Api-Gateway] delete route fail ", e);
            return false;
        }

    }


    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }
}