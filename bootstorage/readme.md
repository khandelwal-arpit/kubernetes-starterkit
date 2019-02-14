Building your image
```
docker build -t bootstorage .
```

Run the image
```
docker run -p 80:5000 bootstorage
```

Run the maven build from command promot
```
mvn package
```