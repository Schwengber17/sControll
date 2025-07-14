# Testes da API - SControl

## Problemas Resolvidos

### ✅ **Problema de Referência Circular**
- **Problema**: JSON infinito ao fazer GET dos usuários devido ao relacionamento bidirecional User ↔ Family
- **Solução**: Adicionada anotação `@JsonIgnore` no campo `families` da entidade `User`

### ✅ **Adição da Lista de Contas no Usuário**
- **Melhoria**: Adicionado relacionamento `@OneToMany` entre User e Account
- **Benefícios**: 
  - Facilita a visualização das contas de cada usuário
  - Melhora a manipulação de dados
  - Preparado para futuras funcionalidades

## Endpoints para Teste

### 1. **GET /api/users** - Listar todos os usuários
```bash
curl -X GET http://localhost:8080/api/users
```

**Resposta esperada (sem referência circular):**
```json
[
  {
    "id": 1,
    "nome": "João Silva",
    "cpf": "12345678901",
    "email": "joao@email.com",
    "createdAt": "2025-07-10",
    "accounts": [
      {
        "id": 1,
        "name": "Conta Corrente João",
        "description": "Conta principal do João",
        "balance": 2670.00
      },
      {
        "id": 2,
        "name": "Poupança João",
        "description": "Conta poupança do João",
        "balance": 10000.00
      }
    ]
  },
  {
    "id": 2,
    "nome": "Maria Santos",
    "cpf": "98765432100",
    "email": "maria@email.com",
    "createdAt": "2025-07-10",
    "accounts": [
      {
        "id": 3,
        "name": "Conta Corrente Maria",
        "description": "Conta principal da Maria",
        "balance": 2600.00
      }
    ]
  }
]
```

### 2. **GET /api/users/{id}/with-accounts** - Usuário com contas carregadas
```bash
curl -X GET http://localhost:8080/api/users/1/with-accounts
```

### 3. **GET /api/families** - Listar famílias
```bash
curl -X GET http://localhost:8080/api/families
```

**Resposta esperada:**
```json
[
  {
    "id": 1,
    "name": "Família Silva",
    "users": [
      {
        "id": 1,
        "nome": "João Silva",
        "cpf": "12345678901",
        "email": "joao@email.com",
        "createdAt": "2025-07-10",
        "accounts": [...]
      },
      {
        "id": 2,
        "nome": "Maria Santos",
        "cpf": "98765432100",
        "email": "maria@email.com",
        "createdAt": "2025-07-10",
        "accounts": [...]
      }
    ]
  }
]
```

### 4. **GET /api/accounts** - Listar todas as contas
```bash
curl -X GET http://localhost:8080/api/accounts
```

### 5. **GET /api/accounts/user/{userId}** - Contas de um usuário
```bash
curl -X GET http://localhost:8080/api/accounts/user/1
```

### 6. **GET /api/transactions** - Listar todas as transações
```bash
curl -X GET http://localhost:8080/api/transactions
```

### 7. **GET /api/reports/balance/user/{userId}** - Saldo total do usuário
```bash
curl -X GET http://localhost:8080/api/reports/balance/user/1
```

**Resposta esperada:**
```json
{
  "userId": 1,
  "totalBalance": 12670.00,
  "numberOfAccounts": 2,
  "accounts": [...]
}
```

### 8. **GET /api/reports/summary/user/{userId}** - Resumo financeiro
```bash
curl -X GET http://localhost:8080/api/reports/summary/user/1
```

## Vantagens das Melhorias

### **Relacionamento Bidirecional User ↔ Account**
1. **Facilita consultas**: Agora você pode acessar todas as contas de um usuário diretamente
2. **Melhora performance**: Reduz consultas desnecessárias ao banco
3. **Flexibilidade**: Permite criar relatórios mais ricos e detalhados

### **Resolução da Referência Circular**
1. **JSON limpo**: Não há mais loops infinitos na serialização
2. **Performance**: Reduz o tamanho das respostas da API
3. **Usabilidade**: APIs mais fáceis de consumir por frontends

### **Endpoints Especializados**
1. **`/users/{id}/with-accounts`**: Para quando você precisa do usuário com todas as contas carregadas
2. **`/accounts/user/{userId}`**: Para quando você só precisa das contas de um usuário
3. **Relatórios**: Endpoints especializados para análises financeiras

## Casos de Uso Práticos

### **Dashboard do Usuário**
```bash
# Buscar usuário com suas contas
GET /api/users/1/with-accounts

# Buscar resumo financeiro
GET /api/reports/summary/user/1
```

### **Gestão de Contas**
```bash
# Listar contas do usuário
GET /api/accounts/user/1

# Criar nova conta
POST /api/accounts
{
  "name": "Nova Conta",
  "description": "Descrição da conta",
  "balance": 1000.00,
  "user": {"id": 1}
}
```

### **Controle Familiar**
```bash
# Ver família com todos os membros e suas contas
GET /api/families/1

# Adicionar novo membro à família
POST /api/families/1/users/3
```

## Testes Recomendados

1. ✅ Testar GET /api/users - Verificar se não há mais loop infinito
2. ✅ Testar GET /api/users/1/with-accounts - Verificar se as contas aparecem
3. ✅ Testar criação de nova conta e verificar se aparece no usuário
4. ✅ Testar relatórios financeiros
5. ✅ Testar gestão de famílias

A API agora está muito mais robusta e pronta para uso em produção!
