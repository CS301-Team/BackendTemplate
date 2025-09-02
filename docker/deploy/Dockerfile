# ============================ Stage 2 ============================
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .
COPY checkstyle.xml .
COPY .mvn/ .mvn/
COPY mvnw .

COPY src/ src/

RUN ./mvnw -B package -DskipTests --file pom.xml

# ============================ Stage 2 ============================

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

ARG USERNAME=appuser
ARG USER_UID=4000
ARG USER_GID=$USER_UID

RUN addgroup -g $USER_GID -S $USERNAME && \
    adduser -u $USER_UID -S $USERNAME -G $USERNAME

COPY --from=builder /app/target/*.jar app.jar

RUN chown -R $USERNAME:$USERNAME /app

USER $USERNAME

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]