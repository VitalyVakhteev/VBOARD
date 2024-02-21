FROM adoptopenjdk/openjdk21:alpine-jre
LABEL maintainer="vitaly.v@techusage.com"

VOLUME /tmp
EXPOSE 8080

# Todo: Create containerization for production
#ARG JAR_FILE=target/app.jar
#
#ADD ${JAR_FILE} app.jar
#
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]