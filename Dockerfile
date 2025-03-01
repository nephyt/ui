FROM openjdk:21-oracle

ARG PROPS_ENV

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY props/${PROPS_ENV}/* .
ENTRYPOINT ["java","-jar","/app.jar"]