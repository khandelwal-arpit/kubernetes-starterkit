## Application ##
It is a Java (v1.8) and Spring Boot (v2.x) based microservice that has two primary purposes:

1. Store executed operations inside Redis, and
2. Return the list of operations stored in Redis sorted in the order of execution (last operation at top).

Redis has been used in the cluster as a statefulset which has persistence enabled and uses persistent volume claim templates in the cluster to store the Redis dump files which allows the cluster to make sure Redis always retains the values in between cluster outages too. The Redis connection parameters are dynamically passed to the pod from the ENV variables which are provided to it using the config maps.

## Build Instructions ##

Build the image using following command:
```
docker build -t bootstorage .
```

Run the image locally using following command:
```
docker run -p 80:5000 bootstorage
```

Run the maven build from command promot:
```
mvn package
```