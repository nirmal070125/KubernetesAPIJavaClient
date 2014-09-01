package org.apache.stratos.kubernetes.api.client.interfaces;

import org.apache.stratos.kubernetes.api.exceptions.KubernetesClientException;
import org.apache.stratos.kubernetes.api.model.Pod;

public interface KubernetesAPIClientInterface {

	/**
	 * Get information of a Pod given the PodID
	 * @param podId id of the pod
	 * @return {@link Pod}
	 */
	public Pod getPod(String podId) throws KubernetesClientException;
	
}
