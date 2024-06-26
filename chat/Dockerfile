FROM bellsoft/liberica-openjdk-alpine:latest
EXPOSE 9006
COPY ./target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]