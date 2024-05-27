FROM bellsoft/liberica-openjdk-alpine:latest
EXPOSE 9002
COPY ./target/*.jar app.jar
ADD  ./src/main/resources/static .
ENTRYPOINT ["java","-jar","/app.jar"]
