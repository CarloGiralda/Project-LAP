# Service port list
- Api gateway 8080
- Service discovery 8761
- Frontend 8081
- User service 9000, DB 5434
- Send Email 9001, DB 5434
- Car insertion 9002, DB 5433
- Car search 9003, DB 5433
- Payment 9004
- Car book 9005
- Chat 9006
- Notification 9007
- Select area 9008
- Payment service 9010
- Blockchain 9011
- Blockchain2 9012

# keys
-----BEGIN PRIVATE KEY-----
MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOkrIlPSksIjSrPq
Cllfp4pZrcuO6QdMM1+jy4O6EOspR9qfI15ngUb10hW7UHwQ4ppJDhOmYEUy0VlA
riPcOPProNFQuwrrh6yL9C1LLpzrIcMbB9lymc8AdzynvvR3IJHgRdNwico54eCz
EwNdfZyY7UPnGmBByuH9wYy0tW1lAgMBAAECgYEAkgmhE20E3DEsfajtWHLPxZhL
gP3+N8LjPlihg9yK8i7FwUkA5wTe9GnMbi4Gx93vf4CYH3iKPW0Jr+QR7YXl6LYf
7X/DeaDeG7eN7GW6VMlvaQG0iW8ytLf1rPqKBCftIjLhAmDUmwgORf6vWhBtkbQ2
yAO23+hU2sWmgq4c3/UCQQD6ElXOwpsybig8zGd2ZzFUKuE3ToYAhDq9A801AMhT
8/OVzaSqhAE+S5CYUNs/mV7+O91904w0DC2/r5EiqqizAkEA7rI3FHjRSeZnKr2/
jyW3fhDn1RualW1WR0pRGuXyMsVGSLQCoXtHtzfNA7SJhhQs7bUVpGbbPk5Hm4hP
gDUthwJAHs3tZgfQ6FhrennPcOVrTSvvDEoO9bdUKP5z48iR5M2TPuvTEfIq2Qqj
6C9loqL48aZcNWi+x5aMeCVfYn7VQwJAZbFVmAo1GCfqEiKPBxyJhVMMghVlkX3o
gSzfSvdmbUzwua6/ZC7SifNjC84RvSbBUXCRzN3pUC0ngtLbFAMT5wJAJe/2Tl8C
4GlSEKIFzU7q+cyhWNMIMa3SJlpQLzQfmqnatrC0xWuk4LWh+ErMPoGYVwuheZUl
DjCodM6+evi6Mg==
-----END PRIVATE KEY-----
-----BEGIN PUBLIC KEY-----
MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDpKyJT0pLCI0qz6gpZX6eKWa3L
jukHTDNfo8uDuhDrKUfanyNeZ4FG9dIVu1B8EOKaSQ4TpmBFMtFZQK4j3Djz66DR
ULsK64esi/QtSy6c6yHDGwfZcpnPAHc8p770dyCR4EXTcInKOeHgsxMDXX2cmO1D
5xpgQcrh/cGMtLVtZQIDAQAB
-----END PUBLIC KEY-----

# command for RabbitMQ
docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.12-management

# SQL data imports
1.ddl_auto set to create
2.in application.properties insert spring.jpa.defer-datasource-initialization=true spring.sql.init.mode=always
3.in src/main/resources insert a file named data.sql with the insert into statements (stored in github folder SQL_IMPORTS/{SERVICE_NAME})
docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.12-management

# to generate rsa keys and veirfy signature
https://8gwifi.org/RSAFunctionality?rsasignverifyfunctions=rsasignverifyfunctions&keysize=512
https://acte.ltd/utils/openssl
# initial data postgresql
https://www.baeldung.com/spring-boot-data-sql-and-schema-sql
# deployment
  # docker lightweight image
    eclipse-temurin:17-jre-alpine —> most lightweight Java 17 image
  # compose
    docker-compose.yml
  # dockerfile
    mvn package
    docker image build -t [microservice_name]:latest .
    docker run -p [inside_port]:[outside_port] docker-java-jar:latest
