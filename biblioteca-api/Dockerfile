# ============================================================
# ETAPA 1: BUILD
# Compila la aplicación con Maven y JDK 21
# ============================================================
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
RUN ./mvnw package -DskipTests
# ============================================================
# ETAPA 2: LAYERS
# Extrae el JAR ejecutable en capas para aprovechar la caché
# ============================================================
FROM eclipse-temurin:21-jdk AS layers
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
RUN java -Djarmode=tools -jar app.jar extract --layers --destination extracted
# ============================================================
# ETAPA 3: RUNTIME
# Imagen final mínima, solo con JRE y las capas extraídas
# ============================================================
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=layers /app/extracted/dependencies/ ./
COPY --from=layers /app/extracted/spring-boot-loader/ ./
COPY --from=layers /app/extracted/snapshot-dependencies/ ./
COPY --from=layers /app/extracted/application/ ./
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
