package com.github.kubernetes.java.client.model;


public class AbstractKubernetesModel {

    private String kind;
    private String apiVersion = "v1beta1";

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
