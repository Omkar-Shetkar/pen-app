# Reference -- 1. https://snyk.io/blog/best-practices-to-build-java-containers-with-docker/

# Start with a base image containing Java runtime
FROM openjdk:16-jre-slim

# Add Maintainer Info
LABEL maintainer="light@gmail.com"

# Running applications with user privileges helps to mitigate some risks
#So, an important improvement to the Dockerfile is to run the application as a non-root user:
RUN addgroup --system javauser && adduser -S -s /bin/false -G javauser javauser
USER javauser:javauser

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8080 available to the world outside this container
EXPOSE 8080

# The application's jar file
ARG JAR_FILE=target/*.jar

# Add the application's jar to the container
ADD ${JAR_FILE} light.jar

# Run the jar file
ENTRYPOINT ["java","-jar","/light.jar"]