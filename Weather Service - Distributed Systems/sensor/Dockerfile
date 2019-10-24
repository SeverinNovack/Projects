FROM alpine:latest
FROM maven:3.5-jdk-8
RUN sed -i '/jessie-updates/d' /etc/apt/sources.list
RUN apt-get update  
COPY pom.xml /usr/local/service/pom.xml
COPY src /usr/local/service/src
WORKDIR /usr/local/service
RUN mvn package
CMD ["java", "-jar", "target/Sensor-1.0.jar"]

