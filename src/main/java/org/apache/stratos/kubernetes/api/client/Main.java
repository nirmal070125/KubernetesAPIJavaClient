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

import org.apache.stratos.kubernetes.api.model.Container;
import org.apache.stratos.kubernetes.api.model.Label;
import org.apache.stratos.kubernetes.api.model.Manifest;
import org.apache.stratos.kubernetes.api.model.Pod;
import org.apache.stratos.kubernetes.api.model.Port;
import org.apache.stratos.kubernetes.api.model.ReplicationController;
import org.apache.stratos.kubernetes.api.model.Selector;
import org.apache.stratos.kubernetes.api.model.State;

public class Main {

	public static void main(String[] args) throws Exception {
		
		final String KUBERNETES_API_ENDPOINT = "http://54.255.46.34:8080/api/v1beta1/";

        KubernetesApiClient client = new KubernetesApiClient(KUBERNETES_API_ENDPOINT);
        
        // test get pod
        System.out.println("Test GET POD");
        System.out.println(client.getPod("redis-master-2"));
        
        // test create pod
        System.out.println("Test POST POD");
        Pod pod = new Pod();
        pod.setApiVersion("v1beta1");
        pod.setId("nirmal-test-pod");
        pod.setKind("Pod");
        Label l = new Label();
        l.setName("nirmal");
        pod.setLabels(l);
        State desiredState = new State();
        Manifest m = new Manifest();
        m.setId("nirmal-test-pod");
        m.setVersion("v1beta1");
        Container c = new Container();
        c.setName("master");
        c.setImage("dockerfile/redis");
        Port p = new Port();
        p.setContainerPort(8379);
        p.setHostPort(8379);
        c.setPorts(new Port[]{p});
        m.setContainers(new Container[]{c});
        desiredState.setManifest(m);
        pod.setDesiredState(desiredState);
        client.createPod(pod);
        
        // test get all Pods
        System.out.println("Test GET PODS");
        Pod[] pods = client.getAllPods();
        for (Pod pod2 : pods) {
			System.out.println("Pod : "+pod2.getId());
		}
        
        // test delete Pod
        System.out.println("Test DELETE POD");
        client.deletePod("nirmal-test-pod");
        
        /* Replication Controllers */
        // test get controller
        System.out.println("Test GET ReplicationController");
        ReplicationController controller = client.getReplicationController("frontendController");
        System.out.println(controller);
        
        // test get all controllers
        System.out.println("Test GET ReplicationControllers");
        ReplicationController[] controllers = client.getAllReplicationControllers();
        for (ReplicationController replicationController : controllers) {
			System.out.println("Replication Controller: "+replicationController.getId());
		}
        
        // test create controller
        System.out.println("Test POST ReplicationController");
        ReplicationController contr = new ReplicationController();
        contr.setId("nirmalController");
        contr.setKind("ReplicationController");
        contr.setApiVersion("v1beta1");
        desiredState = new State();
        desiredState.setReplicas(3);
        Selector selector = new Selector();
        selector.setName("frontend");
        desiredState.setReplicaSelector(selector);
        
        Pod podTemplate = new Pod();
        State podState = new State();
        Manifest manifest = new Manifest();
        manifest.setVersion("v1beta1");
        manifest.setId("nirmalfrontendController");
        Container container = new Container();
        container.setName("nirmal-php-redis");
        container.setImage("brendanburns/php-redis");
        p = new Port();
        p.setContainerPort(81);
        p.setHostPort(8001);
        container.setPorts(new Port[]{p});
        manifest.setContainers(new Container[]{container});
        podState.setManifest(manifest);
        podTemplate.setDesiredState(podState);
        Label l1 = new  Label();
        l1.setName("frontend");
        podTemplate.setLabels(l1);
        
        desiredState.setPodTemplate(podTemplate);
        contr.setDesiredState(desiredState);
        Label l2 = new Label();
        l2.setName("frontend");
        contr.setLabels(l2);
        
        client.createReplicationController(contr);
        
        // Test delete controller
        System.out.println("Test DELETE ReplicationController");
        client.deleteReplicationController("nirmalController");
        
	}

}
