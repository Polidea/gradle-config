package com.tmiyamon.config

class SettingsClassGenerator {
    public static SettingsElement buildAST(Map<String, Object> parsedYaml) {
        new SettingsRootClass(parsedYaml.collect { internalBuildAST([it.key], it.value).toTopLevel() })
    }

    private static SettingsElement internalBuildAST(List<String> keys, Object o) {
        if (o instanceof String ||
            o instanceof Integer ||
            o instanceof Double ||
            o instanceof Date ||
            o instanceof Boolean
        ) {
            return new SettingsField(keys.last(), o)
        } else if (o instanceof Map<String, Object>) {
            def children = (o as Map<String, Object>).collect { internalBuildAST(keys + [it.key] as List, it.value)}
            return new SettingsClass(keys.last(), children)
        }

        throw new RuntimeException("Not supported type: ${o.getClass()}")
    }

}