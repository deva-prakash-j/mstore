ARG mongoURI
ARG mongoDatabase
ARG cfSpaceId
ARG cfEnvironmentId
ARG cfManagementApiToken
ARG cfDeliveryApiToken

FROM maven:3.6.0-jdk-8-alpine AS build

ARG mongoURI
ARG mongoDatabase
ARG cfSpaceId
ARG cfEnvironmentId
ARG cfManagementApiToken
ARG cfDeliveryApiToken

ENV mongoURI=${mongoURI}
ENV mongoDatabase=${mongoDatabase}
ENV cfSpaceId=${cfSpaceId}
ENV cfEnvironmentId=${cfEnvironmentId}
ENV cfManagementApiToken=${cfManagementApiToken}
ENV cfDeliveryApiToken=${cfDeliveryApiToken}

COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package



FROM openjdk:8-jdk-alpine

ENV mongoURI=${mongoURI}
ENV mongoDatabase=${mongoDatabase}
ENV cfSpaceId=${cfSpaceId}
ENV cfEnvironmentId=${cfEnvironmentId}
ENV cfManagementApiToken=${cfManagementApiToken}
ENV cfDeliveryApiToken=${cfDeliveryApiToken}

MAINTAINER baeldung.com
COPY --from=build /home/app/target/mstore-api-0.0.1-SNAPSHOT.jar /usr/local/lib/mstore-api.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/mstore-api.jar"]