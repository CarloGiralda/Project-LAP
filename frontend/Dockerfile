FROM bellsoft/liberica-openjdk-alpine:latest
EXPOSE 8081
COPY ./target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]