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
 * https://github.com/GoogleCloudPlatform/kubernetes/blob/master/api/examples/service.json
 * @author github
 *
 */
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class Service extends AbstractKubernetesModel {

    private String id;
    private String creationTimestamp;
    private String selfLink;
    private String name;
    private int port;
    private int containerPort;
    private String type;
    private int nodePort;
    private Selector selector;
    private Label labels;

    public Service() {
        super("Service");
        type = "ClusterIP"
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getContainerPort() {
        return containerPort;
    }

    public void setContainerPort(int containerPort) {
        this.containerPort = containerPort;
    }
    
    public String getType() {
        return type;
    }

    public void setId(String type) {
        this.type = type;
    }
    
    public int getNodePort() {
        return nodePort;
    }

    public void setNodePort(int nodePort) {
        this.nodePort = nodePort;
    }

    public Selector getSelector() {
        return selector;
    }

    public void setSelector(Selector selector) {
        this.selector = selector;
    }

    @Override
    public String toString() {
        return "Service [id=" + id + ", creationTimestamp=" + creationTimestamp + ", selfLink=" + selfLink + ", name="
                + name + ", port=" + port + ", containerPort=" + containerPort + ", type=" + type + ", nodePort=" + nodePort + ", selector=" + selector + ", labels="
                + labels + "]";
    }

}
