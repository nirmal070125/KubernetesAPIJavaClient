package com.github.kubernetes.java.client.v2;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.jboss.resteasy.client.jaxrs.ProxyBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;

public class RestFactory {

    private final ClassLoader classLoader;

    public RestFactory() {
        classLoader = null;
    }

    public RestFactory(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public KubernetesAPI createAPI(URI uri, String userName, String password) {

        // Configure HttpClient to authenticate preemptively
        // by prepopulating the authentication data cache.
        // http://docs.jboss.org/resteasy/docs/3.0.9.Final/userguide/html/RESTEasy_Client_Framework.html#transport_layer
        // http://hc.apache.org/httpcomponents-client-4.2.x/tutorial/html/authentication.html#d5e1032

        HttpHost targetHost = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());

        DefaultHttpClient httpclient = new DefaultHttpClient();

        httpclient.getCredentialsProvider().setCredentials(
                new AuthScope(targetHost.getHostName(), targetHost.getPort()),
                new UsernamePasswordCredentials(userName, password));

        // Create AuthCache instance
        AuthCache authCache = new BasicAuthCache();
        // Generate BASIC scheme object and add it to the local auth cache
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(targetHost, basicAuth);

        // Add AuthCache to the execution context
        BasicHttpContext localcontext = new BasicHttpContext();
        localcontext.setAttribute(ClientContext.AUTH_CACHE, authCache);

        // 4. Create client executor and proxy
        ApacheHttpClient4Engine engine = new ApacheHttpClient4Engine(httpclient, localcontext);
        ResteasyClient client = new ResteasyClientBuilder().httpEngine(engine).build();

        client.register(JacksonJaxbJsonProvider.class);
        ProxyBuilder<KubernetesAPI> proxyBuilder = client.target(uri).proxyBuilder(KubernetesAPI.class);
        if (classLoader != null) {
            proxyBuilder = proxyBuilder.classloader(classLoader);
        }
        return proxyBuilder.build();
    }

    public KubernetesAPI createAPI(String url, String userName, String password) throws URISyntaxException {
        URI uri = new URI(url);
        return createAPI(uri, userName, password);
    }
}
