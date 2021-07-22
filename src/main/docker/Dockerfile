# Reference -- 1. https://snyk.io/blog/best-practices-to-build-java-containers-with-docker/
#              2.https://dzone.com/articles/dockerizing-spring-boot-application-1
#               3. https://www.callicoder.com/spring-boot-mysql-react-docker-compose-example/ - it contains details for 3 services

# Start with a base image containing Java runtime
FROM openjdk:16.0.1-jdk

# Add Maintainer Info
MAINTAINER "tantragnyana@gmail.com"

# SpringBoot  default active profile
ENV ENV_NAME dev

# Java OPTS params
ENV BOOTAPP_JAVA_OPTS -Xms256m -Xmx1024m

#Container log directory
ENV LOG_PATH = "/logs"

# Make port 8080 available to the world outside this container
EXPOSE 8900

# The application's jar file
ARG JAR_FILE=target/*.jar

# Add the application's jar to the container
ADD ${JAR_FILE} pen-backend.jar

# Run the jar file
ENTRYPOINT ["java","-jar","/pen-backend.jar"]
