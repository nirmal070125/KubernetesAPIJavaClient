/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
package com.github.kubernetes.java.client.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author github
 *
 */
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReplicationController extends AbstractKubernetesModel {

    private String id;
    private int resourceVersion;
    private String creationTimestamp;
    private String selfLink;
    private Label labels;
    private State desiredState;
    private State currentState;

    public ReplicationController() {
        super(Kind.REPLICATIONCONTROLLER);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(String creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    public Label getLabels() {
        return labels;
    }

    public void setLabels(Label labels) {
        this.labels = labels;
    }

    public State getDesiredState() {
        return desiredState;
    }

    public void setDesiredState(State desiredState) {
        this.desiredState = desiredState;
    }

    public State getCurrentState() {
        return currentState;
    }

    public int getResourceVersion() {
        return resourceVersion;
    }

    public void setResourceVersion(int resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

    @Override
    public String toString() {
        return "ReplicationController [id=" + id + ", creationTimestamp=" + creationTimestamp
                + ", selfLink=" + selfLink + ", labels=" + labels + ", desiredState=" + desiredState + "]";
    }

}
