FROM bellsoft/liberica-openjdk-alpine:latest
EXPOSE 9003
COPY ./target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
