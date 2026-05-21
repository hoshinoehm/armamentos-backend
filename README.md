# SISACAF — Backend

![Java](https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL_8-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

API REST do **Sistema de Controle de Armamento e Fardamento (SISACAF)** do **31º Batalhão de Polícia Militar do Maranhão**. Responsável pelo gerenciamento de militares, cautelas de armas e coletes, geração de relatórios PDF e controle de acesso.

---

## Funcionalidades

- Cadastro e gestão de **militares** (efetivo do batalhão)
- Controle de **cautelas de armas e coletes** (saída, devolução, permanência)
- Geração de **documentos PDF** oficiais (requerimento de cautela, concordância do comandante, parecer, etc.)
- **Dashboard** com totais de armamento, colete e efetivo por sub-unidade
- Autenticação e autorização com **JWT**
- Gestão de **sub-unidades**, **funções** e localização geográfica (cidade/estado)

---

## Documentos gerados

| Documento | Descrição |
|---|---|
| Requerimento de Cautela | Solicitação formal de armamento |
| Cautela de Arma | Registro de saída de arma individual |
| Cautela Geral de Armas | Registro consolidado de armas por sub-unidade |
| Permanência de Cautela | Prorrogação de cautela ativa |
| Amparo Legal | Fundamento legal da cautela |
| Concordância do Comandante | Aprovação do comandante da OPM |
| Parecer do Comandante | Parecer para cautela externa |
| Observações da Arma | Registro de condições do armamento |
| Detalhes da Cautela | Relatório detalhado do processo |
| Relatório ACAF | Relatório para o processo ACAF |

---

## Tecnologias

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 21 |
| Framework | Spring Boot 3 |
| Segurança | Spring Security + JWT |
| Banco de dados | MySQL 8 |
| ORM | Spring Data JPA / Hibernate |
| Relatórios | JasperReports + Thymeleaf (HTML→PDF) |
| Build | Maven |
| Container | Docker |

---

## Pré-requisitos

- Java 21+
- Maven 3.9+
- MySQL 8 (ou Docker)

---

## Como rodar

### Com Docker (recomendado)

Na raiz do repositório de infraestrutura (onde está o `docker-compose.yml`):

```bash
docker compose up -d --build
```

A API estará disponível em `http://localhost:8080`.

### Local (sem Docker)

1. Crie o banco de dados MySQL:

```sql
CREATE DATABASE armamentos_db;
CREATE USER 'armamentos_user'@'localhost' IDENTIFIED BY 'armamentos_pass';
GRANT ALL PRIVILEGES ON armamentos_db.* TO 'armamentos_user'@'localhost';
```

2. Configure `src/main/resources/application.properties` com suas credenciais.

3. Inicie a aplicação:

```bash
./mvnw spring-boot:run
```

---

## Variáveis de ambiente (produção)

| Variável | Descrição |
|---|---|
| `SPRING_DATASOURCE_URL` | URL JDBC do MySQL |
| `SPRING_DATASOURCE_USERNAME` | Usuário do banco |
| `SPRING_DATASOURCE_PASSWORD` | Senha do banco |
| `API_SECURITY_TOKEN_SECRET` | Chave secreta para assinar JWT |
| `UNIDADE_NOME_CHEFE_P4` | Nome do chefe do P/4 |
| `UNIDADE_POSTO_CHEFE_P4` | Posto/graduação do chefe do P/4 |
| `UNIDADE_FUNCAO_CHEFE_P4` | Função do chefe do P/4 |
| `UNIDADE_NOME_COMANDANTE_OPM` | Nome do comandante da OPM |
| `UNIDADE_POSTO_COMANDANTE_OPM` | Posto/graduação do comandante |
| `UNIDADE_FUNCAO_COMANDANTE_OPM` | Função do comandante |

---

## Endpoints principais

### Autenticação
| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/auth/login` | Login — retorna token JWT |
| `POST` | `/auth/register` | Cadastro de usuário |

### Militares
| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/militares` | Lista todos os militares |
| `GET` | `/militares/{id}` | Busca militar por ID |
| `POST` | `/militares` | Cadastra novo militar |
| `PUT` | `/militares/{id}` | Atualiza dados do militar |
| `DELETE` | `/militares/{id}` | Remove militar |

### Controle de Armamento
| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/controle-armamento` | Lista todas as cautelas |
| `POST` | `/controle-armamento` | Registra nova cautela |
| `PUT` | `/controle-armamento/{id}/baixa` | Devolução de arma (baixa) |
| `GET` | `/controle-armamento/{id}/pdf/{tipo}` | Gera PDF do documento |

### Armas e Coletes
| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/armas` | Lista armas cadastradas |
| `GET` | `/coletes` | Lista coletes cadastrados |

### Dashboard
| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/dashboard` | Totais de armas, coletes e efetivo |

---

## Estrutura do projeto

```
armamentos-backend/
├── src/main/java/com/example/auth/
│   ├── config/            # Configurações (unidade policial, modelMapper)
│   ├── controllers/       # Endpoints REST
│   ├── domain/
│   │   ├── dto/           # Data Transfer Objects
│   │   └── enums/         # Enumerações (PostoGraduacao, Situacao, Sexo...)
│   ├── infra/security/    # JWT, filtros e configuração de segurança
│   ├── repositories/      # Interfaces Spring Data JPA
│   └── services/          # Regras de negócio e geração de PDFs
├── src/main/resources/
│   ├── application.properties
│   ├── reports/           # JRXMLs e JASPERs compilados
│   └── templates/         # Templates HTML (Thymeleaf) para PDF
└── pom.xml
```

---

## Licença

Uso interno — 31º Batalhão de Polícia Militar / PMMA. Todos os direitos reservados.
