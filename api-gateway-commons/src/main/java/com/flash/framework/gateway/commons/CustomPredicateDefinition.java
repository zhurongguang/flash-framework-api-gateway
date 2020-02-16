package com.flash.framework.gateway.commons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author zhurg
 * @date 2019/4/10 - 上午11:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomPredicateDefinition implements Serializable {

    private static final long serialVersionUID = 8773361497784234728L;
    private String name;
    private Map<String, String> args = new LinkedHashMap();
}