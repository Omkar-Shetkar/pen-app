# Reference -- 1. https://snyk.io/blog/best-practices-to-build-java-containers-with-docker/
#              2.https://dzone.com/articles/dockerizing-spring-boot-application-1
#               3. https://www.callicoder.com/spring-boot-mysql-react-docker-compose-example/ - it contains details for 3 services

# Start with a base image containing Java runtime
FROM tomcat:8.5-jdk15-openjdk-oracle

# Add Maintainer Info
LABEL maintainer="light@gmail.com"

# SspringBoot  default active profile
ENV ENV_NAME dev

# Java OPTS params
ENV BOOTAPP_JAVA_OPTS -Xms256m -Xmx1024m

#Container log directory
ENV LOG_PATH = "/logs"


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