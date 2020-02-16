package com.flash.framework.gateway.sdk;

import com.flash.framework.gateway.sdk.redis.RedisGatewayRouteReader;
import com.flash.framework.gateway.sdk.redis.RedisGatewayRouteWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhurg
 * @date 2019/4/10 - 下午2:28
 */
@Configuration
public class ApiGatewayAutoConfiguration {

    @Bean
    public GatewayRouteReader gatewayRouteReader() {
        return new RedisGatewayRouteReader();
    }

    @Bean
    public GatewayRouteWriter gatewayRouteWriter() {
        return new RedisGatewayRouteWriter();
    }
}