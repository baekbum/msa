FROM eclipse-temurin:21-jdk-alpine
#VOLUME /tmp
COPY build/libs/service-discovery-1.0.jar DiscoveryService.jar
ENTRYPOINT ["java","-jar","DiscoveryService.jar"]