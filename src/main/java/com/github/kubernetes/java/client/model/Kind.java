package com.github.kubernetes.java.client.model;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

public enum Kind {
    STATUS("Status"), STATUSDETAILS("StatusDetails"), POD("Pod"), PODLIST("PodList"), REPLICATIONCONTROLLER(
            "ReplicationController"), REPLICATIONCONTROLLERLIST("ReplicationControllerList"), SERVICE("Service"), SERVICELIST(
            "ServiceList");

    private final String text;

    private Kind(final String text) {
        this.text = text;
    }

    @JsonCreator
    public static Kind forValue(String value) {
        return Kind.valueOf(value.toUpperCase());
    }

    @Override
    @JsonValue
    public String toString() {
        return text;
    }
}
