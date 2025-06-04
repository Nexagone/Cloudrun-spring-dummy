# syntax=docker/dockerfile:1.4
# Stage 1: Build
FROM --platform=$BUILDPLATFORM maven:3.9.6-amazoncorretto-17 AS builder

# Installation des outils nécessaires et ajout d'un utilisateur non-root
RUN yum install -y shadow-utils && \
    groupadd -r spring && \
    useradd -r -g spring spring && \
    yum remove -y shadow-utils && \
    yum clean all
USER spring:spring

WORKDIR /home/spring/app

# Copie et téléchargement des dépendances en premier (pour le cache)
COPY --chown=spring:spring pom.xml .
RUN mvn dependency:go-offline

# Copie et compilation du code source
COPY --chown=spring:spring src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM --platform=$TARGETPLATFORM amazoncorretto:17-alpine

# Installation des packages de sécurité et création d'un utilisateur non-root
RUN apk add --no-cache dumb-init && \
    addgroup -S spring && \
    adduser -S spring -G spring

# Création et configuration du répertoire de l'application
WORKDIR /app
RUN chown spring:spring /app

# Copie du jar depuis le stage de build
COPY --from=builder --chown=spring:spring /home/spring/app/target/*.jar app.jar

# Vérification des permissions
RUN chmod 500 /app/app.jar

# Configuration des variables d'environnement
ENV SPRING_DATASOURCE_HOST=postgres \
    SPRING_DATASOURCE_PORT=5432 \
    SPRING_DATASOURCE_DB=testdb \
    SPRING_DATASOURCE_USERNAME=testuser \
    SPRING_DATASOURCE_PASSWORD=testpass

# Exposition du port
EXPOSE 8080

# Passage à l'utilisateur non-root
USER spring:spring

# Utilisation de dumb-init comme point d'entrée pour une meilleure gestion des signaux
ENTRYPOINT ["/usr/bin/dumb-init", "--"]

# Démarrage de l'application
CMD ["java", \
     "-Djava.security.egd=file:/dev/./urandom", \
     "-Djava.awt.headless=true", \
     "-XX:+UseContainerSupport", \
     "-XX:MaxRAMPercentage=75", \
     "-jar", \
     "app.jar"] 