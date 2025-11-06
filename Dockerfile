# ===========================================================
# ETAPA 1: BUILD (Compilação do Código)
# Usa o JDK (Java Development Kit) completo, pois ele é necessário para compilar.
# A imagem final não usará esta etapa, resultando em uma imagem pequena.
# ===========================================================
FROM eclipse-temurin:21-jdk-jammy AS build

# Variável que define o nome do JAR que será gerado (baseado no seu pom.xml)
ARG JAR_FILE=target/medix-api-0.0.1-SNAPSHOT.jar

# Define a pasta de trabalho
WORKDIR /app

# Copia os arquivos necessários para compilar (pom.xml, mvnw e o código fonte)
COPY pom.xml mvnw .
COPY .mvn .mvn
COPY src src

# Garante que o script do Maven seja executável e compila a aplicação
# -DskipTests: pular testes para acelerar a criação da imagem
RUN chmod +x mvnw
RUN ./mvnw package -DskipTests


# ===========================================================
# ETAPA 2: PRODUCTION (Ambiente de Execução)
# Usa JRE (Java Runtime Environment) e a tag 'slim' para tamanho mínimo.
# Requisito: Imagem base 'slim' ou 'alpine'.
# ===========================================================
FROM eclipse-temurin:21-jre-jammy-slim

# Define a pasta de trabalho da aplicação final
WORKDIR /usr/app

# -----------------------------------------------------------
# Configuração de Segurança (Requisito: Usuário sem Privilégios)
# -----------------------------------------------------------
# 1. Cria um grupo e um usuário do sistema ('appuser') sem privilégios administrativos
RUN groupadd --system appgroup && useradd --system --gid appgroup appuser

# 2. Define o usuário 'appuser' como o usuário de execução
USER appuser

# Copia o JAR compilado da ETAPA 1 para a pasta da ETAPA 2 (como 'app.jar')
# O comando '--from=build' é o que torna o build multi-stage
COPY --from=build /app/${JAR_FILE} /usr/app/app.jar

# Expõe a porta usada pelo Spring Boot (conforme seu application.yml)
EXPOSE 8080

# Comando para iniciar a aplicação
# O 'docker-compose -d' fará com que este comando seja executado em background
CMD ["java", "-jar", "app.jar"]