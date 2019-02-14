## Application ##
It is an Express.js (v4.16.x) based web application serving basic APIs for addition and subtraction. It is a containerised service and deployed inside the cluster as a pod eligible to horizontally scale under extreme conditions. The _Vuecalc_ service calls the apis from this service when it has to do an add or subtract operation, once the operation is complete an async call is placed to the _Bootstorage_ service to store the last performed operation in the Redis data store.

## Build Instructions ##

Build the image using following command:
```
docker build -t expressed .
```

Run the image locally using following command:
```
docker run -p 80:3000 expressed
```