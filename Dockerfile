# ===================================================================
# ESTÁGIO 1: BUILD (Compilação)
# Imagem completa para compilar o projeto.
# ===================================================================
FROM maven:3.9-eclipse-temurin-21 AS builder

# Diretório de trabalho padrão
WORKDIR /app

# Copia APENAS o pom.xml e a pasta src. 
# Removida a cópia de .mvn/ e mvnw para evitar o erro.
COPY pom.xml ./
COPY src ./src

# Baixa as dependências e compila usando o 'mvn' JÁ INSTALADO na imagem base.
RUN mvn dependency:go-offline
RUN mvn package -DskipTests

# ===================================================================
# ESTÁGIO 2: RUN (Execução - Otimizado com Alpine)
# Imagem baseada em JRE 21 Alpine (a menor disponível).
# ===================================================================
FROM eclipse-temurin:21-jre-alpine

# Define o diretório de trabalho padrão
WORKDIR /app

# Cria e troca para um usuário sem privilégios (appuser) para segurança
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copia o artefato .jar do estágio de build
COPY --from=builder /app/target/medix-api-0.0.1-SNAPSHOT.jar medix-api.jar

# Expõe a porta padrão do Spring Boot
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "medix-api.jar"]
