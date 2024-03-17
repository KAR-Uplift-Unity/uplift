FROM maven:3-openjdk-17 AS build

COPY . /app

WORKDIR /app

RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim

COPY --from=build /app/target/KAR-Uplift-Unity-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]


