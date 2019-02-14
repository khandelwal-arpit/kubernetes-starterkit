FROM openjdk:8-jre-alpine

WORKDIR /usr/spring/bootstorage

COPY ./target/bootstorage-0.0.1-SNAPSHOT.jar /usr/spring/bootstorage

EXPOSE 5000

CMD ["java", "-jar", "bootstorage-0.0.1-SNAPSHOT.jar"]