# 🩺 MEDIX API - DEVOPS E CLOUD COMPUTING (SPRINT 2)

## ☁️ Visão Geral do Projeto

Este repositório contém a aplicação **Medix API**, desenvolvida em **Spring Boot (Java 21)**.  
O objetivo principal deste projeto é a aplicação prática de conceitos de **DevOps Tools** e **Cloud Computing**, utilizando **Máquinas Virtuais (VMs)** e **Contêineres Docker** para orquestração e *deployment*.

O foco principal é a implantação de uma solução contêinerizada em um provedor de Nuvem (**Azure**) de forma otimizada e segura.

---

## 🛠️ Tecnologias Utilizadas

| Categoria | Tecnologia | Versão/Detalhes |
| :--- | :--- | :--- |
| **Linguagem/Framework** | Java | 21 |
| **Framework Web** | Spring Boot | 3.3.0 |
| **Gerenciador de Build** | Maven | (via `mvnw` wrapper) |
| **Provedor de Nuvem** | Azure | (Uso de Virtual Machine) |
| **Contêinerização** | Docker e Docker Compose | Multi-Stage Build, Imagem `slim`, `non-root user` |
| **Banco de Dados** | Oracle | (Conexão via JDBC) |

---

## 🚀 Instruções de Deployment e Teste

As instruções abaixo descrevem o processo de implantação da aplicação em uma Máquina Virtual Linux (Ubuntu Server) configurada com o Docker Engine.

### 🧩 Pré-requisitos na Máquina Host (VM do Azure)

1. **Máquina Virtual:**  
   Uma VM Linux (Recomendado: Ubuntu Server LTS) provisionada no Azure, com a Porta `8080` liberada no Grupo de Segurança de Rede (NSG).

2. **Docker Engine:**  
   O Docker Engine e o pacote `docker.io` devem estar instalados e o serviço ativo.

3. **Docker Compose:**  
   A ferramenta `docker-compose` deve estar instalada:
   ```bash
   sudo apt install docker-compose
   ```

---

### 1. 📦 Clonar o Repositório e Navegar até o Projeto

No terminal SSH da sua VM, execute:

```bash
# Instala o Git (se não estiver instalado)
sudo apt update && sudo apt install -y git

# Clona o projeto
git clone https://github.com/challengeoracle/sprint-1-java.git

# Entra na pasta do projeto
cd sprint-1-java
```

---

### 2. 🔐 Configurar Variáveis de Ambiente

A aplicação e o Docker Compose precisam das credenciais do banco de dados Oracle e da chave JWT, configuradas no `application.yml`.

Defina as seguintes variáveis de ambiente — **substitua os valores de `DB_PASS` e `JWT_SECRET_KEY` pelos seus dados reais:**

```bash
# Credenciais do Banco de Dados Oracle
export DB_URL="jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL"
export DB_USER="rm*****"
export DB_PASS="senha"

# Chave Secreta JWT
export JWT_SECRET_KEY="aec3ec1f-53aa-4e82-93e7-702ab0194b80"
```

---

### 3. 🐳 Construir e Iniciar os Contêineres (Deployment)

O comando abaixo instrui o Docker Compose a construir a imagem (usando o `Dockerfile`) e iniciar o contêiner em modo *detached* (segundo plano), conforme o **Requisito 2**:

```bash
# O '-d' (detached mode) garante a execução em background
docker-compose up -d --build
```

---

### 4. 🧭 Verificar o Status

Aguarde alguns minutos para a aplicação Spring Boot iniciar (ela precisa baixar o Java 21, compilar e subir o servidor).

**Verifique se o contêiner está rodando (Requisito: Execução em Background):**

```bash
docker-compose ps
# O estado deve ser 'Up' (Rodando)
```

**Visualize os logs para garantir que a API está funcionando corretamente:**

```bash
docker-compose logs -f
# Procure pela mensagem "Started MedixApiApplication in ..."
```

---

### 5. 🔎 Teste de Funcionamento (API)

A API deve estar acessível na porta **8080** do IP público da sua VM.

**Endpoint de Login (POST):**
- **URL:**  
  `http://[IP_PUBLICO_VM]:8080/api/auth/login`
- **Corpo da Requisição (JSON):**
  ```json
  {
    "email": "admin@medix.com",
    "senha": "senha123"
  }
  ```
- **Resultado Esperado:**  
  Código `200 OK` com um objeto contendo o `token` JWT.

✅ Se este teste funcionar, significa que a **VM, Docker, Aplicação Java e conexão com o Oracle** estão funcionando corretamente.

---

## 🧐 Detalhes Técnicos do Contêiner

| Arquivo/Configuração | Requisito Atendido | Detalhes |
| :--- | :--- | :--- |
| **`Dockerfile` (Multi-Stage)** | Uso de Dockerfile | Separa o ambiente de *build* do ambiente de *runtime*, reduzindo o tamanho da imagem final. |
| **Imagem Base `slim`** | Imagens Docker de melhor desempenho (`slim`) | Usa `eclipse-temurin:21-jre-jammy-slim`, que é otimizada e muito menor que o JDK completo. |
| **`USER appuser`** | Aplicativo rodando sem privilégios | Cria o usuário `appuser` sem privilégios `root`, aumentando a segurança (Requisito 2). |
| **`docker-compose up -d`** | Execução em *background* | Garante que o contêiner rode de forma persistente e reinicie em caso de falha (`restart: always`). |

---

## 👥 Integrantes do Grupo

- **Arthur Thomas Mariano de Souza (RM 561061)**
- **Davi Cavalcanti Jorge (RM 559873)**
- **Mateus da Silveira Lima (RM 559728)**

---

✨ *Projeto desenvolvido como parte da Sprint 4 de DevOps e Cloud Computing - FIAP 2025.*
