package com.github.kubernetes.java.client.v2;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.jboss.resteasy.client.jaxrs.ProxyBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;

import com.github.kubernetes.java.client.interfaces.KubernetesAPIClientInterface;

public class RestFactory {

    private final ClassLoader classLoader;

    public RestFactory() {
        classLoader = null;
    }

    public RestFactory(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

	public KubernetesAPIClientInterface createAPI(URI uri, String userName, String password) {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.getCredentialsProvider().setCredentials( new AuthScope( uri.getHost(), uri.getPort() ),
                                                            new UsernamePasswordCredentials( userName, password ) );

		ApacheHttpClient4Engine engine = new ApacheHttpClient4Engine(httpClient);
        ResteasyClient client = new ResteasyClientBuilder().httpEngine(engine).build();
		client.register(JacksonJaxbJsonProvider.class);
		ProxyBuilder<KubernetesAPIClientInterface> proxyBuilder = client.target(uri).proxyBuilder(KubernetesAPIClientInterface.class);
		if (classLoader != null) {
		    proxyBuilder = proxyBuilder.classloader(classLoader);
		}
		return proxyBuilder.build();
	}

    public KubernetesAPIClientInterface createAPI(String url, String userName, String password) throws URISyntaxException {
		URI uri = new URI(url);
		return createAPI(uri, userName, password);
	}
}
