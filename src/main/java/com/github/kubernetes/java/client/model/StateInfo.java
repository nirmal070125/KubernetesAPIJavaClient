package com.github.kubernetes.java.client.model;

import java.util.Map;

public class StateInfo {
    private int restartCount;
    private String podIP;
    private String image;
    private String containerID;
    private Map<String, Map<String, String>> state;

    public int getRestartCount() {
        return restartCount;
    }

    public void setRestartCount(int restartCount) {
        this.restartCount = restartCount;
    }

    public String getPodIP() {
        return podIP;
    }

    public void setPodIP(String podIP) {
        this.podIP = podIP;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContainerID() {
        return containerID;
    }

    public void setContainerID(String containerID) {
        this.containerID = containerID;
    }

    public Map<String, Map<String, String>> getState() {
        return state;
    }

    public Map<String, String> getState(String key) {
        return state.get(key);
    }

    public void setState(Map<String, Map<String, String>> state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "State [containerID=" + containerID + ", podIP=" + podIP + ", restartCount=" + restartCount + ", image="
                + image + ", state=" + state + "]";
    }

}
