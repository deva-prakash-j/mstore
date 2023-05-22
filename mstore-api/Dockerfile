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
ENV NEW_RELIC_APP_NAME="mstore-api"
ENV NEW_RELIC_LICENSE_KEY="f27ab6c60732902b793c5ca5e5df4bff005aNRAL"
ENV NEW_RELIC_LOG_FILE_NAME="STDOUT"

ADD newrelic/newrelic.jar  /app
ADD newrelic/newrelic.yml  /app 
COPY --from=build /home/app/target/mstore-api-0.0.1-SNAPSHOT.jar /app/mstore-api.jar
EXPOSE 8080
ENTRYPOINT ["java","-javaagent:/app/newrelic.jar", "-jar","/app/mstore-api.jar"]