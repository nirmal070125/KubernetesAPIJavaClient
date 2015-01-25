package com.github.kubernetes.java.client.exceptions;

import java.util.List;

import com.github.kubernetes.java.client.model.AbstractKubernetesModel;
import com.github.kubernetes.java.client.model.Kind;
import com.google.common.base.MoreObjects;

public class StatusDetails extends AbstractKubernetesModel {
    private String id;
    private List<StatusDetailsCause> causes;

    public StatusDetails() {
        super(Kind.STATUSDETAILS);
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<StatusDetailsCause> getCauses() {
        return causes;
    }

    public void setCauses(List<StatusDetailsCause> causes) {
        this.causes = causes;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("id", id).add("causes", causes).toString();
    }
}
