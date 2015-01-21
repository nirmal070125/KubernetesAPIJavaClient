package com.github.kubernetes.java.client.v2;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.NotFoundException;

import com.github.kubernetes.java.client.exceptions.KubernetesClientException;
import com.github.kubernetes.java.client.interfaces.KubernetesAPIClientInterface;
import com.github.kubernetes.java.client.model.Label;
import com.github.kubernetes.java.client.model.Pod;
import com.github.kubernetes.java.client.model.PodList;
import com.github.kubernetes.java.client.model.ReplicationController;
import com.github.kubernetes.java.client.model.ReplicationControllerList;
import com.github.kubernetes.java.client.model.Service;
import com.github.kubernetes.java.client.model.ServiceList;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class KubernetesApiClient implements KubernetesAPIClientInterface {

    private URI endpointURI;
    private KubernetesAPI api;

    public KubernetesApiClient(String endpointUrl, String username, String password) {
        this(endpointUrl, username, password, new RestFactory());
    }

    public KubernetesApiClient(String endpointUrl, String username, String password, RestFactory factory) {
        try {
            endpointURI = new URI(endpointUrl);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        api = factory.createAPI(endpointURI, username, password);
    }

    public Pod getPod(String podId) throws KubernetesClientException {
        try {
            return api.getPod(podId);
        } catch (NotFoundException e) {
            throw new KubernetesClientException("Pod [" + podId + "] doesn't exist.");
        } catch (ClientErrorException e) {
            throw new KubernetesClientException(e);
        }
    }

    public PodList getAllPods() throws KubernetesClientException {
        try {
            return api.getAllPods();
        } catch (NotFoundException e) {
            return new PodList();
        } catch (ClientErrorException e) {
            throw new KubernetesClientException(e);
        }
    }

    public PodList getSelectedPods(List<Label> labels) throws KubernetesClientException {
        Function<Label, String> f = new Function<Label, String>() {
            public String apply(Label l) {
                return "name=" + l.getName();
            }
        };
        String param = Joiner.on(',').join(Lists.transform(labels, f));

        try {
            return api.getSelectedPods(param);
        } catch (NotFoundException e) {
            return new PodList();
        } catch (ClientErrorException e) {
            throw new KubernetesClientException(e);
        }
    }

    public void createPod(Pod pod) throws KubernetesClientException {
        try {
            api.createPod(pod);
        } catch (ClientErrorException e) {
            throw new KubernetesClientException(e);
        }
    }

    public void deletePod(String podId) throws KubernetesClientException {
        try {
            api.deletePod(podId);
        } catch (NotFoundException e) {
            throw new KubernetesClientException("Pod [" + podId + "] doesn't exist.");
        } catch (ClientErrorException e) {
            throw new KubernetesClientException(e);
        }
    }

    public ReplicationController getReplicationController(String controllerId) throws KubernetesClientException {
        try {
            return api.getReplicationController(controllerId);
        } catch (NotFoundException e) {
            throw new KubernetesClientException("Replication Controller [" + controllerId + "] doesn't exist.");
        } catch (ClientErrorException e) {
            throw new KubernetesClientException(e);
        }
    }

    public ReplicationControllerList getAllReplicationControllers() throws KubernetesClientException {
        try {
            return api.getAllReplicationControllers();
        } catch (NotFoundException e) {
            return new ReplicationControllerList();
        } catch (ClientErrorException e) {
            throw new KubernetesClientException(e);
        }
    }

    public void createReplicationController(ReplicationController controller) throws KubernetesClientException {
        try {
            api.createReplicationController(controller);
        } catch (ClientErrorException e) {
            throw new KubernetesClientException(e);
        }
    }

    public void updateReplicationController(String controllerId, int replicas) throws KubernetesClientException {
        ReplicationController controller = getReplicationController(controllerId);
        controller.getDesiredState().setReplicas(replicas);
        try {
            api.updateReplicationController(controllerId, controller);
        } catch (ClientErrorException e) {
            String msg = "Replication Controller [" + controllerId + "] update failed. Error: " + e.getMessage();
            throw new KubernetesClientException( msg, e );
        }
    }

    public void deleteReplicationController(String controllerId) throws KubernetesClientException {
        try {
            api.deleteReplicationController(controllerId);
        } catch (NotFoundException e) {
            throw new KubernetesClientException("Replication Controller [" + controllerId + "] doesn't exist.");
        } catch (ClientErrorException e) {
            throw new KubernetesClientException(e);
        }
    }

    public Service getService(String serviceId) throws KubernetesClientException {
        try {
            return api.getService(serviceId);
        } catch (NotFoundException e) {
            throw new KubernetesClientException("Service [" + serviceId + "] doesn't exist.");
        } catch (ClientErrorException e) {
            throw new KubernetesClientException(e);
        }
    }

    public ServiceList getAllServices() throws KubernetesClientException {
        try {
            return api.getAllServices();
        } catch (NotFoundException e) {
            return new ServiceList();
        } catch (ClientErrorException e) {
            throw new KubernetesClientException(e);
        }
    }

    public void createService(Service service) throws KubernetesClientException {
        try {
            api.createService(service);
        } catch (ClientErrorException e) {
            throw new KubernetesClientException(e);
        }
    }

    public void deleteService(String serviceId) throws KubernetesClientException {
        try {
            api.deleteService(serviceId);
        } catch (NotFoundException e) {
            throw new KubernetesClientException("Service [" + serviceId + "] doesn't exist.");
        } catch (ClientErrorException e) {
            throw new KubernetesClientException(e);
        }
    }

}
