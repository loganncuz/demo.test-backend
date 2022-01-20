FROM openjdk:8-jdk-alpine
ARG JAR_FILE=build/libs/*.jar
# VOLUME /tmp
EXPOSE 8050
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]