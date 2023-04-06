package com.primedice.client;

public class ConfigValue {
    private String key;
    private String value;

    public ConfigValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) { this.value = value; }
}