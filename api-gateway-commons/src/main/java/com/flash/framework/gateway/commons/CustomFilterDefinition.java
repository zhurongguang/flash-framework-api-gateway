package com.flash.framework.gateway.commons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author zhurg
 * @date 2019/4/10 - 上午11:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomFilterDefinition implements Serializable {

    private static final long serialVersionUID = 6936245101959373203L;
    private String name;
    private Map<String, String> args = new LinkedHashMap();
}