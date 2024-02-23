FROM openjdk:21-jdk-slim
LABEL maintainer="vitaly.v@techusage.com"

# Set a working directory
WORKDIR /app

VOLUME /tmp
EXPOSE 8080

# Copy the built JAR file
ARG JAR_FILE=target/VBOARD-1.0.2.jar
COPY ${JAR_FILE} VBOARD-1.0.2.jar

## Copy the .env file to the working directory
#COPY .env .env

# Run the application
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","VBOARD-1.0.2.jar"]