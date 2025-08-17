FROM eclipse-temurin:21-jdk-alpine
#VOLUME /tmp
COPY build/libs/config-service-1.0.jar ConfigService.jar
ENTRYPOINT ["java","-jar","ConfigService.jar"]