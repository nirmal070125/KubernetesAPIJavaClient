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
import org.apache.stratos.kubernetes.api.model.State;

public class Main {

	public static void main(String[] args) throws Exception {
		
		final String KUBERNETES_API_ENDPOINT = "http://54.255.46.34:8080/api/v1beta1/";

        KubernetesApiClient client = new KubernetesApiClient(KUBERNETES_API_ENDPOINT);
        
        // test get pod
        System.out.println(client.getPod("redis-master-2").getCreationTimestamp());
        
        // test create pod
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
        Pod[] pods = client.getAllPods();
        for (Pod pod2 : pods) {
			System.out.println("Pod : "+pod2.getId());
		}
        
        client.deletePod("nirmal-test-pod");
        
	}

}
