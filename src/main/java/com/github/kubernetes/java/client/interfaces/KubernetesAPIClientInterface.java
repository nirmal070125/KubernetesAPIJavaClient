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
package com.github.kubernetes.java.client.interfaces;

import java.util.List;

import com.github.kubernetes.java.client.exceptions.KubernetesClientException;
import com.github.kubernetes.java.client.exceptions.Status;
import com.github.kubernetes.java.client.model.Label;
import com.github.kubernetes.java.client.model.Pod;
import com.github.kubernetes.java.client.model.PodList;
import com.github.kubernetes.java.client.model.ReplicationController;
import com.github.kubernetes.java.client.model.ReplicationControllerList;
import com.github.kubernetes.java.client.model.Service;
import com.github.kubernetes.java.client.model.ServiceList;

public interface KubernetesAPIClientInterface {

    public static final String VERSION = "v1beta2";

    /* Pod API */

    /**
     * Get information of a Pod given the PodID
     * 
     * @param podId
     *            id of the pod
     * @return {@link Pod}
     * @throws KubernetesClientException
     */
    public Pod getPod(String podId) throws KubernetesClientException;

    /**
     * Get all Pods
     * 
     * @return Pods
     * @throws KubernetesClientException
     */
    public PodList getAllPods() throws KubernetesClientException;

    /**
     * Create a new Pod
     * 
     * @param pod
     *            Pod to be created
     * @return
     * @throws KubernetesClientException
     */
    public Pod createPod(Pod pod) throws KubernetesClientException;

    /**
     * Delete a Pod
     * 
     * @param podId
     *            Id of the Pod to be deleted
     * @throws KubernetesClientException
     */
    public Status deletePod(String podId) throws KubernetesClientException;

    /* Replication Controller API */

    /**
     * Get a Replication Controller Info
     * 
     * @param controllerId
     *            id of the Replication Controller
     * @return {@link ReplicationController}
     * @throws KubernetesClientException
     */
    public ReplicationController getReplicationController(String controllerId) throws KubernetesClientException;

    /**
     * Get all Replication Controllers.
     * 
     * @return {@link ReplicationController}s
     * @throws KubernetesClientException
     */
    public ReplicationControllerList getAllReplicationControllers() throws KubernetesClientException;

    /**
     * Create a new Replication Controller
     * 
     * @param controller
     *            controller to be created
     * @throws KubernetesClientException
     */
    public ReplicationController createReplicationController(ReplicationController controller)
            throws KubernetesClientException;

    /**
     * Update a Replication Controller (update the number of replicas).
     * 
     * @param controllerId
     *            id of the controller to be updated
     * @param replicas
     *            update the replicas count of the current controller.
     * @throws KubernetesClientException
     */
    public ReplicationController updateReplicationController(String controllerId, int replicas)
            throws KubernetesClientException;

    /**
     * Delete a Replication Controller.
     * 
     * @param replication
     *            controller id controller id to be deleted.
     * @throws KubernetesClientException
     */
    public Status deleteReplicationController(String controllerId) throws KubernetesClientException;

    /* Services API */

    /**
     * Get the Service with the given id.
     * 
     * @param serviceId
     *            id of the service.
     * @return {@link Service}
     * @throws KubernetesClientException
     */
    public Service getService(String serviceId) throws KubernetesClientException;

    /**
     * Get all the services.
     * 
     * @return array of {@link Service}s
     * @throws KubernetesClientException
     */
    public ServiceList getAllServices() throws KubernetesClientException;

    /**
     * Create a new Kubernetes service.
     * 
     * @param service
     *            service to be created.
     * @throws KubernetesClientException
     */
    public Service createService(Service service) throws KubernetesClientException;

    /**
     * Delete a Service.
     * 
     * @param serviceId
     *            service id to be deleted.
     * @throws KubernetesClientException
     */
    public Status deleteService(String serviceId) throws KubernetesClientException;

    /**
     * Run a label query and retrieve a sub set of Pods.
     * 
     * @param label
     *            of labels for the label query
     * @return Pods selected Pods by executing the label query.
     * @throws KubernetesClientException
     */
    public PodList getSelectedPods(List<Label> labels) throws KubernetesClientException;
}
