FROM ubuntu:latest
COPY ./target/CarInsertion-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
RUN apt update && apt upgrade -y
RUN apt install openjdk-17-jre -y
ENTRYPOINT ["java", "-jar", "/app.jar"]