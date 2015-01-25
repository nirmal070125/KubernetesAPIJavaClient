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

import java.net.InetAddress;

/**
 * https://github.com/GoogleCloudPlatform/kubernetes/blob/master/api/examples/
 * service.json
 */
public class Service extends AbstractKubernetesModel {

    private String name;
    private int port;
    private String containerPort;
    private Selector selector;
    private Label labels;
    private String protocol = "TCP";
    private InetAddress portalIP;
    private String sessionAffinity;

    public Service() {
        super(Kind.SERVICE);
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

    public String getContainerPort() {
        return containerPort;
    }

    public void setContainerPort(String containerPort) {
        this.containerPort = containerPort;
    }

    public Selector getSelector() {
        return selector;
    }

    public void setSelector(Selector selector) {
        this.selector = selector;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public InetAddress getPortalIP() {
        return portalIP;
    }

    public void setPortalIP(InetAddress portalIP) {
        this.portalIP = portalIP;
    }

    public String getSessionAffinity() {
        return sessionAffinity;
    }

    public void setSessionAffinity(String sessionAffinity) {
        this.sessionAffinity = sessionAffinity;
    }

    @Override
    public String toString() {
        return "Service [id=" + getId() + ", selfLink=" + getSelfLink() + ", name=" + name + ", port=" + port
                + ", containerPort=" + containerPort + ", selector=" + selector + ", labels=" + labels + "]";
    }
}
