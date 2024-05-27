FROM bellsoft/liberica-openjdk-alpine:latest
EXPOSE 9007
COPY ./target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]