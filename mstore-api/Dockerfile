FROM openjdk:8-jdk-alpine
MAINTAINER baeldung.com

RUN mvn clean package
COPY target/mstore-api-0.0.1-SNAPSHOT.jar mstore-api.jar
ENTRYPOINT ["java","-jar","/mstore-api.jar"]