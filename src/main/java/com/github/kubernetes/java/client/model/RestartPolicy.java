package com.github.kubernetes.java.client.model;

import java.util.Map;

public class RestartPolicy {

    private Map<String, String> always;
    private Map<String, String> onFailure;
    private Map<String, String> never;

    public Map<String, String> getAlways() {
        return always;
    }

    public void setAlways(Map<String, String> always) {
        this.always = always;
    }

    public Map<String, String> getOnFailure() {
        return onFailure;
    }

    public void setOnFailure(Map<String, String> onFailure) {
        this.onFailure = onFailure;
    }

    public Map<String, String> getNever() {
        return never;
    }

    public void setNever(Map<String, String> never) {
        this.never = never;
    }
}
