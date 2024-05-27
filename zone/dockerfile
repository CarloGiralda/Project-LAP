FROM bellsoft/liberica-openjdk-alpine:latest
EXPOSE 9008
COPY ./target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]