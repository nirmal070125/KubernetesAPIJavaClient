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
package com.github.kubernetes.java.client.live;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.github.kubernetes.java.client.exceptions.KubernetesClientException;
import com.github.kubernetes.java.client.exceptions.Status;
import com.github.kubernetes.java.client.interfaces.KubernetesAPIClientInterface;
import com.github.kubernetes.java.client.model.AbstractKubernetesModel;
import com.github.kubernetes.java.client.model.Container;
import com.github.kubernetes.java.client.model.Label;
import com.github.kubernetes.java.client.model.Manifest;
import com.github.kubernetes.java.client.model.Pod;
import com.github.kubernetes.java.client.model.PodList;
import com.github.kubernetes.java.client.model.Port;
import com.github.kubernetes.java.client.model.ReplicationController;
import com.github.kubernetes.java.client.model.Selector;
import com.github.kubernetes.java.client.model.Service;
import com.github.kubernetes.java.client.model.ServiceList;
import com.github.kubernetes.java.client.model.State;
import com.github.kubernetes.java.client.v2.KubernetesApiClient;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;

@Category(com.github.kubernetes.java.client.LiveTests.class)
public class KubernetesApiClientLiveTest {

    private static final Log log = LogFactory.getLog(KubernetesApiClientLiveTest.class);

    private String dockerImage;
    protected String endpoint;
    protected String username;
    protected String password;

    protected Pod pod;
    protected ReplicationController contr;
    protected Service serv;

    private KubernetesAPIClientInterface client;

    protected Class getExceptionClass() {
        return KubernetesClientException.class;
    }

    protected KubernetesAPIClientInterface getClient() {
        if (client == null) {
            client = new KubernetesApiClient(endpoint, username, password);
        }
        return client;
    }

    @Before
    public void setUp() {
        endpoint = System.getProperty("kubernetes.api.endpoint", "http://192.168.1.100:8080/api/"
                + KubernetesAPIClientInterface.VERSION + "/");
        username = System.getProperty("kubernetes.api.username", "vagrant");
        password = System.getProperty("kubernetes.api.password", "vagrant");
        log.info("Provided Kubernetes endpoint using system property [kubernetes.api.endpoint] : " + endpoint);

        // image should be pre-downloaded for ease of testing.
        dockerImage = System.getProperty("docker.image", "busybox");

        pod = getPod();
        contr = getReplicationController();
        serv = getService();

        // cleanup
        cleanup();
    }

    @After
    public void cleanup() {
        Function<AbstractKubernetesModel, Status> delete = new Function<AbstractKubernetesModel, Status>() {
            public Status apply(AbstractKubernetesModel o) {
                switch (o.getKind()) {
                case POD:
                    return getClient().deletePod(pod.getId());
                case REPLICATIONCONTROLLER:
                    if (getClient().getReplicationController(contr.getId()) != null) {
                        getClient().updateReplicationController(contr.getId(), 0);
                    }
                    PodList pods = getClient().getSelectedPods(Collections.singletonList(contr.getLabels()));
                    for (Pod pod : pods.getItems()) {
                        getClient().deletePod(pod.getId());
                    }
                    return getClient().deleteReplicationController(contr.getId());
                case SERVICE:
                    return getClient().deleteService(serv.getId());
                default:
                    throw new IllegalArgumentException(o.toString());
                }
            }
        };

        for (AbstractKubernetesModel model : ImmutableList.of(pod, contr, serv)) {
            try {
                delete.apply(model);
            } catch (KubernetesClientException e) {
                if ((e.getStatus() == null) || (e.getStatus().getCode() != 404)) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Pod getPod() {
        Pod pod = new Pod();
        pod.setId("kubernetes-test-pod");
        pod.setLabels(new Label("kubernetes-test-pod-label"));
        State desiredState = new State();
        Manifest m = new Manifest();
        m.setId(pod.getId());
        Container c = new Container();
        c.setName("master");
        c.setImage(dockerImage);
        c.setCommand(Collections.singletonList("tail -f /dev/null"));
        Port p = new Port();
        p.setContainerPort(8379);
        p.setHostPort(8379);
        c.setPorts(Collections.singletonList(p));
        m.setContainers(Collections.singletonList(c));
        desiredState.setManifest(m);
        pod.setDesiredState(desiredState);
        return pod;
    }

    private ReplicationController getReplicationController() {
        ReplicationController contr = new ReplicationController();
        contr.setId("kubernetes-test-controller");
        State desiredState = new State();
        desiredState.setReplicas(2);

        Selector selector = new Selector();
        selector.setName("kubernetes-test-controller-selector");
        desiredState.setReplicaSelector(selector);

        Pod podTemplate = new Pod();
        State podState = new State();
        Manifest manifest = new Manifest();
        manifest.setId(contr.getId());
        Container container = new Container();
        container.setName("kubernetes-test");
        container.setImage(dockerImage);
        Port p = new Port();
        p.setContainerPort(80);
        container.setPorts(Collections.singletonList(p));
        container.setCommand(Collections.singletonList("tail -f /dev/null"));
        manifest.setContainers(Collections.singletonList(container));
        podState.setManifest(manifest);
        podTemplate.setDesiredState(podState);
        podTemplate.setLabels(new Label(selector.getName()));

        desiredState.setPodTemplate(podTemplate);
        contr.setDesiredState(desiredState);
        contr.setLabels(podTemplate.getLabels());
        return contr;
    }

    private Service getService() {
        Service serv = new Service();
        serv.setContainerPort("8379");
        serv.setPort(5000);
        serv.setId("kubernetes-test-service");
        serv.setLabels(new Label("kubernetes-test-service-label"));
        serv.setName("kubernetes-test-service-name");
        Selector selector = new Selector();
        selector.setName(serv.getLabels().getName());
        serv.setSelector(selector);
        return serv;
    }

    @Test
    public void testCreatePod() throws Exception {
        log.info("Testing Pods ....");

        if (log.isDebugEnabled()) {
            log.debug("Creating a Pod " + pod);
        }
        Pod createPod = getClient().createPod(pod);
        assertEquals(pod.getId(), createPod.getId());
        assertNotNull(getClient().getPod(pod.getId()));

        // give 2s to download the image
        Thread.sleep(2000);

        // test recreation from same id
        try {
            getClient().createPod(pod);
            fail("Should have thrown exception");
        } catch (Exception e) {
            // ignore
        }
        assertNotNull(getClient().getPod(pod.getId()));
    }

    @Test
    public void testGetNonExistantPod() throws Exception {
        assertNull(getClient().getPod("non-existant"));
    }

    @Test
    public void testGetAllPods() throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Get all Pods ");
        }
        getClient().createPod(pod);
        PodList podList = getClient().getAllPods();
        assertNotNull(podList);
        List<Pod> currentPods;
        assertNotNull(currentPods = podList.getItems());
        boolean match = false;
        for (Pod pod2 : currentPods) {
            if (pod.getId().equals(pod2.getId())) {
                match = true;
                break;
            }
        }
        assertEquals(true, match);
    }

    @Test
    public void testGetSelectedPods() throws Exception {
        getClient().createPod(pod);
        PodList selectedPods = getClient().getSelectedPods(ImmutableList.of(pod.getLabels()));
        assertNotNull(selectedPods);
        assertNotNull(selectedPods.getItems());
        assertEquals(1, selectedPods.getItems().size());
    }

    @Test
    public void testGetSelectedPodsEmpty() {
        PodList selectedPods = getClient().getSelectedPods(ImmutableList.of(pod.getLabels()));
        assertNotNull(selectedPods);
        assertNotNull(selectedPods.getItems());
        assertEquals(0, selectedPods.getItems().size());
    }

    @Test
    public void testGetSelectedPodsWithNonExistantLabel() throws Exception {
        PodList selectedPods = getClient().getSelectedPods(ImmutableList.of(pod.getLabels(), new Label("no-match")));
        assertNotNull(selectedPods);
        assertNotNull(selectedPods.getItems());
        assertEquals(0, selectedPods.getItems().size());
    }

    @Test
    public void testGetSelectedPodsWithEmptyLabel() throws Exception {
        PodList selectedPods = getClient().getSelectedPods(Collections.<Label> emptyList());
        PodList allPods = getClient().getAllPods();
        assertNotNull(selectedPods);
        assertNotNull(selectedPods.getItems());
        assertEquals(allPods.getItems().size(), selectedPods.getItems().size());
    }

    @Test
    public void testDeletePod() throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Deleting a Pod " + pod);
        }
        getClient().createPod(pod);
        getClient().deletePod(pod.getId());
        assertNull(getClient().getPod(pod.getId()));
    }

    @Test(expected = KubernetesClientException.class)
    public void testDeleteNonExistantPod() throws Exception {
        // delete a non-existing pod
        getClient().deletePod("xxxxxx");
    }

    @Test
    public void testCreateReplicationController() throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Creating a Replication Controller: " + contr);
        }
        getClient().createReplicationController(contr);
        assertNotNull(getClient().getReplicationController(contr.getId()));

        // wait 10s for Pods to be created
        Thread.sleep(10000);

        // test recreation using same id
        try {
            getClient().createReplicationController(contr);
            fail("Should have thrown exception");
        } catch (Exception e) {
            // ignore
        }
        assertNotNull(getClient().getReplicationController(contr.getId()));

        PodList podList = getClient().getSelectedPods(ImmutableList.of(contr.getLabels()));
        assertNotNull(podList);
        assertNotNull(podList.getItems());
        assertEquals(contr.getDesiredState().getReplicas(), podList.getItems().size());
    }

    @Test
    public void testGetAllReplicationControllers() throws Exception {
        getClient().createReplicationController(contr);
        assertThat(getClient().getAllReplicationControllers().getItems().size(), greaterThan(0));
    }

    @Test(expected = KubernetesClientException.class)
    public void testUpdateReplicationControllerWithBadCount() throws Exception {
        // test incorrect replica count
        getClient().createReplicationController(contr);
        getClient().updateReplicationController(contr.getId(), -1);
    }

    @Test
    public void testUpdateReplicationControllerToZero() throws Exception {
        getClient().createReplicationController(contr);
        getClient().updateReplicationController(contr.getId(), 0);

        Thread.sleep(10000);

        PodList podList = getClient().getSelectedPods(ImmutableList.of(contr.getLabels()));
        assertNotNull(podList);
        assertNotNull(podList.getItems());
        assertEquals(0, podList.getItems().size());
    }

    @Test
    public void testDeleteReplicationController() throws Exception {
        getClient().createReplicationController(contr);
        getClient().deleteReplicationController(contr.getId());
        assertNull(getClient().getReplicationController(contr.getId()));
    }

    @Test(expected = KubernetesClientException.class)
    public void testCreateInvalidReplicationController() throws Exception {
        // create an invalid Controller
        ReplicationController bogusContr = new ReplicationController();
        bogusContr.setId("xxxxx");
        getClient().createReplicationController(bogusContr);
    }

    @Test
    public void testGetInvalidReplicationController() throws Exception {
        assertNull(getClient().getReplicationController("xxxxx"));
    }

    @Test(expected = KubernetesClientException.class)
    public void testUpdateInvalidReplicationController() throws Exception {
        getClient().updateReplicationController("xxxxx", 3);
    }

    @Test(expected = KubernetesClientException.class)
    public void testDeleteInvalidReplicationController() throws Exception {
        getClient().deleteReplicationController("xxxxx");
    }

    @Test
    public void testCreateService() throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Creating a Service Proxy: " + serv);
        }
        getClient().createService(serv);
        assertNotNull(getClient().getService(serv.getId()));

        // test recreation using same id
        try {
            getClient().createService(serv);
            fail("Should have thrown exception");
        } catch (Exception e) {
            // ignore
        }

        assertNotNull(getClient().getService(serv.getId()));
    }

    @Test
    public void testGetAllServices() throws Exception {
        getClient().createService(serv);
        ServiceList serviceList = getClient().getAllServices();
        assertNotNull(serviceList);
        assertNotNull(serviceList.getItems());
        assertThat(serviceList.getItems().size(), greaterThan(0));
    }

    @Test
    public void testDeleteService() throws Exception {
        getClient().createService(serv);
        Status status = getClient().deleteService(serv.getId());
        assertNull(getClient().getService(serv.getId()));
    }

    @Test(expected = KubernetesClientException.class)
    public void testCreateInvalidService() throws Exception {
        // create an invalid Service
        Service bogusServ = new Service();
        bogusServ.setId("xxxxxx");
        getClient().createService(bogusServ);
    }

    @Test(expected = KubernetesClientException.class)
    public void testDeleteInvalidService() throws Exception {
        getClient().deleteService("xxxxx");
    }
}
