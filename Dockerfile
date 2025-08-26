## Docker **Dockerfile** (Maven multi-stage)

# Build
FROM maven:3.9.5-eclipse-temurin-23 AS build
WORKDIR /app

# Copy Maven build files
COPY pom.xml mvnw* ./
COPY .mvn/ .mvn/

RUN mvn -q -N io.takari:maven:wrapper -Dmaven=3.9.5 || true

# Copy source code
COPY src/ src/

RUN mvn -B -DskipTests package

# Run
FROM eclipse-temurin:23-jre
WORKDIR /app
ENV JAVA_OPTS="-XX:+UseZGC -XX:MaxRAMPercentage=75"

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]

