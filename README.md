# 🩺 MEDIX API - DEVOPS E CLOUD COMPUTING (SPRINT 2)

## ☁️ Visão Geral do Projeto

Este repositório contém a aplicação **Medix API**, desenvolvida em **Spring Boot (Java 21)**.  
O objetivo principal deste projeto é aplicar, na prática, os conceitos de **DevOps Tools** e **Cloud Computing**, com foco em **contêinerização, automação e deploy em nuvem**.

A aplicação foi implantada em uma **Máquina Virtual (VM)** do **Microsoft Azure**, utilizando **Docker** e **Docker Compose** para orquestração, seguindo boas práticas de segurança, desempenho e portabilidade.

---

## 🛠️ Tecnologias Utilizadas

| Categoria                       | Tecnologia             | Versão/Detalhes                                          |
| :------------------------------ | :--------------------- | :------------------------------------------------------- |
| **Linguagem**                   | Java                   | 17                                                       |
| **Framework**                   | Spring Boot            | 3.3.0                                                    |
| **Gerenciador de Dependências** | Maven                  | Wrapper (`mvnw`)                                         |
| **Banco de Dados**              | Oracle                 | Conexão via JDBC                                         |
| **Contêinerização**             | Docker, Docker Compose | Multi-Stage Build, imagem `slim`, execução em background |
| **Provedor de Nuvem**           | Azure                  | Máquina Virtual Linux (Ubuntu Server LTS)                |

---

## 🚀 Passo a Passo de Implantação (Deployment na VM Azure)

As etapas a seguir descrevem o processo completo para clonar, configurar e executar o projeto dentro de uma **VM Linux** com **Docker** instalado.

### 🧩 1. Pré-requisitos na Máquina Host (VM Azure)

Antes de iniciar, verifique se sua **VM Ubuntu** possui:

-   Porta **8080** liberada no **Grupo de Segurança de Rede (NSG)**;
-   **Git**, **Docker Engine** e **Docker Compose** instalados e ativos.

Instalação rápida (caso necessário):

```
sudo apt update
sudo apt install -y git docker.io docker-compose
sudo systemctl enable docker
sudo systemctl start docker
```

### 📦 2. Clonar o Repositório

```

# Clona o projeto

git clone https://github.com/challengeoracle/sprint-1-java.git

# Entra no diretório

cd sprint-1-java
```

### 🐳 4. Construir e Subir o Contêiner

Execute o _build_ e o _deploy_ com Docker Compose:

```
docker compose up -d
```

Esse comando:

-   Cria a imagem da API via **Multi-Stage Build**;
-   Executa o contêiner em **background** (`-d`);
-   Garante reinicialização automática (`restart: always`).

### 🧭 5. Verificar o Status da Aplicação

```
docker-compose ps
```

Verifique os logs:

```
docker-compose logs -f
```

### 🔎 6. Testar a API

Com a aplicação em execução, acesse o IP público da sua VM:

**Endpoint de Login (POST):**

`http://[IP_PUBLICO_VM]:8080/api/swagger-ui/index.html`

---

## ⚙️ Estrutura e Boas Práticas de Deploy

| Arquivo                          | Propósito            | Implementação                                                        |
| :------------------------------- | :------------------- | :------------------------------------------------------------------- |
| **Dockerfile**                   | Criação da imagem    | Multi-Stage Build para separar build e runtime                       |
| **Imagem Base**                  | Desempenho otimizado | `eclipse-temurin:21-jre-jammy-slim`                                  |
| **Usuário sem privilégios root** | Segurança            | `USER appuser`                                                       |
| **docker-compose.yml**           | Orquestração         | Define o serviço `app`, reinício automático e execução em background |

---

## 👥 Integrantes do Grupo

-   **Arthur Thomas Mariano de Souza (RM 561061)**
-   **Davi Cavalcanti Jorge (RM 559873)**
-   **Mateus da Silveira Lima (RM 559728)**

---

Projeto desenvolvido como parte da Sprint 2 de DevOps e Cloud Computing – FIAP 2025.

