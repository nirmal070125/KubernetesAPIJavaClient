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
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.kubernetes.java.client.exceptions.KubernetesClientException;
import com.github.kubernetes.java.client.interfaces.KubernetesAPIClientInterface;
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
import com.google.common.collect.ImmutableList;

public abstract class AbstractKubernetesApiClientLiveTest extends TestCase {

    private static final Log log = LogFactory.getLog(AbstractKubernetesApiClientLiveTest.class);
    private String dockerImage;
    protected String endpoint;
    protected String username;
    protected String password;

    protected Pod pod;
    protected ReplicationController contr;
    protected Service serv;

    protected abstract KubernetesAPIClientInterface getClient();

    protected Class getExceptionClass() {
        return KubernetesClientException.class;
    }

    @Before
    public void setUp() {
        endpoint = System.getProperty("kubernetes.api.endpoint", "http://192.168.1.100:8080/api/" + KubernetesAPIClientInterface.VERSION + "/");
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
    private void cleanup() {
        try {
            getClient().deletePod(pod.getId());
        } catch (Exception e) {
            // do nothing
        }
        try {
            getClient().deleteReplicationController(contr.getId());
        } catch (Exception e) {
            // do nothing
        }
        try {
            getClient().deleteService(serv.getId());
        } catch (Exception e) {
            // do nothing
        }
    }

    private Pod getPod() {
        Pod pod = new Pod();
        pod.setId("github-test-pod");
        pod.setLabels(new Label("test-pod"));
        State desiredState = new State();
        Manifest m = new Manifest();
        m.setId(pod.getId());
        Container c = new Container();
        c.setName("master");
        c.setImage(dockerImage);
        c.setCommand(Collections.singletonList("tail -f /dev/null" ));
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
        contr.setId("githubController");
        State desiredState = new State();
        desiredState.setReplicas(2);

        Selector selector = new Selector();
        selector.setName("test-replicationController");
        desiredState.setReplicaSelector(selector);

        Pod podTemplate = new Pod();
        State podState = new State();
        Manifest manifest = new Manifest();
        manifest.setVersion("v1beta1");
        manifest.setId(contr.getId());
        Container container = new Container();
        container.setName("github-php");
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
        String serviceId = "github-service";
        Service serv = new Service();
        serv.setContainerPort("8379");
        serv.setPort(5000);
        serv.setId(serviceId);
        serv.setLabels(new Label("test-service"));
        serv.setName("github-service");
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
        getClient().createPod(pod);
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
        String bogusPodId = "non-existant";
        try {
            getClient().getPod(bogusPodId);
        } catch (Exception e) {
            assertThat(e, instanceOf(getExceptionClass()));
            assertEquals("Pod [" + bogusPodId + "] doesn't exist.", e.getMessage());
        }
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
    public void testGetSelectedPodsWithNonExistantLabel() throws Exception {
        PodList selectedPods = getClient().getSelectedPods(ImmutableList.of(pod.getLabels(), new Label("no-match")));
        assertNotNull(selectedPods);
        assertNotNull(selectedPods.getItems());
        assertEquals(0, selectedPods.getItems().size());
    }

    @Test
    public void testGetSelectedPodsWithEmptyLabel() throws Exception {
        PodList selectedPods = getClient().getSelectedPods(Collections.<Label>emptyList());
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
        try {
            getClient().getPod(pod.getId());
        } catch (Exception e) {
            assertThat(e, instanceOf(getExceptionClass()));
        }
    }

    @Test
    public void testDeleteNonExistantPod() throws Exception {
        // delete a non-existing pod
        try {
            getClient().deletePod("xxxxxx");
        } catch (Exception e) {
            assertThat(e, instanceOf(getExceptionClass()));
        }

        PodList selectedPods = getClient().getSelectedPods(ImmutableList.of(pod.getLabels()));
        assertNotNull(selectedPods);
        assertNotNull(selectedPods.getItems());
        assertEquals(0, selectedPods.getItems().size());
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

    @Test
    public void testUpdateReplicationControllerWithBadCount() throws Exception {
        // test incorrect replica count
        getClient().createReplicationController(contr);
        try {
            getClient().updateReplicationController(contr.getId(), -1);
        } catch (Exception e) {
            assertThat(e, instanceOf(getExceptionClass()));
            assertEquals(true, e.getMessage().contains("update failed"));
        }
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
        try {
            getClient().getReplicationController(contr.getId());
        } catch (Exception e) {
            assertThat(e, instanceOf(getExceptionClass()));
        }
    }

    @Test
    public void testCreateInvalidReplicationController() throws Exception {
        String bogusContrId = "github";
        // create an invalid Controller
        ReplicationController bogusContr = new ReplicationController();
        bogusContr.setId(bogusContrId);
        try {
            getClient().createReplicationController(bogusContr);
        } catch (Exception e) {
            assertThat(e, instanceOf(getExceptionClass()));
        }

        try {
            getClient().getReplicationController(bogusContrId);
        } catch (Exception e) {
            assertThat(e, instanceOf(getExceptionClass()));
            assertEquals("Replication Controller [" + bogusContrId + "] doesn't exist.", e.getMessage());
        }

        try {
            getClient().updateReplicationController(bogusContrId, 3);
        } catch (Exception e) {
            assertThat(e, instanceOf(getExceptionClass()));
            assertEquals("Replication Controller [" + bogusContrId + "] doesn't exist.", e.getMessage());
        }

        try {
            getClient().deleteReplicationController(bogusContrId);
        } catch (Exception e) {
            assertThat(e, instanceOf(getExceptionClass()));
            assertEquals("Replication Controller [" + bogusContrId + "] doesn't exist.", e.getMessage());
        }
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
        getClient().deleteService(serv.getId());
        try {
            getClient().getService(serv.getId());
        } catch (Exception e) {
            assertThat(e, instanceOf(getExceptionClass()));
        }
    }

    @Test
    public void testCreateInvalidService() throws Exception {
        String bogusServId = "github";
        // create an invalid Service
        Service bogusServ = new Service();
        bogusServ.setId(bogusServId);
        try {
            getClient().createService(bogusServ);
        } catch (Exception e) {
            assertThat(e, instanceOf(getExceptionClass()));
        }

        try {
            getClient().getService(bogusServId);
        } catch (Exception e) {
            assertThat(e, instanceOf(getExceptionClass()));
            assertEquals("Service [" + bogusServId + "] doesn't exist.", e.getMessage());
        }

        try {
            getClient().deleteService(bogusServId);
        } catch (Exception e) {
            assertThat(e, instanceOf(getExceptionClass()));
            assertEquals("Service [" + bogusServId + "] doesn't exist.", e.getMessage());
        }
    }
}
