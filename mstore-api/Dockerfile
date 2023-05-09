ARG mongoURI
ARG mongoDatabase

FROM maven:3.6.0-jdk-8-alpine AS build
ARG mongoURI
ARG mongoDatabase

ENV mongoURI=${mongoURI}
ENV mongoDatabase=${mongoDatabase}

RUN echo "${mongoURI} -  ${mongoDatabase}"
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:8-jdk-alpine
ARG mongoURI
ARG mongoDatabase

ENV mongoURI=${mongoURI}
ENV mongoDatabase=${mongoDatabase}
MAINTAINER baeldung.com
COPY --from=build /home/app/target/mstore-api-0.0.1-SNAPSHOT.jar /usr/local/lib/mstore-api.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/mstore-api.jar"]