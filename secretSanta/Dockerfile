FROM maven:3.8.3-openjdk-17-slim AS BUILDER
WORKDIR /opt/build/secretsanta
COPY /pom.xml /opt/build/secretsanta/pom.xml
COPY /src /opt/build/secretsanta/src
RUN mvn -f /opt/build/secretsanta/pom.xml clean package -B -DskipTests

FROM openjdk:17-alpine
WORKDIR /opt/app/secretsanta
COPY --from=BUILDER /opt/build/secretsanta/target/*.jar /opt/app/secretsanta/app.jar
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
EXPOSE 8080
CMD ["-jar", "java", "/opt/app/secretsanta/app.jar"]