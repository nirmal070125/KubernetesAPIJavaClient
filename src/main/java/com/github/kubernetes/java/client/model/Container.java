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

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Container {

    private String name;
    private String image;
    private String imagePullPolicy;
    private String workingDir;
    private List<String> command;
    private List<VolumeMount> volumeMounts;
    private List<Port> ports;
    private List<EnvironmentVariable> env;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImagePullPolicy() {
        return imagePullPolicy;
    }

    public void setImagePullPolicy(String pullPolicy) {
        this.imagePullPolicy = pullPolicy;
    }

    public String getWorkingDir() {
        return workingDir;
    }

    public void setWorkingDir(String workingDir) {
        this.workingDir = workingDir;
    }

    @JsonProperty
    public List<String> getCommand() {
        return command;
    }

    public void setCommand(List<String> command) {
        this.command = command;
    }

    @JsonIgnore
    public void setCommand(String... command) {
        this.command = Arrays.asList(command);
    }

    public List<VolumeMount> getVolumeMounts() {
        return volumeMounts;
    }

    public void setVolumeMounts(List<VolumeMount> volumeMounts) {
        this.volumeMounts = volumeMounts;
    }

    @JsonProperty
    public List<Port> getPorts() {
        return ports;
    }

    public void setPorts(List<Port> ports) {
        this.ports = ports;
    }

    @JsonIgnore
    public void setPorts(Port... ports) {
        this.ports = Arrays.asList(ports);
    }

    public List<EnvironmentVariable> getEnv() {
        return env;
    }

    public void setEnv(List<EnvironmentVariable> env) {
        this.env = env;
    }

    @Override
    public String toString() {
        return "Container [name=" + name + ", image=" + image + ", workingDir=" + workingDir + ", command="
                + StringUtils.join(command, ",") + ", volumeMounts=" + StringUtils.join(volumeMounts, ",") + ", ports="
                + StringUtils.join(ports, ",") + ", env=" + StringUtils.join(env, ",") + "]";
    }

}
