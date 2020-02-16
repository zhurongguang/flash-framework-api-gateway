package com.flash.framework.gateway.sdk;

import com.alibaba.fastjson.JSON;
import com.flash.framework.gateway.commons.CustomPredicateDefinition;
import com.flash.framework.gateway.commons.CustomRouteDefinition;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * @author zhurg
 * @date 2019/4/10 - 下午2:34
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApiGatewaySdkApplication.class})
public class SdkTest {

    @Autowired
    private GatewayRouteReader gatewayRouteReader;

    @Autowired
    private GatewayRouteWriter gatewayRouteWriter;

    @Test
    public void testAdd() {
        CustomRouteDefinition customRouteDefinition = new CustomRouteDefinition();
        customRouteDefinition.setId("demo");
        customRouteDefinition.setUri("lb://zk-web");

        Map<String, String> predicateParams1 = Maps.newHashMap();
        predicateParams1.put("_genkey_0", "/demo/**");
        customRouteDefinition.setPredicates(Lists.newArrayList(
                new CustomPredicateDefinition("Path", predicateParams1)
        ));

        gatewayRouteWriter.addRoute(customRouteDefinition);

        customRouteDefinition = new CustomRouteDefinition();
        customRouteDefinition.setId("demo2");
        customRouteDefinition.setUri("lb://zk-web");

        Map<String, String> predicateParams2 = Maps.newHashMap();
        predicateParams2.put("_genkey_0", "/demo2/**");
        customRouteDefinition.setPredicates(Lists.newArrayList(
                new CustomPredicateDefinition("Path", predicateParams2)
        ));

        gatewayRouteWriter.addRoute(customRouteDefinition);

    }

    @Test
    public void testQuery() {
        System.out.println(JSON.toJSONString(gatewayRouteReader.getRoutes()));
    }

    @Test
    public void testUpdate() {
        CustomRouteDefinition customRouteDefinition = new CustomRouteDefinition();
        customRouteDefinition.setId("demo");
        customRouteDefinition.setUri("lb://zk-web");

        Map<String, String> predicateParams1 = Maps.newHashMap();
        predicateParams1.put("_genkey_0", "/test/**");
        customRouteDefinition.setPredicates(Lists.newArrayList(
                new CustomPredicateDefinition("Path", predicateParams1)
        ));
        gatewayRouteWriter.updateRoute(customRouteDefinition);
    }

    @Test
    public void testDelete() {
        gatewayRouteWriter.deleteRoute("demo");
    }
}