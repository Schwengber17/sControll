# SControl - Sistema de Controle Financeiro Familiar

## Descrição

Sistema de controle financeiro desenvolvido em Spring Boot para gerenciar as finanças de famílias, permitindo o controle de usuários, contas, transações e relatórios financeiros com validações robustas e tratamento de erros padronizado.

## Funcionalidades Implementadas

- ✅ **Gestão de usuários** com validações completas
- ✅ **Gestão de famílias** (relacionamento Many-to-Many com usuários)
- ✅ **Gestão de contas bancárias** por usuário com validações
- ✅ **Gestão de transações** (receitas e despesas) com atualização automática de saldo
- ✅ **Listagem de categorias** utilizadas nas transações
- ✅ **Sistema de validação** com Bean Validation (Jakarta Validation)
- ✅ **Tratamento global de erros** com respostas padronizadas
- ✅ **Prevenção de referências circulares** em JSON
- ✅ **Cascata de operações** (deletar usuário remove contas e transações)

## Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.5.0**
- **Spring Data JPA** + Hibernate
- **Spring Boot Starter Validation** (Jakarta Validation)
- **H2 Database** (em memória para desenvolvimento)
- **Lombok** (redução de boilerplate)
- **Maven** (gerenciamento de dependências)

## Estrutura da API

### UserController (`/api/users`)

| Método | Endpoint | Descrição | Validações |
|--------|----------|-----------|------------|
| `POST` | `/api/users` | Criar usuário | Nome (2-100), Email (válido, único), CPF (11 dígitos, único) |
| `GET` | `/api/users` | Listar todos os usuários | - |
| `GET` | `/api/users/{id}` | Buscar usuário por ID | ID deve existir |
| `PUT` | `/api/users/{id}` | Atualizar usuário (apenas nome, email, cpf) | Mesmas validações do POST |
| `DELETE` | `/api/users/{id}` | Deletar usuário (cascata: remove contas e transações) | ID deve existir |
| `POST` | `/api/users/{id}/accounts` | Criar conta para usuário específico | ID usuário válido + validações de conta |
| `GET` | `/api/users/{id}/accounts` | Listar contas do usuário | ID usuário válido |

**Exemplo de criação de usuário:**
```json
POST /api/users
{
  "nome": "João Silva",
  "email": "joao@email.com",
  "cpf": "12345678901"
}
```

**Exemplo de criação de conta via usuário:**
```json
POST /api/users/1/accounts
{
  "name": "Conta Corrente",
  "description": "Conta principal",
  "balance": 1000.00
}
```

### FamilyController (`/api/families`)

| Método | Endpoint | Descrição | Validações |
|--------|----------|-----------|------------|
| `POST` | `/api/families` | Criar família | Nome (2-20 caracteres) |
| `GET` | `/api/families` | Listar todas as famílias | - |
| `GET` | `/api/families/{id}` | Buscar família por ID | ID deve existir |
| `PUT` | `/api/families/{id}` | Atualizar família | Nome (2-20 caracteres) |
| `DELETE` | `/api/families/{id}` | Deletar família | ID deve existir |
| `POST` | `/api/families/{familyId}/users/{userId}` | Adicionar usuário à família | IDs válidos |
| `DELETE` | `/api/families/{familyId}/users/{userId}` | Remover usuário da família | IDs válidos |

### AccountController (`/api/accounts`)

| Método | Endpoint | Descrição | Validações |
|--------|----------|-----------|------------|
| `GET` | `/api/accounts` | Listar todas as contas | - |
| `GET` | `/api/accounts/{id}` | Buscar conta por ID | ID deve existir |
| `GET` | `/api/accounts/user/{userId}` | Listar contas por usuário | ID usuário válido |
| `PUT` | `/api/accounts/{id}` | Atualizar conta | Nome (2-100), Saldo ≥ 0 |
| `DELETE` | `/api/accounts/{id}` | Deletar conta (cascata: remove transações) | ID deve existir |

**⚠️ Nota:** A criação de contas deve ser feita via `/api/users/{id}/accounts` para garantir associação correta com o usuário.

### 💰 TransactionController (`/api/transactions`)

| Método | Endpoint | Descrição | Validações |
|--------|----------|-----------|------------|
| `POST` | `/api/transactions` | Criar transação (atualiza saldo automaticamente) | Amount obrigatório, Category obrigatória, Account válido |
| `GET` | `/api/transactions` | Listar todas as transações | - |
| `GET` | `/api/transactions/{id}` | Buscar transação por ID | ID deve existir |
| `GET` | `/api/transactions/account/{accountId}` | Listar transações por conta | ID conta válido |
| `GET` | `/api/transactions/category/{category}` | Listar transações por categoria | - |
| `PUT` | `/api/transactions/{id}` | Atualizar transação (reajusta saldo) | Mesmas validações do POST |
| `DELETE` | `/api/transactions/{id}` | Deletar transação (reverte saldo) | ID deve existir |

**Exemplo de transação:**
```json
POST /api/transactions
{
  "description": "Salário",
  "amount": 3500.00,
  "category": "Receita",
  "date": "2025-07-21",
  "account": {"id": 1}
}
```

###  CategoryController (`/api/categories`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/api/categories` | Listar todas as categorias utilizadas nas transações |

## 🔧 Sistema de Validações

### User
- **nome**: Obrigatório, 2-100 caracteres
- **email**: Obrigatório, formato válido, único, máximo 100 caracteres  
- **cpf**: Obrigatório, exatamente 11 dígitos, único

### Account
- **name**: Obrigatório, 2-100 caracteres
- **description**: Opcional, máximo 255 caracteres
- **balance**: Obrigatório, maior ou igual a 0

### Transaction
- **description**: Opcional, máximo 255 caracteres
- **amount**: Obrigatório (valores positivos = receitas, negativos = despesas)
- **category**: Obrigatório, máximo 50 caracteres
- **date**: Opcional, não pode ser futura (padrão: data atual)
- **account**: Obrigatório, deve existir

### Family
- **name**: Obrigatório, 2-20 caracteres

##  Sistema de Tratamento de Erros

A API implementa tratamento global de erros com respostas padronizadas.

**⚠️ Nota:** UserController usa exceções customizadas (ResourceNotFoundException), enquanto outros controllers ainda usam ResponseStatusException. Migração em andamento.

### Estrutura de Resposta de Erro
```json
{
  "status": 400,
  "error": "Dados inválidos",
  "message": "Falha na validação dos dados fornecidos",
  "path": "/api/users",
  "timestamp": "2025-07-21T23:45:00",
  "validationErrors": [
    {
      "field": "nome",
      "message": "Nome é obrigatório",
      "rejectedValue": null
    }
  ]
}
```

### Códigos de Status HTTP

| Status | Erro | Descrição |
|--------|------|-----------|
| `400` | **Bad Request** | Dados inválidos, JSON malformado, parâmetros incorretos |
| `404` | **Not Found** | Recurso não encontrado (usuário, conta, etc.) |
| `409` | **Conflict** | Violação de integridade (email/CPF duplicados) |
| `422` | **Unprocessable Entity** | Erro de regra de negócio |
| `500` | **Internal Server Error** | Erro interno do servidor |

### Exemplos de Erros Comuns

**1. Validação de campos:**
```json
{
  "status": 400,
  "error": "Dados inválidos",
  "validationErrors": [
    {
      "field": "email",
      "message": "Email deve ter um formato válido",
      "rejectedValue": "email-invalido"
    }
  ]
}
```

**2. Recurso não encontrado:**
```json
{
  "status": 404,
  "error": "Recurso não encontrado",
  "message": "User não encontrado com id: 999"
}
```

**3. Violação de integridade:**
```json
{
  "status": 409,
  "error": "Violação de integridade",
  "message": "Já existe um registro com esses dados únicos (email, CPF, etc.)"
}
```

## Modelos de Dados

### User
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@email.com",
  "cpf": "12345678901",
  "createdAt": "2025-07-21"
}
```

### Family
```json
{
  "id": 1,
  "name": "Família Silva",
  "users": [
    {"id": 1, "nome": "João Silva", ...},
    {"id": 2, "nome": "Maria Silva", ...}
  ]
}
```

### Account
```json
{
  "id": 1,
  "name": "Conta Corrente",
  "description": "Conta principal",
  "balance": 5000.00
}
```

### Transaction
```json
{
  "id": 1,
  "description": "Salário",
  "amount": 3500.00,
  "category": "Receita",
  "date": "2025-07-21",
  "account": {"id": 1}
}
```

## Como Executar

### Pré-requisitos
- Java 21+
- Maven 3.8+
- Porta 8080 disponível

### Executar a API
```bash
# Clone o projeto
git clone <repository-url>
cd scontroll

# Execute a aplicação
mvn spring-boot:run
```

### Executar o Frontend (Angular)
```bash
# Navegue para a pasta frontend
cd frontend

# Instale as dependências (primeira vez)
npm install

# Execute o servidor de desenvolvimento
npm start
```

### Acessos
- **API**: http://localhost:8080
- **Frontend**: http://localhost:4200 (quando executado)
- **Console H2**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:db`
  - Username: `sa`
  - Password: (deixe em branco)

## Dados de Exemplo

A aplicação inicia com dados pré-carregados para facilitar os testes:

### Usuários
- **João Silva** (joao@email.com, CPF: 12345678901)
- **Maria Santos** (maria@email.com, CPF: 98765432100)

### Família
- **Família Silva** (ambos usuários incluídos)

### Contas
- **Conta Corrente João** (Saldo: R$ 1.000,00)
- **Conta Poupança João** (Saldo: R$ 5.000,00)  
- **Conta Corrente Maria** (Saldo: R$ 2.500,00)

### Transações
- Salário João: +R$ 3.500,00
- Conta de luz: -R$ 150,00
- Supermercado: -R$ 300,00
- Salário Maria: +R$ 4.000,00
- Internet: -R$ 80,00

## Funcionalidades Especiais

### Gestão Automática de Saldo
- Transações atualizam automaticamente o saldo da conta
- Valores positivos = receitas | Valores negativos = despesas
- Edições/exclusões reajustam o saldo automaticamente

###  Relacionamentos Seguros
- **User ↔ Family**: Many-to-Many com gestão de associações
- **User → Account**: One-to-Many com cascata de exclusão
- **Account → Transaction**: One-to-Many com cascata de exclusão
- **Prevenção de referências circulares** em JSON

### 🛡️ Proteções Implementadas
- **Cascata inteligente**: Deletar usuário remove contas e transações
- **Validação rigorosa**: Todos os dados são validados antes da persistência
- **Integridade referencial**: Relacionamentos são mantidos consistentes
- **Tratamento de erros**: Respostas padronizadas para facilitar integração

## Próximos Passos

### Backend
- [ ] **Implementar sistema de relatórios** - Criar ReportController para análises financeiras
- [ ] Implementar autenticação JWT
- [ ] Adicionar autorização baseada em roles
- [ ] Implementar paginação nas listagens
- [ ] Adicionar filtros por período nas transações
- [ ] Adicionar testes unitários e de integração
- [ ] Configurar banco de dados PostgreSQL para produção
- [ ] Implementar cache com Redis
- [ ] Adicionar documentação OpenAPI/Swagger

### Frontend
- [ ] Desenvolver interface Angular completa
- [ ] Implementar dashboard com gráficos
- [ ] Adicionar formulários reativos
- [ ] Criar sistema de notificações
- [ ] Implementar PWA (Progressive Web App)
- [ ] Adicionar temas escuro/claro

### DevOps
- [ ] Configurar CI/CD
- [ ] Containerização com Docker
- [ ] Deploy em cloud (AWS/Azure)
- [ ] Monitoramento e logging

## Arquivos de Documentação

- **`README_API.md`** - Esta documentação completa da API
- **`TRATAMENTO_ERROS.md`** - Detalhes sobre o sistema de erros
- **`TESTES_API.md`** - Exemplos de testes da API
- **`TODO.md`** - Lista de tarefas e melhorias pendentes



