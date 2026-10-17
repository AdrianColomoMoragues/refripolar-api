# ---- Etapa 1: compilar el .war con Maven ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q package -DskipTests

# ---- Etapa 2: desplegar en Tomcat 10.1 (Jakarta EE 10) ----
FROM tomcat:10.1-jre17-temurin
# Desactiva el puerto de apagado (8005): así el único puerto abierto es el HTTP
# y plataformas como Render no se confunden al detectarlo.
RUN rm -rf /usr/local/tomcat/webapps/* \
 && sed -i 's/port="8005"/port="-1"/' /usr/local/tomcat/conf/server.xml
COPY --from=build /app/target/apirefripolar.war /usr/local/tomcat/webapps/apirefripolar.war
ENV PORT=8080
EXPOSE 8080
# Tomcat escucha en $PORT (8080 por defecto; Render puede indicar otro)
CMD ["sh", "-c", "sed -i \"s/port=\\\"8080\\\"/port=\\\"${PORT}\\\"/\" /usr/local/tomcat/conf/server.xml && exec catalina.sh run"]
