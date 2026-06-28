# Etape 1 : build du projet avec Maven
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Cache des dependances : on copie d'abord le pom, puis on telecharge
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Copie du code source et construction du jar (sans les tests)
COPY src ./src
RUN mvn -B clean package -DskipTests

# Etape 2 : image legere pour l'execution
FROM eclipse-temurin:17-jre
WORKDIR /app

# Recupere le jar genere a l'etape de build
COPY --from=build /app/target/*.jar app.jar

# Render fournit la variable PORT ; l'application l'utilise via application.properties
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
