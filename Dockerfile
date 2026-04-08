# ====== build stage ======
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Copia primeiro para aproveitar cache
COPY pom.xml .
RUN mvn -q -e -DskipTests dependency:go-offline

# Copia o restante do projeto e builda o jar
COPY . .
RUN mvn -q -DskipTests clean package

# ====== runtime stage ======
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copia o JAR gerado (padrão do spring-boot-maven-plugin)
COPY --from=builder /app/target/*.jar app.jar

ENV JAVA_OPTS=""
EXPOSE 8080

# Roda o app
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]