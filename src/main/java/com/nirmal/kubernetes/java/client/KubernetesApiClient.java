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
package com.nirmal.kubernetes.java.client;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

import com.nirmal.kubernetes.java.client.exceptions.KubernetesClientException;
import com.nirmal.kubernetes.java.client.interfaces.KubernetesAPIClientInterface;
import com.nirmal.kubernetes.java.client.model.Label;
import com.nirmal.kubernetes.java.client.model.Pod;
import com.nirmal.kubernetes.java.client.model.PodList;
import com.nirmal.kubernetes.java.client.model.ReplicationController;
import com.nirmal.kubernetes.java.client.model.ReplicationControllerList;
import com.nirmal.kubernetes.java.client.model.Service;
import com.nirmal.kubernetes.java.client.model.ServiceList;

public class KubernetesApiClient implements KubernetesAPIClientInterface {
	
	private String endpointUrl;
	private static final Log log = LogFactory.getLog(KubernetesApiClient.class);
	
	public KubernetesApiClient(String endpointUrl) {
		this.endpointUrl = endpointUrl;
	}

	
	public Pod getPod(String podId) throws KubernetesClientException{
		try {
			ClientRequest request = new ClientRequest(endpointUrl+"pods/{podId}");
			ClientResponse<Pod> res = request.pathParameter("podId", podId).get(Pod.class);
			
			handleNullResponse("Pod ["+podId+"] retrieval failed.", res);
			
			if (res.getResponseStatus().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
				String msg = "Pod ["+podId+"] doesn't exist.";
				log.error(msg);
				throw new KubernetesClientException(msg);
			}
			return res.getEntity();
		} catch (KubernetesClientException e) {
            throw e;
        } catch (Exception e) {
			String msg = "Error while retrieving Pod info with Pod ID: "+podId;
			log.error(msg, e);
			throw new KubernetesClientException(msg, e);
		}
	}
	
	
	public PodList getAllPods() throws KubernetesClientException {
		
		try {
			ClientRequest request = new ClientRequest(endpointUrl+"pods/");
			ClientResponse<PodList> res = request.get(PodList.class);
			handleNullResponse("Pod retrieval failed.", res);
			if (res.getResponseStatus().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
				return new PodList();
			}
			PodList podList = new PodList();
			podList.setItems(res.getEntity().getItems());
			return podList;
		} catch (Exception e) {
			String msg = "Error while retrieving Pods.";
			log.error(msg, e);
			throw new KubernetesClientException(msg, e);
		}
	}

	
	public void createPod(Pod pod) throws KubernetesClientException {

		try {
			ClientRequest request = new ClientRequest(endpointUrl+"pods");
			ClientResponse<?> res = request.body("application/json", pod).post();
			
			handleNullResponse("Pod creation failed.", res);
			
			if (res.getResponseStatus().getStatusCode() == HttpStatus.SC_CONFLICT) {
                log.warn("Pod already created. "+pod);
                return;
            }
			
			if (res.getResponseStatus().getStatusCode() != HttpStatus.SC_ACCEPTED && 
			        res.getResponseStatus().getStatusCode() != HttpStatus.SC_OK) {
				String msg = "Pod ["+pod+"] creation failed. Error: "+
								res.getResponseStatus().getReasonPhrase();
				log.error(msg);
				throw new KubernetesClientException(msg);
			}
		} catch (KubernetesClientException e) {
            throw e;
        } catch (Exception e) {
			String msg = "Error while creating Pod: "+pod;
			log.error(msg, e);
			throw new KubernetesClientException(msg, e);
		}
	}

	
	public void deletePod(String podId) throws KubernetesClientException {

		try {
			ClientRequest request = new ClientRequest(endpointUrl+"pods/{podId}");
			ClientResponse<?> res = request.pathParameter("podId", podId).delete();
			
			handleNullResponse("Pod ["+podId+"] deletion failed.", res);
			
			if (res.getResponseStatus().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                String msg = "Pod ["+podId+"] doesn't exist.";
                log.error(msg);
                throw new KubernetesClientException(msg);
            }
			
			if (res.getResponseStatus().getStatusCode() != HttpStatus.SC_ACCEPTED && 
			        res.getResponseStatus().getStatusCode() != HttpStatus.SC_OK) {
				String msg = "Pod ["+podId+"] deletion failed. Error: "+
								res.getResponseStatus().getReasonPhrase();
				log.error(msg);
				throw new KubernetesClientException(msg);
			}
		} catch (KubernetesClientException e) {
            throw e;
        } catch (Exception e) {
			String msg = "Error while retrieving Pod info of Pod ID: "+podId;
			log.error(msg, e);
			throw new KubernetesClientException(msg, e);
		}
	}

	
	public ReplicationController getReplicationController(String controllerId)
			throws KubernetesClientException {

		try {
			ClientRequest request = new ClientRequest(endpointUrl+"replicationControllers/{controllerId}");
			ClientResponse<ReplicationController> res = request.pathParameter("controllerId", controllerId)
					.get(ReplicationController.class);
			
			handleNullResponse("Replication Controller ["+controllerId+"] retrieval failed.", res);
			
			if (res.getResponseStatus().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
				String msg = "Replication Controller ["+controllerId+"] doesn't exist.";
				log.error(msg);
				throw new KubernetesClientException(msg);
			}
			return res.getEntity();
		} catch (KubernetesClientException e) {
            throw e;
        } catch (Exception e) {
			String msg = "Error while retrieving Replication Controller info with ID: "+controllerId;
			log.error(msg, e);
			throw new KubernetesClientException(msg, e);
		}
	}

	
	public ReplicationController[] getAllReplicationControllers()
			throws KubernetesClientException {
		
		try {
			ClientRequest request = new ClientRequest(endpointUrl+"replicationControllers/");
			ClientResponse<ReplicationControllerList> res = request.get(ReplicationControllerList.class);
			
			handleNullResponse("Replication Controller retrieval failed.", res);
			
			if (res.getResponseStatus().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
				return new ReplicationController[0];
			}
			return res.getEntity().getItems();
		} catch (Exception e) {
			String msg = "Error while retrieving Replication Controllers.";
			log.error(msg, e);
			throw new KubernetesClientException(msg, e);
		}
	}

	
	public void createReplicationController(ReplicationController controller)
			throws KubernetesClientException {

		try {
			ClientRequest request = new ClientRequest(endpointUrl
					+ "replicationControllers/");
			ClientResponse<?> res = request
					.body("application/json", controller).post();
			
			handleNullResponse("Replication Controller ["+controller.getId()+"] creation failed.", res);
			
			if (res.getResponseStatus().getStatusCode() == HttpStatus.SC_CONFLICT) {
                log.warn("Replication Controller already created. "+controller);
                return;
            }

			if (res.getResponseStatus().getStatusCode() != HttpStatus.SC_ACCEPTED && 
                    res.getResponseStatus().getStatusCode() != HttpStatus.SC_OK ) {
				String msg = "Replication Controller [" + controller
						+ "] creation failed. Error: "
						+ res.getResponseStatus().getReasonPhrase();
				log.error(msg);
				throw new KubernetesClientException(msg);
			}
		} catch (KubernetesClientException e) {
            throw e;
        } catch (Exception e) {
			String msg = "Error while creating Replication Controller: "
					+ controller;
			log.error(msg, e);
			throw new KubernetesClientException(msg, e);

		}
	}
	
    public void updateReplicationController(String controllerId, int replicas) throws KubernetesClientException {

        ReplicationController controller = getReplicationController(controllerId);
        
        try {
            // update the number of replicas
            controller.getDesiredState().setReplicas(replicas);
            
            ClientRequest request = new ClientRequest(endpointUrl
                    + "replicationControllers/"+controllerId);
            ClientResponse<?> res = request
                    .body("application/json", controller).put();
            
            handleNullResponse("Replication Controller ["+controllerId+"] update failed.", res);
            
            if (res.getResponseStatus().getStatusCode() == HttpStatus.SC_CONFLICT) {
                log.warn("Replication Controller conflicts. "+controller);
                return;
            }
            
            if (res.getResponseStatus().getStatusCode() != HttpStatus.SC_OK && 
                    res.getResponseStatus().getStatusCode() != HttpStatus.SC_ACCEPTED ) {
                String msg = "Replication Controller [" + controller
                        + "] update failed. Error: "
                        + res.getResponseStatus().getReasonPhrase();
                log.error(msg);
                throw new KubernetesClientException(msg);
            }
        } catch (KubernetesClientException e) {
            throw e;
        } catch (Exception e) {
            String msg = "Error while updating Replication Controller: "
                    + controller;
            log.error(msg, e);
            throw new KubernetesClientException(msg, e);

        }
    }
	
	public void deleteReplicationController(String controllerId)
			throws KubernetesClientException {
		
		try {
			ClientRequest request = new ClientRequest(endpointUrl+"replicationControllers/{controllerId}");
			ClientResponse<?> res = request.pathParameter("controllerId", controllerId).delete();
			
			handleNullResponse("Replication Controller ["+controllerId+"] deletion failed.", res);
			
			if (res.getResponseStatus().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                String msg = "Replication Controller ["+controllerId+"] doesn't exist.";
                log.error(msg);
                throw new KubernetesClientException(msg);
            }
			
			if (res.getResponseStatus().getStatusCode() != HttpStatus.SC_ACCEPTED && 
                    res.getResponseStatus().getStatusCode() != HttpStatus.SC_OK) {
				String msg = "Replication Controller ["+controllerId+"] deletion failed. Error: "+
								res.getResponseStatus().getReasonPhrase();
				log.error(msg);
				throw new KubernetesClientException(msg);
			}
		} catch (KubernetesClientException e) {
            throw e;
        } catch (Exception e) {
			String msg = "Error while retrieving Replication Controller info of Controller ID: "+controllerId;
			log.error(msg, e);
			throw new KubernetesClientException(msg, e);
		}
	}

	
	public Service getService(String serviceId)
			throws KubernetesClientException {
		try {
			ClientRequest request = new ClientRequest(endpointUrl+"services/{serviceId}");
			ClientResponse<Service> res = request.pathParameter("serviceId", serviceId).get(Service.class);
			
			handleNullResponse("Service ["+serviceId+"] retrieval failed.", res);
			
			if (res.getResponseStatus().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
				String msg = "Service ["+serviceId+"] doesn't exist.";
				log.error(msg);
				throw new KubernetesClientException(msg);
			}
			return res.getEntity();
		} catch (KubernetesClientException e) {
            throw e;
        } catch (Exception e) {
			String msg = "Error while retrieving Service info with Service ID: "+serviceId;
			log.error(msg, e);
			throw new KubernetesClientException(msg, e);
		}
	}

	
	public ServiceList getAllServices() throws KubernetesClientException {
		try {
			ClientRequest request = new ClientRequest(endpointUrl+"services/");
			ClientResponse<ServiceList> res = request.get(ServiceList.class);
			
			handleNullResponse("Service retrieval failed.", res);
			
			if (res.getResponseStatus().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
				return new ServiceList();
			}
			ServiceList serviceList = new ServiceList();
			serviceList.setItems(res.getEntity().getItems());
			return serviceList;
		} catch (Exception e) {
			String msg = "Error while retrieving Services.";
			log.error(msg, e);
			throw new KubernetesClientException(msg, e);
		}
	}

	
	public void createService(Service service) throws KubernetesClientException {

		try {
			ClientRequest request = new ClientRequest(endpointUrl+"services/");
			ClientResponse<?> res = request.body("application/json", service).post();
			
			handleNullResponse("Service ["+service.getId()+"] creation failed.", res);
			
			if (res.getResponseStatus().getStatusCode() == HttpStatus.SC_CONFLICT) {
                log.warn("Service already created. "+service);
                return;
            }
			
			if (res.getResponseStatus().getStatusCode() != HttpStatus.SC_ACCEPTED && 
                    res.getResponseStatus().getStatusCode() != HttpStatus.SC_OK) {
				String msg = "Service ["+service+"] creation failed. Error: "+
								res.getResponseStatus().getReasonPhrase();
				log.error(msg);
				throw new KubernetesClientException(msg);
			}
		} catch (KubernetesClientException e) {
            throw e;
        } catch (Exception e) {
			String msg = "Error while creating the Service: "+service;
			log.error(msg, e);
			throw new KubernetesClientException(msg, e);
		}
	}

	
	public void deleteService(String serviceId)
			throws KubernetesClientException {

		try {
			ClientRequest request = new ClientRequest(endpointUrl+"services/{serviceId}");
			ClientResponse<?> res = request.pathParameter("serviceId", serviceId).delete();
			
			handleNullResponse("Service ["+serviceId+"] deletion failed.", res);
			
			if (res.getResponseStatus().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                String msg = "Service ["+serviceId+"] doesn't exist.";
                log.error(msg);
                throw new KubernetesClientException(msg);
            }
			
			if (res.getResponseStatus().getStatusCode() != HttpStatus.SC_ACCEPTED && 
                    res.getResponseStatus().getStatusCode() != HttpStatus.SC_OK) {
				String msg = "Service ["+serviceId+"] deletion failed. Error: "+
								res.getResponseStatus().getReasonPhrase();
				log.error(msg);
				throw new KubernetesClientException(msg);
			}
		} catch (KubernetesClientException e) {
            throw e;
        } catch (Exception e) {
			String msg = "Error while retrieving Service info of Service ID: "+serviceId;
			log.error(msg, e);
			throw new KubernetesClientException(msg, e);
		}
	}


    public PodList getSelectedPods(Label[] label) throws KubernetesClientException {
        try {
            String labelQuery = getLabelQuery(label);
            ClientRequest request = new ClientRequest(endpointUrl+"pods");
            ClientResponse<PodList> res = request.queryParameter("labels", labelQuery).get(PodList.class);
            
            handleNullResponse("Pod retrieval failed.", res);
            
            if (res.getResponseStatus().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                return new PodList();
            }
            PodList podList = new PodList();
            podList.setItems(res.getEntity().getItems());
            return podList;
        } catch (Exception e) {
            String msg = "Error while selecting pods for : "+Arrays.toString(label);
            log.error(msg, e);
            throw new KubernetesClientException(msg, e);
        }
    }
    
    private String getLabelQuery(Label[] label) {
        String query = "";
        for (Label l : label) {
            query = query.concat("name="+l.getName()+",");
        }
        return query.endsWith(",") ? query.substring(0, query.length()-1) : query;
    }
    
    private void handleNullResponse(String message, ClientResponse<?> res)
            throws KubernetesClientException {
        if (res == null || res.getResponseStatus() == null) {
            log.error(message+ " Null response received.");
            throw new KubernetesClientException(message);
        }
    }

}
