package com.github.kubernetes.java.client.model;

import java.util.Calendar;

import com.github.kubernetes.java.client.interfaces.KubernetesAPIClientInterface;

public class AbstractKubernetesModel {

    private String id;
    private String uid;
    private Kind kind;
    private String apiVersion = KubernetesAPIClientInterface.VERSION;
    private Calendar creationTimestamp;
    private String namespace;
    private String selfLink;
    private int resourceVersion;

    protected AbstractKubernetesModel(Kind kind) {
        this.kind = kind;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Kind getKind() {
        return kind;
    }

    public void setKind(Kind kind) {
        this.kind = kind;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public Calendar getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Calendar creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    public int getResourceVersion() {
        return resourceVersion;
    }

    public void setResourceVersion(int resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

}
