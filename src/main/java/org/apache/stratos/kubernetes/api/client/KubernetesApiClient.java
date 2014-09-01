package org.apache.stratos.kubernetes.api.client;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;
import org.apache.stratos.kubernetes.api.client.interfaces.KubernetesAPIClientInterface;
import org.apache.stratos.kubernetes.api.exceptions.KubernetesClientException;
import org.apache.stratos.kubernetes.api.model.Pod;
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

	
}
