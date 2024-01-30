# Service port list
- Api gateway 8080
- Service discovery 8761
- Frontend 8081
- User service 9000, DB 5434
- Send Email 9001, DB 5434
- Car insertion 9002, DB 5433
- Car search 9003, DB 5433
- User rating 9004
- Car book 9005
- Chat 9006
- Notification 9007
- Select area 9008
- Take out Insurance 9009
- Payment service 9010

# command for RabbitMQ
docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.12-management

# to generate rsa keys and veirfy signature
https://8gwifi.org/RSAFunctionality?rsasignverifyfunctions=rsasignverifyfunctions&keysize=512
https://acte.ltd/utils/openssl

# deployment
  # docker
    
    //DOCKERFILE
    FROM postgres 
    ENV POSTGRES_PASSWORD postgres 
    ENV POSTGRES_DB testdb 
    COPY init.sql /docker-entrypoint-initdb.d/
    //INIT DB
    CREATE TABLE public.persons (
        id int PRIMARY KEY,
        firstName varchar(255),
        lastName varchar(255),
        address varchar(255)
    );
    
    // BUILD
    docker build -t my-postgres-image .
    
    // DOCKER COMMAND PSQL
    docker run --name postgres-docker -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres
  # compose
    docker-compose.yml
  # dockerfile
    mvn package
    docker image build -t docker-java-jar:latest .
    docker run docker-java-jar:latest
