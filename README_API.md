# SControl - Sistema de Controle Financeiro Familiar

## Descrição

Sistema de controle financeiro desenvolvido em Spring Boot para gerenciar as finanças de famílias, permitindo o controle de usuários, contas, transações e relatórios financeiros.

## Funcionalidades

- ✅ Gestão de usuários
- ✅ Gestão de famílias (relacionamento Many-to-Many com usuários)
- ✅ Gestão de contas bancárias por usuário
- ✅ Gestão de transações (receitas e despesas)
- ✅ Relatórios financeiros detalhados
- ✅ Categorização de transações

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.5.0
- Spring Data JPA
- Hibernate
- H2 Database (em memória)
- Lombok
- Maven

## Estrutura da API

### Controllers Implementados

#### 1. UserController (`/api/users`)
- `POST /api/users` - Criar usuário
- `GET /api/users` - Listar todos os usuários
- `GET /api/users/{id}` - Buscar usuário por ID
- `PUT /api/users/{id}` - Atualizar usuário
- `DELETE /api/users/{id}` - Deletar usuário

#### 2. FamilyController (`/api/families`)
- `POST /api/families` - Criar família
- `GET /api/families` - Listar todas as famílias
- `GET /api/families/{id}` - Buscar família por ID
- `PUT /api/families/{id}` - Atualizar família
- `DELETE /api/families/{id}` - Deletar família
- `POST /api/families/{familyId}/users/{userId}` - Adicionar usuário à família
- `DELETE /api/families/{familyId}/users/{userId}` - Remover usuário da família

#### 3. AccountController (`/api/accounts`)
- `POST /api/accounts` - Criar conta
- `GET /api/accounts` - Listar todas as contas
- `GET /api/accounts/{id}` - Buscar conta por ID
- `GET /api/accounts/user/{userId}` - Listar contas por usuário
- `PUT /api/accounts/{id}` - Atualizar conta
- `DELETE /api/accounts/{id}` - Deletar conta

#### 4. TransactionController (`/api/transactions`)
- `POST /api/transactions` - Criar transação (atualiza automaticamente o saldo da conta)
- `GET /api/transactions` - Listar todas as transações
- `GET /api/transactions/{id}` - Buscar transação por ID
- `GET /api/transactions/account/{accountId}` - Listar transações por conta
- `GET /api/transactions/category/{category}` - Listar transações por categoria
- `GET /api/transactions/period?startDate=yyyy-mm-dd&endDate=yyyy-mm-dd` - Transações por período
- `GET /api/transactions/account/{accountId}/period?startDate=yyyy-mm-dd&endDate=yyyy-mm-dd` - Transações por conta e período
- `PUT /api/transactions/{id}` - Atualizar transação (reajusta o saldo da conta)
- `DELETE /api/transactions/{id}` - Deletar transação (reverte o saldo da conta)

#### 5. ReportController (`/api/reports`)
- `GET /api/reports/balance/user/{userId}` - Saldo total do usuário
- `GET /api/reports/expenses/user/{userId}` - Relatório de gastos do usuário (com filtro de período opcional)
- `GET /api/reports/income/user/{userId}` - Relatório de receitas do usuário (com filtro de período opcional)
- `GET /api/reports/summary/user/{userId}` - Resumo financeiro completo do usuário

#### 6. CategoryController (`/api/categories`)
- `GET /api/categories` - Listar todas as categorias utilizadas

## Modelos de Dados

### User
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@email.com",
  "cpf": "12345678901",
  "createdAt": "2025-01-10"
}
```

### Family
```json
{
  "id": 1,
  "name": "Família Silva",
  "users": [...]
}
```

### Account
```json
{
  "id": 1,
  "name": "Conta Corrente",
  "description": "Conta principal",
  "balance": 5000.00,
  "user": {...}
}
```

### Transaction
```json
{
  "id": 1,
  "description": "Salário",
  "amount": 3500.00,
  "account": {...},
  "date": "2025-01-10",
  "category": "Salário"
}
```

## Como Executar

1. **Clone o projeto:**
```bash
git clone <repository-url>
cd scontroll
```

2. **Execute a aplicação:**
```bash
mvn spring-boot:run
```

3. **Acesse a aplicação:**
- API: http://localhost:8080
- Console H2: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:db`
  - Username: `sa`
  - Password: `senha`

## Dados de Exemplo

A aplicação já vem com dados de exemplo pré-carregados:

- **Usuários**: João Silva e Maria Santos
- **Família**: Família Silva (com ambos os usuários)
- **Contas**: 3 contas (2 do João, 1 da Maria)
- **Transações**: 5 transações de exemplo (salários, gastos diversos)

## Funcionalidades Especiais

### Gestão Automática de Saldo
- Ao criar uma transação, o saldo da conta é automaticamente atualizado
- Valores positivos = receitas, valores negativos = despesas
- Ao editar/deletar transações, o saldo é reajustado automaticamente

### Relatórios Inteligentes
- Cálculo automático de totais por categoria
- Separação automática entre receitas e despesas
- Filtros por período de data
- Resumos financeiros completos

### Relacionamentos
- **User ↔ Family**: Many-to-Many (um usuário pode estar em várias famílias)
- **User → Account**: One-to-Many (um usuário pode ter várias contas)
- **Account → Transaction**: One-to-Many (uma conta pode ter várias transações)

## Próximos Passos

- [ ] Implementar autenticação e autorização
- [ ] Adicionar validações mais robustas
- [ ] Implementar paginação nas listagens
- [ ] Adicionar testes unitários e de integração
- [ ] Criar interface web (frontend)
- [ ] Implementar notificações
- [ ] Adicionar mais tipos de relatórios

## Contribuição

Sinta-se à vontade para contribuir com melhorias, correções de bugs ou novas funcionalidades!
