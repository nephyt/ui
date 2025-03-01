FROM openjdk:21-oracle
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY props/dev/* .
ENTRYPOINT ["java","-jar","/app.jar"]