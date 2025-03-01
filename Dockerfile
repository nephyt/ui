ARG PROPS_ENV="dev"

FROM openjdk:21-oracle
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY props/${PROPS_ENV}/* .
ENTRYPOINT ["java","-jar","/app.jar"]