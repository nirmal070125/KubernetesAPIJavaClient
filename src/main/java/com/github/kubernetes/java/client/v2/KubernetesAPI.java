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
package com.github.kubernetes.java.client.v2;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.github.kubernetes.java.client.exceptions.KubernetesClientException;
import com.github.kubernetes.java.client.model.Label;
import com.github.kubernetes.java.client.model.Pod;
import com.github.kubernetes.java.client.model.PodList;
import com.github.kubernetes.java.client.model.ReplicationController;
import com.github.kubernetes.java.client.model.ReplicationControllerList;
import com.github.kubernetes.java.client.model.Service;
import com.github.kubernetes.java.client.model.ServiceList;

public interface KubernetesAPI {
	
	/* Pod API */

	/**
	 * Get information of a Pod given the PodID
	 * @param podId id of the pod
	 * @return {@link Pod}
	 * @throws KubernetesClientException
	 */
	@GET 
	@Path("/pods/{podId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)	
	public Pod getPod(@PathParam("podId") String podId) throws KubernetesClientException;
	
	/**
	 * Get all Pods
	 * @return Pods
	 * @throws KubernetesClientException
	 */
	@GET 
	@Path("/pods")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)		
	public PodList getAllPods() throws KubernetesClientException;
	
	/**
	 * Create a new Pod
	 * @param pod Pod to be created
	 * @throws KubernetesClientException
	 */
	@POST
	@Path("/pods")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)		
	public void createPod(Pod pod) throws KubernetesClientException;
	
	/**
	 * Delete a Pod
	 * @param podId Id of the Pod to be deleted
	 * @throws KubernetesClientException
	 */
	@DELETE
    @Path("/pods/{podId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void deletePod(@PathParam("podId") String podId) throws KubernetesClientException;
	
	/* Replication Controller API */
	
	/**
	 * Get a Replication Controller Info
	 * @param controllerId id of the Replication Controller
	 * @return {@link ReplicationController}
	 * @throws KubernetesClientException
	 */
	@GET
    @Path("/replicationControllers/{controllerId}")
    @Consumes(MediaType.APPLICATION_JSON)
	public ReplicationController getReplicationController(@PathParam("controllerId") String controllerId) throws KubernetesClientException;
	
	/**
	 * Get all Replication Controllers.
	 * @return {@link ReplicationController}s
	 * @throws KubernetesClientException
	 */
	@GET
    @Path("/replicationControllers")
	@Consumes(MediaType.APPLICATION_JSON)
	public ReplicationControllerList getAllReplicationControllers() throws KubernetesClientException;
	
	/**
	 * Create a new Replication Controller
	 * @param controller controller to be created
	 * @throws KubernetesClientException
	 */
	@POST
    @Path("/replicationControllers")
	@Consumes(MediaType.APPLICATION_JSON)
	public void createReplicationController(ReplicationController controller) throws KubernetesClientException;
	
	/**
     * Convenience method to update the number of replicas in a Replication Controller.
     * @param controllerId id of the controller to be updated
     * @param replicas update the replicas count of the current controller.
     * @throws KubernetesClientException
     */
    @PUT
    @Path("/replicationControllers/{controllerId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateReplicationController(@PathParam("controllerId") String controllerId, int replicas) throws KubernetesClientException;
	
    /**
     * Update a Replication Controller
     * @param controllerId id of the controller to be updated
     * @param controller controller to update (only the number of replicas can be updated).
     * @throws KubernetesClientException
     */
    @PUT
    @Path("/replicationControllers/{controllerId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateReplicationController(@PathParam("controllerId") String controllerId, ReplicationController replicationController) throws KubernetesClientException;
    
	/**
	 * Delete a Replication Controller.
	 * @param replication controller id controller id to be deleted.
	 * @throws KubernetesClientException
	 */
	@DELETE
    @Path("/replicationControllers/{controllerId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteReplicationController(@PathParam("controllerId") String controllerId) throws KubernetesClientException;
	
	/* Services API */
	
	/**
	 * Get the Service with the given id.
	 * @param serviceId id of the service.
	 * @return {@link Service}
	 * @throws KubernetesClientException
	 */
	@GET
	@Path("/services/{serviceId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Service getService(@PathParam("serviceId") String serviceId) throws KubernetesClientException;
	
	/**
	 * Get all the services.
	 * @return array of {@link Service}s
	 * @throws KubernetesClientException
	 */
	@GET
	@Path("/services")
	@Consumes(MediaType.APPLICATION_JSON)
	public ServiceList getAllServices() throws KubernetesClientException;
	
	/**
	 * Create a new Kubernetes service.
	 * @param service service to be created.
	 * @throws KubernetesClientException
	 */
	@POST
	@Path("/services")
	@Consumes(MediaType.APPLICATION_JSON)
	public void createService(Service service) throws KubernetesClientException;
	
	/**
	 * Delete a Service.
	 * @param serviceId service id to be deleted.
 	 * @throws KubernetesClientException
	 */
	@DELETE
    @Path("/services/{serviceId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteService(@PathParam("serviceId") String serviceId) throws KubernetesClientException;
	
	/**
     * Run a label query and retrieve a sub set of Pods.
     * @param labels parameter label=label1,label2,label3
     * @return Pods selected Pods by executing the label query.
     * @throws KubernetesClientException
     */
	@GET
	@Path("/pods")
	@Consumes(MediaType.APPLICATION_JSON)
    public PodList getSelectedPods(@QueryParam("labels") String labels) throws KubernetesClientException;
}
