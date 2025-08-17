FROM eclipse-temurin:21-jdk-alpine
#VOLUME /tmp
COPY build/libs/catalog-service-1.0.jar CatalogService.jar
ENTRYPOINT ["java","-jar","CatalogService.jar"]