# 🚀 Sistema de Gestão de Solicitações (SGS)

[![Java 17](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue.svg)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)](https://www.docker.com/)

O **Sistema de Gestão de Solicitações (SGS)** é uma solução web Full-Stack desenvolvida como parte do Desafio Técnico da SergipeTec para o cargo de Programador de Sistemas de Computação.

O objetivo principal da aplicação é apoiar o controle de solicitações de pagamento, substituindo processos manuais e garantindo maior organização, rastreabilidade e controle do fluxo, exigindo forte domínio de persistência em banco relacional e regras de negócio.

---

## 🎯 Como o Projeto Atende aos Critérios de Avaliação

O projeto foi meticulosamente desenhado para atender 100% dos requisitos listados no documento do desafio, focando em simplicidade, clareza e boas práticas:

1. **Modelagem do Banco de Dados:** Entidades criadas (`Solicitante`, `Categoria` e `Solicitacao`) com integridade referencial (`@ManyToOne`) e restrições exatas (`cpf_cnpj UNIQUE`).
2. **Qualidade das Queries (SQL Nativo):** Em cumprimento à exigência estrita do desafio, a listagem de solicitações com filtros dinâmicos utiliza **NATIVE QUERY**, construída em tempo de execução via `StringBuilder` e `EntityManager`, permitindo a junção (JOIN) segura e flexível das tabelas.
3. **Implementação das Regras de Negócio (Máquina de Estados):** As regras de transição de status (`SOLICITADO` -> `LIBERADO` -> `APROVADO` -> `CANCELADO` / `REJEITADO`) estão **blindadas no backend** (camada de *Service*), lançando exceções caso haja tentativa de transição ilegal.
4. **Integração Frontend/Backend:** API RESTful robusta, consumida por uma Single Page Application (SPA) assíncrona feita em Vanilla JS, sem frameworks engessados, garantindo a exigência de "Frontend Livre".
5. **Organização e Documentação:** Arquitetura em camadas MVC clássica (Controller, Service, Repository), além de versionamento via Git com commits descritivos.
6. **Scripts Obrigatórios:** A criação das tabelas e a inserção dos registros mínimos foram automatizadas via **Flyway** e também estão documentadas ao fim deste README.

---

## ⚙️ Funcionalidades e Telas Implementadas

* **Cadastro de Solicitações:** Formulário dinâmico que vincula o Solicitante e a Categoria à nova solicitação. Toda solicitação nasce automaticamente com status `SOLICITADO`.
* **Listagem Dinâmica:** Tabela principal contendo Nome e CPF/CNPJ do Solicitante, Categoria, Valor, Data e Status.
* **Filtros Avançados:** Barra de pesquisa que refina as consultas utilizando o backend via Status, Categoria ou Período de Datas.
* **Atualização de Status Rápida:** Botões de ações na própria listagem para transição de status, renderizados reativamente para exibir apenas as ações legais.
* **Detalhamento:** Visualização completa em tela com todos os dados da solicitação específica.

---

## 🛠️ Stack Tecnológica

* **Backend:** Java 17, Spring Boot 3, Spring Web, Spring Data JPA.
* **Banco de Dados:** PostgreSQL 16 (Containers via Docker).
* **Migrações:** Flyway Migration (Garante a criação de DDL e DML automaticamente).
* **Frontend:** HTML5, CSS3 Customizado, JavaScript puro (Vanilla API Fetch).
* **Tratamento de Erros:** Exceções tratadas globalmente via `@RestControllerAdvice`.

---

## 🚀 Instruções de Execução

Você pode executar este projeto de duas maneiras, dependendo do seu ambiente.

### Opção 1: Via Docker (Recomendado ⭐)
Esta é a forma mais rápida, pois não exige a instalação do Java, Maven ou PostgreSQL na sua máquina.

**Pré-requisitos:** Docker e Docker Compose instalados.

1. **Clonar e Subir o Ambiente:**
   ```bash
   git clone [https://github.com/MarceloStos/SGS_Sistema_de_Gestao_de_Solicitacoes.git](https://github.com/MarceloStos/SGS_Sistema_de_Gestao_de_Solicitacoes.git)
   cd SGS_Sistema_de_Gestao_de_Solicitacoes
   docker-compose up --build -d
   ```

2. **Acessar o Sistema:** Abra http://localhost:8080 no seu navegador. 
   (O Docker compilará o projeto, subirá o banco e executará as migrações automaticamente).

### Opção 2: Exucução Local (Manual)
Caso prefira rodar a aplicação nativamente sem o Docker.

**Pré-requisitos:** Java 17 e PostgreSQL (versão 15 ou superior) instalados localmente.

1. **Configuração do Banco de Dados:**
   - Crie um banco de dados vazio no seu PostgreSQL chamado sgs_db.
   - Certifique-se de que as credenciais do seu banco local correspondam às do arquivo src/main/resources/application.properties 


(Padrão: USER: postgres, PASSWORD: postgres, PORTA: 5433). Caso contrário, atualize o arquivo com as suas credenciais.
   

2. **Rodar a Aplicação:**
   Na raiz do projeto, utilize o Maven Wrapper para executar:

   *No Linux/Mac:*
   ```bash
   ./mvnw spring-boot:run
   ```
   *No Windows:*
   ```cmd
   .\mvnw.cmd spring-boot:run
   ```
   *(Durante o startup, o Flyway rodará todos os scripts DDL e DML necessários. A aplicação subirá na porta 8080).*


3. **Acessar o Sistema:**
   👉 Abra seu navegador e acesse: [http://localhost:8080](http://localhost:8080)

---

## 💾 Scripts de Banco de Dados Obrigatórios (DDL e DML)

Para cumprir o requisito estrito do desafio, os scripts de criação de tabelas e inserção da massa de dados estão documentados abaixo (sendo executados automaticamente pelo Flyway nas versões V1 e V2):

### Criação das Tabelas (DDL)
```sql
CREATE TABLE categoria (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);

CREATE TABLE solicitante (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf_cnpj VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE solicitacao (
    id SERIAL PRIMARY KEY,
    solicitante_id BIGINT NOT NULL,
    categoria_id BIGINT NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    valor NUMERIC(10, 2) NOT NULL,
    data_solicitacao TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    CONSTRAINT fk_solicitante FOREIGN KEY (solicitante_id) REFERENCES solicitante(id),
    CONSTRAINT fk_categoria FOREIGN KEY (categoria_id) REFERENCES categoria(id)
);
```

### Inserção de Registros Iniciais (DML - Mínimo de 5 Registros)
```sql
INSERT INTO categoria (nome) VALUES ('Serviços'), ('Material'), ('Transporte'), ('Alimentação'), ('Outros');

INSERT INTO solicitante (nome, cpf_cnpj) VALUES
    ('Marcelo Santos', '001.002.003-45'),
    ('João Silva', '111.111.111-11'),
    ('Tech Solutions SA', '22.222.222/0001-22'),
    ('Maria Oliveira', '222.222.222-22'),
    ('Logística Express', '33.333.333/0001-33');
```