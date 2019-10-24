FROM alpine:latest
FROM maven:3.5-jdk-8
RUN sed -i '/jessie-updates/d' /etc/apt/sources.list
RUN apt-get update  
COPY pom.xml /usr/local/service/pom.xml
COPY src /usr/local/service/src
COPY DataHistory /usr/local/service/DataHistory
WORKDIR /usr/local/service
RUN mvn package
CMD ["java", "-jar", "target/Station-1.0.jar", "7676"]
