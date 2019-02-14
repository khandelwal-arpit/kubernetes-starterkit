## Application ##
This is a vue-cli 3.0 based Vue.js application which renders the calculator UI as shown below:

![Calculator UI](https://github.com/khandelwal-arpit/kubernetes-starterkit/blob/master/assets/img/vuecalc.png)

The service is composed of 'home', 'calculator' and 'history' views. It uses Axios library to do all the API calls to the kubernetes cluster. It uses a sound architecture with separate components and services for different aspects of the interface. The 'history' view shows a list of recent operations done over the calculator application by fetching them from the 'Bootstorage' service located inside the cluster.

## Project setup ##
```
npm install
```

### Compiles and hot-reloads for development ###
```
npm run serve
```

### Compiles and minifies for production ###
```
npm run build
```

### Run your tests ###
```
npm run test
```

### Lints and fixes files ###
```
npm run lint
```

## Build Instructions ##

Build the image using following command:
```
docker build -t vuecalc .
```

Run the image locally using following command:
```
docker run -p 80:2000 vuecalc
```