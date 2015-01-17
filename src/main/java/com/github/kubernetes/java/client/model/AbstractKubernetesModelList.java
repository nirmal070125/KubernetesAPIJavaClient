package com.github.kubernetes.java.client.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public abstract class AbstractKubernetesModelList<T> extends AbstractKubernetesModel {

    private List<T> items = new ArrayList<T>();

    protected AbstractKubernetesModelList(String kind) {
        super(kind);
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return getKind() + " [items=" + StringUtils.join(getItems(), ',') + "]";
    }
}
