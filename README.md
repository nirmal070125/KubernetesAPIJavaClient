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
* Please pull a preferred docker image into the Master node. Say your preferred docker image is ```gurpartap/redis```.

How to run the tests
====================

* Navigate into the folder where you have cloned the API Client.

* Run the live test profile;

```mvn clean install -Plive -Dkubernetes.api.endpoint=http://192.168.1.100:8080/api/v1beta1/ -Ddocker.image=gurpartap/redis```


NOTE: Please note that these are the default values and if your setup is equivalent to this, you can simply run ```mvn clean install -Plive```

