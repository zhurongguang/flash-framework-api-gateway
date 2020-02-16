package com.flash.framework.gateway.commons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhurg
 * @date 2019/4/10 - 下午2:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteChangeMessage {

    private Object message;

    private RouteChangeType type;
}