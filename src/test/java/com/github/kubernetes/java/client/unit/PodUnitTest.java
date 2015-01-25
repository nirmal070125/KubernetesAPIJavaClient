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
package com.github.kubernetes.java.client.unit;

import java.util.Calendar;
import java.util.Collections;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.github.kubernetes.java.client.model.AbstractKubernetesModel;
import com.github.kubernetes.java.client.model.Container;
import com.github.kubernetes.java.client.model.Label;
import com.github.kubernetes.java.client.model.Manifest;
import com.github.kubernetes.java.client.model.Pod;
import com.github.kubernetes.java.client.model.Port;
import com.github.kubernetes.java.client.model.State;

@Category(com.github.kubernetes.java.client.UnitTests.class)
public class PodUnitTest extends TestCase{

	@Before
	public void setUp() {
	}
	
	@Test
	public void testPods() throws Exception { 
	    String podId = "github-test-pod";
	    Calendar time = Calendar.getInstance();
	    String selfLink = "link";
        Pod pod = new Pod();
        pod.setId(podId);
        pod.setCreationTimestamp(time);
        pod.setSelfLink(selfLink);
        Label l = new Label();
        l.setName("github");
        pod.setLabels(l);
        State desiredState = new State();
        Manifest m = new Manifest();
        m.setId(podId);
        Container c = new Container();
        c.setName("master");
        c.setImage("image");
        Port p = new Port();
        p.setContainerPort(8379);
        p.setHostPort(8379);
        c.setPorts(Collections.singletonList(p));
        m.setContainers(Collections.singletonList(c));
        desiredState.setManifest(m);
        pod.setDesiredState(desiredState);
        State currentState = desiredState;
        pod.setCurrentState(currentState);
        
        assertEquals(podId, pod.getId());
        assertEquals("Pod", pod.getKind().toString());
        assertEquals(l, pod.getLabels());
        assertEquals(currentState, pod.getCurrentState());
        assertEquals(selfLink, pod.getSelfLink());
        assertEquals(desiredState, pod.getDesiredState());
        assertEquals(time, pod.getCreationTimestamp());
        
        assertEquals(true, pod.equals(pod));
        
        Pod pod2 = new Pod();
        pod2.setId(podId);
        
        assertEquals(true, pod.equals(pod2));
        assertEquals(true, pod.hashCode() == pod2.hashCode());
        
        pod2.setId("aa");
        assertEquals(false, pod.equals(pod2));
        
        pod2.setId(null);
        assertEquals(false, pod.equals(pod2));
        
        assertEquals(false, pod.equals(null));
        assertEquals(false, pod.equals(desiredState));
        
        pod.setId(null);
        pod2.setId(podId);
        assertEquals(false, pod.equals(pod2));
        
        pod2.setId(null);
        assertEquals(true, pod.equals(pod2));
        assertEquals(true, pod.hashCode() == pod2.hashCode());
        
	}
}
