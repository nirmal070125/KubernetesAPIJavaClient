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
package org.apache.stratos.kubernetes.api.client.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.stratos.kubernetes.api.exceptions.KubernetesClientException;
import org.apache.stratos.kubernetes.api.model.Pod;
import org.apache.stratos.kubernetes.api.model.PodList;
import org.apache.stratos.kubernetes.api.model.ReplicationController;
import org.apache.stratos.kubernetes.api.model.Service;
import org.apache.stratos.kubernetes.api.model.ServiceList;

public interface KubernetesAPIClientInterface {
	
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
	public Pod getPod(String podId) throws KubernetesClientException;
	
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
	@Consumes(MediaType.APPLICATION_JSON)
	public void deletePod(String podId) throws KubernetesClientException;
	
	/* Replication Controller API */
	
	/**
	 * Get a Replication Controller Info
	 * @param controllerId id of the Replication Controller
	 * @return {@link ReplicationController}
	 * @throws KubernetesClientException
	 */
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public ReplicationController getReplicationController(String controllerId) throws KubernetesClientException;
	
	/**
	 * Get all Replication Controllers.
	 * @return {@link ReplicationController}s
	 * @throws KubernetesClientException
	 */
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public ReplicationController[] getAllReplicationControllers() throws KubernetesClientException;
	
	/**
	 * Create a new Replication Controller
	 * @param controller controller to be created
	 * @throws KubernetesClientException
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void createReplicationController(ReplicationController controller) throws KubernetesClientException;
	
	/**
	 * Delete a Replication Controller.
	 * @param replication controller id controller id to be deleted.
	 * @throws KubernetesClientException
	 */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteReplicationController(String controllerId) throws KubernetesClientException;
	
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
	public Service getService(String serviceId) throws KubernetesClientException;
	
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
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteService(String serviceId) throws KubernetesClientException;
}
