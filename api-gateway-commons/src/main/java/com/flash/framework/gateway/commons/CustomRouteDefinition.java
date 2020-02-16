package com.flash.framework.gateway.commons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhurg
 * @date 2019/4/9 - 下午5:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomRouteDefinition implements Serializable {

    private static final long serialVersionUID = 4737087050553301816L;

    private String id;

    private String uri;

    private List<CustomPredicateDefinition> predicates = new ArrayList();

    private List<CustomFilterDefinition> filters = new ArrayList();

    private int order = 0;
}