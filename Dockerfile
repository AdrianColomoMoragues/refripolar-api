# ---- Etapa 1: compilar el .war con Maven ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q package -DskipTests

# ---- Etapa 2: desplegar en Tomcat 10.1 (Jakarta EE 10) ----
FROM tomcat:10.1-jre17-temurin
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /app/target/apirefripolar.war /usr/local/tomcat/webapps/apirefripolar.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
