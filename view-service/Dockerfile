FROM eclipse-temurin:21-jdk-alpine
#VOLUME /tmp
COPY build/libs/view-service-1.0.jar ViewService.jar
ENTRYPOINT ["java","-jar","ViewService.jar"]