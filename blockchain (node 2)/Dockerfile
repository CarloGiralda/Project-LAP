FROM bellsoft/liberica-openjdk-alpine:latest
EXPOSE 9012
COPY ./target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]