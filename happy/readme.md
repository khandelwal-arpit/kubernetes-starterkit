
## Application ##
It is a Hapi.js (v17.8.x) based application having two basic APIs for multiplication and division. It is also a containerised service and similar to the _Expressed_ service, is deployed internally in the kubernetes cluster for serving the apis. It also places an async call to the 'Bootstorage' service to store the last executed operation on Redis.

## Build Instructions ##

Build the image using following command:
```
docker build -t happy .
```

Run the image locally using following command:
```
docker run -p 80:4000 happy
```