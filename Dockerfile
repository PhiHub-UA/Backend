FROM maven:3.8.5-openjdk-17

WORKDIR /backend

COPY . .

ENTRYPOINT ["mvn", "spring-boot:run"]