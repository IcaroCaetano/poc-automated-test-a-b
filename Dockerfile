# =========================
# Step 1: Build with Maven
# =========================
FROM maven:3.9.5-jdk-21 AS build
WORKDIR /app

# copy pom.xml and files of wrapper Maven
COPY pom.xml mvnw* ./
COPY .mvn .mvn

# Ensure the wrapper Maven
RUN mvn -q -N io.takari:maven:wrapper -Dmaven=3.9.5 || true

# Copy o code-font
COPY src src

# Build of application without execute the tests
RUN mvn -B -DskipTests package

# =========================
# Step 2: Final Image
# =========================
FROM eclipse-temurin:23-jre
WORKDIR /app

# Variables de JVM
ENV JAVA_OPTS="-XX:+UseZGC -XX:MaxRAMPercentage=75"

# Copy the JAR step build
COPY --from=build /app/target/*.jar app.jar

# Exposition of port application
EXPOSE 8080

# Command to run apllication
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
