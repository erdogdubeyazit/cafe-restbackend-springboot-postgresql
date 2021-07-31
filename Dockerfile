FROM maven:3.6.3-jdk-8-slim AS build
WORKDIR usr/src/app
COPY . ./
RUN mvn clean install
FROM openjdk:8-jdk-alpine
WORKDIR /usr/src/app
EXPOSE 8080
COPY --from=build /usr/src/app/target/coffeeshop-0.0.1-SNAPSHOT.jar ./app.jar
CMD ["java","-jar", "./app.jar"]