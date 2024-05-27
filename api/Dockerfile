FROM bellsoft/liberica-openjdk-alpine:latest
EXPOSE 8080
COPY ./target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]