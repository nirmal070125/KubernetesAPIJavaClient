package com.github.kubernetes.java.client.model;

import com.github.kubernetes.java.client.interfaces.KubernetesAPIClientInterface;


public class AbstractKubernetesModel {

    private String kind;
    private String apiVersion = KubernetesAPIClientInterface.VERSION;

    protected AbstractKubernetesModel(String kind) {
        this.kind = kind;
    }

    public String getKind() {
    	return kind;
    }

    public void setKind(String kind) {
    	this.kind = kind;
    }

    public String getApiVersion() {
    	return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
    	this.apiVersion = apiVersion;
    }

}
