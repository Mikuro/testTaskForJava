FROM maven:3.8.3-openjdk-17 AS build

COPY /src /app/src
COPY /pom.xml /app/
RUN mvn -f /app/pom.xml clean package

FROM openjdk:17-alpine
COPY --from=build /app/target/testTaskParqour-0.0.1-SNAPSHOT.war /testTaskParqour-0.0.1-SNAPSHOT.war
ENTRYPOINT ["java", "-jar", "/testTaskParqour-0.0.1-SNAPSHOT.war"]