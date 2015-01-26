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

public class Port {

    private String name;
    private String protocol;
    private int containerPort;
    private int hostPort;
    private String hostIp;

    public Port() {
    }

    /**
     * Create a port binding with host and container ports.
     * 
     * Container port alone is useless and not bound
     * https://github.com/GoogleCloudPlatform/kubernetes/blob/dd4524/pkg/kubelet/kubelet.go#L493
     * 
     * @param containerPort
     * @param hostPort
     */
    public Port(int containerPort, int hostPort) {
        this.containerPort = containerPort;
        this.hostPort = hostPort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * TCP or UDP
     */
    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getContainerPort() {
        return containerPort;
    }

    public void setContainerPort(int containerPort) {
        this.containerPort = containerPort;
    }

    public int getHostPort() {
        return hostPort;
    }

    public void setHostPort(int hostPort) {
        this.hostPort = hostPort;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    @Override
    public String toString() {
        return "Port [name=" + name + ", protocol=" + protocol + ", containerPort=" + containerPort + ", hostPort="
                + hostPort + ", hostIp=" + hostIp + "]";
    }

}
