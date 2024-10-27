# Card Authorizer

Este projeto é uma API REST para gerenciamento e autorização de transações financeiras com diferentes tipos de saldo (meal, cash, food). Ele permite criar, consultar e atualizar o saldo de contas e autorizar transações. A API é construída com Spring Boot, com persistência de dados usando JPA e banco de dados relacional.

## Tecnologias

- **Java** 21
- **Spring Boot** 3
- **Spring Data JPA**
- **Banco de Dados**: PostgreSQL
- **Docker** e **Docker Compose**

## Pré-requisitos

Certifique-se de ter o seguinte software instalado em sua máquina:

- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/)

## Executando o Projeto

### Passo 1: clonar o Repositório

Clone o repositório para sua máquina local:

```bash
git clone https://github.com/dvmrabelo94/card-authorizer.git
cd card-authorizer
```

### Passo 2: executar o Docker Compose

```bash
docker-compose up -d
```

#### Caso precise visualizar os logs
```bash
docker-compose logs -f
```

### Passo 3: APIs disponíveis

* http://localhost:8080/api/balance
  * Criar saldo: POST /api/balance
  * Depositar em conta: PUT /api/balance/{account}/deposit
  * Consultar todos os saldos: GET /api/balance
  * Consultar saldo por conta: GET /api/balance/{account}
  * Autorizar transação: POST /api/transaction 
* http://localhost:8080/api/transactions
  * Processa uma transação de cartão de crédito: POST /authorize


## Observações:

### Inicialização da tabela balance

* No arquivo data.sql possui uma lista com 50 saldos de clientes para poder testar o código com maior facilidade.
* A estratégia utilizada para não permitir inserções ao mesmo tempo foi Locking pessimistic, para evitar sobreescrita de transações simultâneas na mesma linha do banco de dados.
* A estratégia para mapear possíveis erros de mcc é "ilustrativa", não precisa ser fixa no código, pode ser uma tabela no banco de dados trazendo todas as possíveis palavras que dão match ou com food ou com meal, foi feita uma lista no código só para demonstrar a solução e lógica.