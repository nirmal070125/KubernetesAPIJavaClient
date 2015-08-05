package com.github.kubernetes.java.client.v2;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.github.kubernetes.java.client.exceptions.KubernetesClientException;
import com.github.kubernetes.java.client.exceptions.Status;
import com.github.kubernetes.java.client.interfaces.KubernetesAPIClientInterface;
import com.github.kubernetes.java.client.model.Pod;
import com.github.kubernetes.java.client.model.PodList;
import com.github.kubernetes.java.client.model.ReplicationController;
import com.github.kubernetes.java.client.model.ReplicationControllerList;
import com.github.kubernetes.java.client.model.Service;
import com.github.kubernetes.java.client.model.ServiceList;
import com.google.common.base.Joiner;

public class KubernetesApiClient implements KubernetesAPIClientInterface {

    private static final Log LOG = LogFactory.getLog(KubernetesApiClient.class);

    private URI endpointURI;
    private KubernetesAPI api;

    public KubernetesApiClient(String endpointUrl, String username, String password) {
        this(endpointUrl, username, password, new RestFactory());
    }

    public KubernetesApiClient(String endpointUrl, String username, String password, RestFactory factory) {
        try {
            if (endpointUrl.matches("/api/v1[a-z0-9]+")) {
                LOG.warn("Deprecated: KubernetesApiClient endpointUrl should not include the /api/version section in "
                        + endpointUrl);
                endpointURI = new URI(endpointUrl);
            } else {
                endpointURI = new URI(endpointUrl + "/api/" + KubernetesAPIClientInterface.VERSION);
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        api = factory.createAPI(endpointURI, username, password);
    }

    public Pod getPod(String podId) throws KubernetesClientException {
        try {
            return api.getPod(podId);
        } catch (NotFoundException e) {
            return null;
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

    public PodList getAllPods() throws KubernetesClientException {
        try {
            return api.getAllPods();
        } catch (NotFoundException e) {
            return new PodList();
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

    public PodList getSelectedPods(Map<String, String> labels) throws KubernetesClientException {
        String param = Joiner.on(",").withKeyValueSeparator("=").join(labels);

        try {
            return api.getSelectedPods(param);
        } catch (NotFoundException e) {
            return new PodList();
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

    public Pod createPod(Pod pod) throws KubernetesClientException {
        try {
            return api.createPod(pod);
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

    public Status deletePod(String podId) throws KubernetesClientException {
        try {
            return api.deletePod(podId);
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

    public ReplicationController getReplicationController(String controllerId) throws KubernetesClientException {
        try {
            return api.getReplicationController(controllerId);
        } catch (NotFoundException e) {
            return null;
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

    public ReplicationControllerList getAllReplicationControllers() throws KubernetesClientException {
        try {
            return api.getAllReplicationControllers();
        } catch (NotFoundException e) {
            return new ReplicationControllerList();
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

    public ReplicationController createReplicationController(ReplicationController controller)
            throws KubernetesClientException {
        try {
            return api.createReplicationController(controller);
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

    public ReplicationController updateReplicationController(String controllerId, int replicas)
            throws KubernetesClientException {
        try {
            ReplicationController controller = api.getReplicationController(controllerId);
            controller.getDesiredState().setReplicas(replicas);
            return api.updateReplicationController(controllerId, controller);
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

    public ReplicationController updateReplicationController(String controllerId, ReplicationController controller)
            throws KubernetesClientException {
        try {
            return api.updateReplicationController(controllerId, controller);
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

    public Status deleteReplicationController(String controllerId) throws KubernetesClientException {
        try {
            return api.deleteReplicationController(controllerId);
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

    public Service getService(String serviceId) throws KubernetesClientException {
        try {
            return api.getService(serviceId);
        } catch (NotFoundException e) {
            return null;
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

    public ServiceList getAllServices() throws KubernetesClientException {
        try {
            return api.getAllServices();
        } catch (NotFoundException e) {
            return new ServiceList();
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

    public Service createService(Service service) throws KubernetesClientException {
        try {
            return api.createService(service);
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

    public Status deleteService(String serviceId) throws KubernetesClientException {
        try {
            return api.deleteService(serviceId);
        } catch (WebApplicationException e) {
            throw new KubernetesClientException(e);
        }
    }

}
