# ----------------------------------------------------------------------------
# BUILD STAGE: Compila a aplicação Java
# Baseada em Alpine Linux, conforme o exercício.
# ----------------------------------------------------------------------------
FROM maven:3.9.9-eclipse-temurin-17-alpine AS build

# CORREÇÃO CRÍTICA DO ERRO SSL/HANDSHAKE (Alpine Linux):
# Garante que os certificados de segurança (ca-certificates) estejam atualizados,
# permitindo a conexão HTTPS com o repositório Maven.
RUN apk add --update ca-certificates && rm -rf /var/cache/apk/*

# 1. Define o diretório de trabalho padrão (Requisito: /app)
WORKDIR /app

# Copia e baixa as dependências (para cache e build mais rápido)
COPY pom.xml .
RUN mvn dependency:go-offline || echo "Ignorando erro de dependência para cache."

# 2. Copia o código-fonte
COPY . .

# 3. Realiza o Build da Aplicação com o Maven
RUN mvn clean package -DskipTests

# ----------------------------------------------------------------------------
# RUN STAGE: Cria a imagem de execução leve (Requisito: Alpine/Slim)
# ----------------------------------------------------------------------------
FROM eclipse-temurin:17-jre-alpine

# 1. Cria e usa o usuário sem privilégios 'userapp' (Requisito: Não-root)
RUN adduser -h /home/userapp -s /bin/sh -D userapp
USER userapp

# 2. Define o diretório de trabalho (Requisito: /app)
WORKDIR /app

# 3. Copia apenas o JAR do Build Stage
COPY --from=build /app/target/medix-api-0.0.1-SNAPSHOT.jar app.jar

# 4. Expõe a porta 8080
EXPOSE 8080

# 5. Comando de execução da aplicação (Requisito: Utilizando CMD)
CMD ["java", "-jar", "app.jar"]
