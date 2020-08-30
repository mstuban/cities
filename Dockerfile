FROM openjdk:14-slim
WORKDIR /usr/src/app
COPY build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT java -jar -Dspring.profiles.active=dev  -Dspring.datasource.url=jdbc:postgresql://dbpostgresql:5433/cities app.jar
