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
package org.apache.stratos.kubernetes.api.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;
import org.apache.stratos.kubernetes.api.client.interfaces.KubernetesAPIClientInterface;
import org.apache.stratos.kubernetes.api.exceptions.KubernetesClientException;
import org.apache.stratos.kubernetes.api.model.Pod;
import org.apache.stratos.kubernetes.api.model.PodList;
import org.apache.stratos.kubernetes.api.model.ReplicationController;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

public class KubernetesApiClient implements KubernetesAPIClientInterface {
	
	private String endpointUrl;
	private static final Log log = LogFactory.getLog(KubernetesApiClient.class);
	
	public KubernetesApiClient(String endpointUrl) {
		this.endpointUrl = endpointUrl;
	}

	@Override
	public Pod getPod(String podId) throws KubernetesClientException{
		try {
			ClientRequest request = new ClientRequest(endpointUrl+"pods/{podId}");
			ClientResponse<Pod> res = request.pathParameter("podId", podId).get(Pod.class);
			if (res.getEntity() == null ) {
				String msg = "Pod ["+podId+"] doesn't exist.";
				log.error(msg);
				throw new KubernetesClientException(msg);
			}
			return res.getEntity();
		} catch (Exception e) {
			String msg = "Error while retrieving Pod info with Pod ID: "+podId;
			log.error(msg, e);
			throw new KubernetesClientException(msg, e);
		}
	}
	
	@Override
	public Pod[] getAllPods() throws KubernetesClientException {
		
		try {
			ClientRequest request = new ClientRequest(endpointUrl+"pods/");
			ClientResponse<PodList> res = request.get(PodList.class);
			if (res.getEntity() == null ) {
				return new Pod[0];
			}
			return res.getEntity().getItems();
		} catch (Exception e) {
			String msg = "Error while retrieving Pods.";
			log.error(msg, e);
			throw new KubernetesClientException(msg, e);
		}
	}

	@Override
	public void createPod(Pod pod) throws KubernetesClientException {

		try {
			ClientRequest request = new ClientRequest(endpointUrl+"pods");
			ClientResponse<?> res = request.body("application/json", pod).post();
			
			if (res.getResponseStatus().getStatusCode() != HttpStatus.SC_ACCEPTED) {
				String msg = "Pod ["+pod+"] creation failed. Error: "+
								res.getResponseStatus().getReasonPhrase();
				log.error(msg);
				throw new KubernetesClientException(msg);
			}
		} catch (Exception e) {
			String msg = "Error while creating Pod: "+pod;
			log.error(msg, e);
			throw new KubernetesClientException(msg, e);
		}
	}

	@Override
	public void deletePod(String podId) throws KubernetesClientException {

		try {
			ClientRequest request = new ClientRequest(endpointUrl+"pods/{podId}");
			ClientResponse<?> res = request.pathParameter("podId", podId).delete();
			if (res.getResponseStatus().getStatusCode() != HttpStatus.SC_ACCEPTED) {
				String msg = "Pod ["+podId+"] deletion failed. Error: "+
								res.getResponseStatus().getReasonPhrase();
				log.error(msg);
				throw new KubernetesClientException(msg);
			}
		} catch (Exception e) {
			String msg = "Error while retrieving Pod info with Pod ID: "+podId;
			log.error(msg, e);
			throw new KubernetesClientException(msg, e);
		}
	}

	@Override
	public ReplicationController getReplicationController(String controllerId)
			throws KubernetesClientException {

		try {
			ClientRequest request = new ClientRequest(endpointUrl+"replicationControllers/{controllerId}");
			ClientResponse<ReplicationController> res = request.pathParameter("controllerId", controllerId)
					.get(ReplicationController.class);
			if (res.getEntity() == null ) {
				String msg = "Replication Controller ["+controllerId+"] doesn't exist.";
				log.error(msg);
				throw new KubernetesClientException(msg);
			}
			return res.getEntity();
		} catch (Exception e) {
			String msg = "Error while retrieving Replication Controller info with ID: "+controllerId;
			log.error(msg, e);
			throw new KubernetesClientException(msg, e);
		}
	}

}
