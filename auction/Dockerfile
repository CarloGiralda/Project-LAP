FROM bellsoft/liberica-openjdk-alpine:latest
EXPOSE 9013
COPY ./target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]