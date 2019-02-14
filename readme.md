<h1 align="center">
  <br>
  <a><img src="https://github.com/khandelwal-arpit/kubernetes-starterkit/blob/master/assets/img/logo.png" alt="Calculator" width="400"></a>
  <br>
  Kubernetes Starterkit
  <br>
</h1>

<h4 align="center">A minimal polyglot Microservices based calculator application built for deployment over <a href="https://kubernetes.io/" target="_blank">Kubernetes</a>.</h4>

<p align="center">
    <a alt="Node.js">
        <img src="https://img.shields.io/badge/Node.js-LTS-green.svg" />
    </a>
    <a alt="Java">
      <img src="https://img.shields.io/badge/Java-1.8-orange.svg" />
    </a>
    <a alt="Docker">
        <img src="https://img.shields.io/badge/Docker-18.09-yellowgreen.svg" />
    </a>
    <a alt="Dependencies">
        <img src="https://img.shields.io/badge/dependencies-up%20to%20date-brightgreen.svg" />
    </a>
    <a alt="Contributions">
        <img src="https://img.shields.io/badge/contributions-welcome-orange.svg" />
    </a>
    <a alt="License">
        <img src="https://img.shields.io/badge/license-MIT-blue.svg" />
    </a>
</p>

## Table of Contents ##
1. [Philosophy](#Philosophy)
2. [Application](#Application)
3. [Architecture](#Architecture)
4. [Services](#Services)
5. [Kubernetes Components](#Kubernetes-Components)
6. [Build and Deploy](#Build-and-Deploy)
7. [Important Kubernetes Commands](#Important-Kubernetes-Commands)
8. [Contributor](#Contributor)
9. [License](#License)


## Philosophy ##
I started learning Kubernetes a few months ago after wrapping up my head around Docker. According to Google, "_Kubernetes_ is a portable, extensible open-source platform for managing containerized workloads and services, that facilitates both declarative configuration and automation".

Honestly, the only simple thing I could get my hands on for first few weeks was the definition of it. No matter how hard I tried to glue things together to understand this exciting technology, I kept on failing and for a very simple reason that I was trying to too hard by looking at different articles and blog posts and was trying to accomplish all at once, however I wasn't building something around it which is unarguably the best approach to learn a new tech, yes, do it!

Its only then, that I embarked on a quest to make sure not only that I learn the concepts of Kubernetes myself, I do it in a way that makes it easier for my colleagues to understand and learn it faster than I did. The current starter-kit takes the approach of 'read-it' first along with 'do-it' next and encapsulates another hot topic 'micro services' inside the kit that will help a lot of techies to deploy and scale their micro services based deployment using docker containers and kubernetes.

## Application ##
The application built as a part of this quest is a simple browser based calculator whose front end is written using Vue.js and the operations (addition, multiplication etc) are served by following micro services written in various programming languages:

1) _Expressed_ service (Express.js)
2) _Happy_ service (Hapi.js)
3) _Bootstorage_ service (Spring Boot)
4) _Vuecalc_ service (Vue.js)

The _Expressed_ and _Happy_ services are rest api based implementations not doing much tangibly but only serve basic calculator functions as APIs. The _Bootstorage_ micro service acts as a backend service that stores and retrieves data provided to it via the former two.

The _Vuecalc_ service is a frontend application designed in _Vue.js_. It resembles a calculator, two of whose primary operations i.e. Add and Subtract are served by the expressed service and the other two operations, i.e. multiply and division are served by the happy service. 

Along with the micro-services, following components are deployed in the cluster as well:
1) [Ambassador API Gateway](https://www.getambassador.io/)
2) [Redis (data store)](https://redis.io/)

Ambassador(provided by datawire.io) is an open source Kubernetes-Native API Gateway built on the Envoy Proxy which is a native kubernetes api gateway. Redis acted as a data store for this implementation, storing the operations done on the calculator and serving them on on demand.

## Architecture ##
The complete architecture of this implementation can be summarised as:

![Calculator Microservices](https://github.com/khandelwal-arpit/kubernetes-starterkit/blob/master/assets/img/calculator.jpg)

As we can see, Ambassador is acting as a single point of entry in the Kubernetes cluster, channeling all the incoming requests to appropriate services. The various microservices are deployed at the following addresses inside the cluster:

| Service       | Cluster Address | Cluster Port | API endpoint     |
| ------------- | --------------- | ------------ | -----------------
| Vuecalc       | vuecalc-svc     |     2000     | /                |
| Expressed     | express-svc     |     3000     | /api/express/    |
| Happy         | happy-svc       |     4000     | /api/happy/      |
| Bootstorage   | bootstorage-svc |     5000     | /api/bootstorage/|

Each one of the microservice has liveness and readiness probes inbuilt for the cluster to determine their health and update the users about any potential downtime. The probes are configured for each service at the following endpoints:

| Service       | Liveness Probe           | Readiness Probe          |
| ------------- | ------------------------ | ------------------------ | 
| Vuecalc       | /                        | /                        |
| Expressed     | /api/express/healthz     | /api/express/healthz     |
| Happy         | /api/happy/healthz       | /api/happy/healthz       |
| Bootstorage   | /api/bootstorage/healthz | /api/bootstorage/healthz |

These probes are configured in the individual deployment configurations as follows:

``` yaml
containers:
      - image: expressed:latest
        imagePullPolicy: Never
        name: express-svc
        env:
          - name: MY_SECRET
            valueFrom:
              secretKeyRef:
                name: expressed-env
                key: secret
          - name: LOG_LEVEL
            valueFrom:
              configMapKeyRef:
                name: expressed-config
                key: log_level
        ports:
        - containerPort: 3000
          protocol: TCP
        readinessProbe:
          failureThreshold: 3
          httpGet:
            path: /api/express/healthz
            port: 3000
            scheme: HTTP
          initialDelaySeconds: 5
          periodSeconds: 10
          successThreshold: 1
          timeoutSeconds: 1
        livenessProbe:
          failureThreshold: 3
          httpGet:
            path: /api/express/healthz
            port: 3000
            scheme: HTTP
          initialDelaySeconds: 5
          periodSeconds: 10
          successThreshold: 1
          timeoutSeconds: 1  
```

All the services are currently deployed under the _default_ namespace to keep things simple. However we strongly suggest that users should opt for different namespaces specially if they are planning to deploy their DEV, QA and UAT environments all on the same cluster (of course assuming _PROD_ is going to be an independent cluster).

## Services ##
The different services created for this starter-kit are summarised in the following diagram:

![Calculator Services](https://github.com/khandelwal-arpit/kubernetes-starterkit/blob/master/assets/img/services.jpg)

### Vuecalc ### 
This is a vue-cli 3.0 based Vue.js application which renders the calculator UI as shown below:

![Calculator UI](https://github.com/khandelwal-arpit/kubernetes-starterkit/blob/master/assets/img/vuecalc.png)

The service is composed of 'home', 'calculator' and 'history' views. It uses Axios library to do all the API calls to the kubernetes cluster. It uses a sound architecture with separate components and services for different aspects of the interface. The 'history' view shows a list of recent operations done over the calculator application by fetching them from the 'Bootstorage' service located inside the cluster. The commands to build and run the service independently are specified in the concerned [readme.md](https://github.com/khandelwal-arpit/kubernetes-starterkit/blob/master/vuecalc/README.md) file in the service's codebase.

### Expressed ###
It is an Express.js (v4.16.x) based web application serving basic APIs for addition and subtraction. It is a containerised service and deployed inside the cluster as a pod eligible to horizontally scale under extreme conditions. The _Vuecalc_ service calls the apis from this service when it has to do an add or subtract operation, once the operation is complete an async call is placed to the _Bootstorage_ service to store the last performed operation in the Redis data store. Build and run commands are shared in [readme.md](https://github.com/khandelwal-arpit/kubernetes-starterkit/blob/master/expressed/readme.md).

### Happy ###
It is a Hapi.js (v17.8.x) based application having two basic APIs for multiplication and division. It is also a containerised service and similar to the _Expressed_ service, is deployed internally in the kubernetes cluster for serving the apis. It also places an async call to the 'Bootstorage' service to store the last executed operation on Redis. Build and run commands are shared in [readme.md](https://github.com/khandelwal-arpit/kubernetes-starterkit/blob/master/happy/readme.md).

### Bootstorage ###
It is a Java (v1.8) and Spring Boot (v2.x) based microservice that has two primary purposes:

1. Store executed operations inside Redis, and
2. Return the list of operations stored in Redis sorted in the order of execution (last operation at top).

Redis has been used in the cluster as a statefulset which has persistence enabled and uses persistent volume claim templates in the cluster to store the Redis dump files which allows the cluster to make sure Redis always retains the values in between cluster outages too. The Redis connection parameters are dynamically passed to the pod from the ENV variables which are provided to it using the config maps. Build and run commands are shared in [readme.md](https://github.com/khandelwal-arpit/kubernetes-starterkit/blob/master/bootstorage/readme.md).

## Kubernetes Components ##
Following kubernetes components were used during the development of this starter kit:

1) Services
2) Deployments
3) Pods
4) StatefulSets
5) Cronjob
6) Secrets
7) Config-Maps
8) Persistent Volumes
9) Persistent Volumes Claims
10) Storage Class
11) Kubernetes Dashboard

We are assuming that there is an existing kubernetes cluster already running on one of the renowned cloud hosting service provider -

1) Google cloud (Kubernetes Engine)
2) MS Azure (Kubernetes Cluster Service)
3) AWS (AKS)

Or, if this is not the case, you can try Docker-for-desktop, it allows us to create a sample kubernetes cluster with a single node inside it. Even though it only has a single node out of the box, it suffices the complete development and testing effort needed to run an application on kubernetes cluster before we actually go to the cloud. We suggest you try your configurations first locally and once it works as expected then move to the cloud to save unnecessary hosting cost.

Some of the important components used for current project's development are explained in sections below.

### Config Maps ###
_ConfigMaps_ allow us to decouple configuration artifacts from image content to keep containerized applications portable. For the purpose of demonstration, the _expressed_ and _happy_ service both use a config map of their own to pass ENV var _log_level_ to them. The yaml look as follows:

*expressed-cmap.yaml*
``` yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: expressed-config
  namespace: default
data:
  log_level: INFO
```

*happy-cmap.yaml*
``` yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: happy-config
  namespace: default
data:
  log_level: DEBUG
```

These configurations are referred inside the deployment specifications as follows:

*expressed.yaml*
``` yaml
template:
    metadata:
      labels:
        run: express-svc
    spec:
      containers:
      - image: expressed:latest
        imagePullPolicy: Never
        name: express-svc
        env:
          - name: LOG_LEVEL
            valueFrom:
              configMapKeyRef:
                name: expressed-config
                key: log_level
```

### StatefulSet ###
Like a Deployment, a _StatefulSet_ manages pods that are based on an identical container spec. Unlike a Deployment, a StatefulSet maintains a sticky identity for each of their Pods. These pods are created from the same spec, but are not interchangeable as each has a persistent identifier that it maintains across any rescheduling.

A StatefulSet operates under the same pattern as any other Controller. We define our desired state in a StatefulSet object, and the StatefulSet controller makes any necessary updates to get there from the current state.

StatefulSets are valuable for applications that require one or more of unique network identifiers or persistent storage or ordered, graceful deployment and scaling. In our case we had the need to store the data for various operations happening in the calculator application so that we can show them when user goes to the history section of the application. Here is how the configuration yaml for it looks like:

*redis.yaml*

``` yaml
---
apiVersion: v1
kind: Service
metadata:
  labels:
    run: redis
  name: redis
  namespace: default
spec:
  ports:
    - port: 6379
      name: redis
  clusterIP: None
  selector:
    app: redis
---
apiVersion: apps/v1
kind: StatefulSet
metadata:
  labels:
    run: redis
  name: redis  
  namespace: default
spec:
  selector:
    matchLabels:
      app: redis
  serviceName: redis
  replicas: 1
  template:
    metadata:
      labels:
        app: redis
    spec:
      containers:
        - name: redis
          image: redis:3.2-alpine
          imagePullPolicy: IfNotPresent
          args: ["--requirepass", "calculator", "--appendonly", "yes", "--save", "900", "1", "--save", "30", "2"]
          ports:
            - containerPort: 6379
              name: redis
          volumeMounts:
            - name: redis-volume
              mountPath: /data
  volumeClaimTemplates:
  - metadata:
      name: redis-volume
    spec:
      accessModes: [ "ReadWriteOnce" ]
      resources:
        requests:
          storage: 50Mi
```

The Redis runs on default port _6379_ however its access is password protected and needs the password _calculator_ for any access within the cluster. The appendonly command instructs Redis to create a dump file and append to it every 900 seconds if single edit has been made or every 30 seconds if 2 edits have been made.

The volume that gets mounted points us to a volume claim which is being dynamically allocated to us using the persistent volume claim template (pvc template) definition. The pvc-template instructs the cluster to create a storage on disk and allocate the same to Redis each time it reboots. This way we do maintain the state of Redis deployment which is crucial since it is acting like the data store for our use case. You can easily replace it with MySql or any other database of your choice, but please bear in mind that you will need to do certain modifications to the _Bootstorage_ service code in order to support the new storage mechanism.

### Cronjob ###
A Cron Job creates Jobs on a time-based schedule. One CronJob object is like one line of a crontab (cron table) file. It runs a job periodically on a given schedule, written in Cron format.

In our case, we have written a cronjob that invokes the following API every 15 minutes:

```
http://bootstorage-svc:5000/api/bootstorage/deletelru 
```

The purpose of this api (written in the Bootstorage service) is to delete the oldest operation that was performed on the calculator. This way we keep the footprint of our data store small and at the same time have a perfectly valid use case to show how a cronjob can be executed in the application to do a recurring operation. The yaml for the same looks as follows:

``` yaml
apiVersion: batch/v1beta1
kind: CronJob
metadata:
  name: cronjob
spec:
  concurrencyPolicy: Forbid
  jobTemplate:
    spec:
      template:
        metadata:
          labels:
            app: cronjob
        spec:
          containers:
          - name: cronjob
            image: spotify/alpine:latest
            imagePullPolicy: Always
            command:
            - curl
            args:
            - http://bootstorage-svc:5000/api/bootstorage/deletelru
          restartPolicy: Never  
  schedule: '*/15 * * * *'
  successfulJobsHistoryLimit: 3
```

The parameter *successfulJobsHistoryLimit* makes sure that we see exactly 3 entries of the last successful executions of the cronjob in our kubernetes history either viewed via kubectl or on the dashboard. This cronjob also presents a classic example where a task defined in one of your micro service needs to execute periodically and you were hesitant to create an entire pod just for the sake of running the task, using this approach you can easily instruct any running pod via the API call which is executed inside a *~35MB* alpine container spun up at runtime.

### API Gateway ###
Ambassador is a production tested, open source API Gateway that exposes the power of Envoy Proxy in Kubernetes. Ambassador relies entirely on Kubernetes for reliability, availability, and scalability. For example, Ambassador persists all state in Kubernetes, instead of requiring a separate database. Ambassador has virtually no moving parts, and delegates all routing and resilience to Envoy Proxy and Kubernetes, respectively. It integrates the features teams need for microservices, including authentication, rate limiting, observability, routing, TLS termination, and more.

In our case, we decided to deploy Ambassador service as a _LoadBalancer_ listening on port 80. The configuration that was responsible for the routing of incoming calls to independent micro services is as follows:

``` yaml
apiVersion: v1
kind: Service
metadata:
  labels:
    service: ambassador
  name: ambassador
  annotations:
    getambassador.io/config: |
      ---
      apiVersion: ambassador/v0
      kind: Mapping
      name: vuecalc-svc_mapping
      prefix: /
      service: vuecalc-svc:2000
      ---
      apiVersion: ambassador/v0
      kind: Mapping
      name: express-svc_mapping
      prefix: /express/
      rewrite: /api/express/
      service: express-svc:3000
      ---
      apiVersion: ambassador/v0
      kind: Mapping
      name: happy-svc_mapping
      prefix: /happy/
      rewrite: /api/happy/
      service: happy-svc:4000
      ---
      apiVersion: ambassador/v0
      kind: Mapping
      name: bootstorage-svc_mapping
      prefix: /bootstorage/
      rewrite: /api/bootstorage/
      service: bootstorage-svc:5000
spec:
  type: LoadBalancer
  ports:
  - name: ambassador
    port: 80
    targetPort: 80
  selector:
    service: ambassador
```

For services such as _Expressed_, _Happy_ and _Bootstorage_ we are rewriting the incoming request path to _/api/*_ and the service mappings are following the convention of _service-name:port_ where the ports are those allocated to these services inside the cluster. There are different ways how developers use the routing configurations, some of us prefer to put this information as a kubernetes-ambassador annotation inside the respective deployment configurations but we personally like to keep them all at a single place inside the ambassador configuration file and that's exactly what you will notice in the configs.

### Kubernetes Dashboard ###
Dashboard is a web-based Kubernetes user interface. You can use Dashboard to deploy containerized applications to a Kubernetes cluster, troubleshoot your containerized application, and manage the cluster resources. You can use Dashboard to get an overview of applications running on your cluster, as well as for creating or modifying individual Kubernetes resources (such as Deployments, Jobs, DaemonSets, etc).

Please follow this [link](https://kubernetes.io/docs/tasks/access-application-cluster/web-ui-dashboard/) to learn more about how to set up the dashboard for your cluster and make use of a decent web interface to administer the kubernetes cluster. Here are a few screen shots of how the calculator application components look like on our dashboard after successful deployment:

**Overview**

![Overview](https://github.com/khandelwal-arpit/kubernetes-starterkit/blob/master/assets/img/overview.png)

**Resources**

![Resources](https://github.com/khandelwal-arpit/kubernetes-starterkit/blob/master/assets/img/resources.png)

**Services**

![Services](https://github.com/khandelwal-arpit/kubernetes-starterkit/blob/master/assets/img/services.png)

**Deployments**

![Deployments](https://github.com/khandelwal-arpit/kubernetes-starterkit/blob/master/assets/img/deployments.png)

**Pods**

![Pods](https://github.com/khandelwal-arpit/kubernetes-starterkit/blob/master/assets/img/pods.png)

**StatefulSets**

![StatefulSets](https://github.com/khandelwal-arpit/kubernetes-starterkit/blob/master/assets/img/statefulsets.png)

**CronJobs**

![CronJobs](https://github.com/khandelwal-arpit/kubernetes-starterkit/blob/master/assets/img/cronjobs.png)

**Config And Storage**

![Config And Storage](https://github.com/khandelwal-arpit/kubernetes-starterkit/blob/master/assets/img/config-storage.png)

## Build and Deploy ##
There are some assumptions that we are making (before you can run the build and deploy command) like you have the following pre-installed on the node/environment where this cluster is being set up -

1. [Docker](https://www.docker.com/)
2. [Maven](https://maven.apache.org/)
3. [NPM](https://nodejs.org/en/)
4. [Kubectl](https://kubernetes.io/docs/reference/kubectl/overview/)

Once these are setup, please execute the following command from the root directory of this repo:

```
sh build-deploy.sh
```

This command is equipped to prepare all the docker containers needed for the setup of this application. It will create them all one by one and finally execute the configurations placed in the **k8s** directory which will result in the creation of various artifacts on the kubernetes cluster.

## Important Kubernetes Commands ##
Here are some basic commands for using Kubernetes command line utility:


1. **kubectl proxy** (Run kubernetes dashboard)
2. **kubectl describe secret admin-user-token-6kchd -n kube-system** (Generate token)
3. **kubectl create -f k8s** (Deploy to kubernetes)
4. **kubectl get deployments** (Check existing deployments)
5. **kubectl get services** (Check existing services)
6. **kubectl set image deployments expressed expressed=expressed:latest** (Update image for Deployment)
7. **kubectl rollout status deployments expressed** (Check status of updated rollout)
8. **kubectl scale --replicas=0 deployment express-svc** (Scale down any deployment)
9. **kubectl scale --replicas=1 deployment express-svc** (Scale up a deployment)
10. **docker image prune** (Remove all dangling images)


## Contributor ##
[Arpit Khandelwal](https://www.linkedin.com/in/arpitkhandelwal1984/)

## License ##
This project is licensed under the terms of the MIT license.