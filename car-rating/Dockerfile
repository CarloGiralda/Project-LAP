FROM bellsoft/liberica-openjdk-alpine:latest
EXPOSE 9004
COPY ./target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]