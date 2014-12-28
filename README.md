KubernetesAPIJavaClient
=======================

Java REST API client for Google's Kubernetes API (https://github.com/GoogleCloudPlatform/kubernetes)

You can use this library to call a Kubernetes API hosted in a Kubernetes master node using Java programming language.

Current client interface can be found at https://github.com/nirmal070125/KubernetesAPIJavaClient/blob/master/src/main/java/com/github/kubernetes/java/client/interfaces/KubernetesAPIClientInterface.java

Running Live Tests
==================

Google Kubernetes Java Client supports running live tests against a Kubernetes API server.

Pre-requisites
==============

* Set up a Kubernetes Master node and make sure the API server is running. Please refer to https://github.com/nirmal070125/vagrant-kubernetes-setup . Say your Kubernetes API endpoint is ```http://192.168.1.100:8080/api/v1beta1/```
* Please pull a preferred docker image into the Master node. Say your preferred docker image is ```busybox```.
* When using Kubernetes with self-signed certificates you need to add them to the java runtime.

Example for a Kubernetes Vagrant setup, import certificate into `$JAVA_HOME/jre/lib/security/jssecacerts`
(you may need to use `sudo`):

    cp -n $JAVA_HOME/jre/lib/security/cacerts $JAVA_HOME/jre/lib/security/jssecacerts
    keytool -import -v -trustcacerts \
      -alias kubernetes-vagrant -file ~/.kubernetes.vagrant.ca.crt \
      -keystore $JAVA_HOME/jre/lib/security/jssecacerts \
      -keypass changeit -storepass changeit


[More resources on Certificates](http://erikzaadi.com/2011/09/09/connecting-jenkins-to-self-signed-certificated-servers/).


How to run the tests
====================

* Navigate into the folder where you have cloned the API Client.

* Run the live test profile;

```mvn clean install -Plive -Dkubernetes.api.endpoint=http://192.168.1.100:8080/api/v1beta1/ -Ddocker.image=busybox```


NOTE: Please note that these are the default values and if your setup is equivalent to this, you can simply run ```mvn clean install -Plive```

