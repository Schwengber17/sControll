# Sistema de Tratamento de Erros - SControl API

## Visão Geral

A API SControl implementa um sistema robusto de tratamento de erros que padroniza as respostas de erro e facilita o debugging e a integração com frontends.

## Estrutura da Resposta de Erro

Todas as respostas de erro seguem o padrão:

```json
{
  "status": 400,
  "error": "Dados inválidos",
  "message": "Falha na validação dos dados fornecidos",
  "path": "/api/users",
  "timestamp": "2025-07-10T23:09:20",
  "validationErrors": [
    {
      "field": "nome",
      "message": "Nome é obrigatório",
      "rejectedValue": null
    },
    {
      "field": "email",
      "message": "Email deve ter um formato válido",
      "rejectedValue": "email-invalido"
    }
  ]
}
```

## Tipos de Erro

### 1. Erros de Validação (400 Bad Request)
- **Quando ocorre**: Dados inválidos enviados nas requisições
- **Campos validados**:
  - **User**: nome (2-100 chars), email (formato válido), CPF (11 dígitos)
  - **Account**: name (2-100 chars), balance (≥ 0)
  - **Transaction**: category (obrigatório), amount (obrigatório), date (não futura)
  - **Family**: name (2-20 chars)

**Exemplo de requisição inválida:**
```bash
POST /api/users
{
  "nome": "A",
  "email": "email-invalido",
  "cpf": "123"
}
```

**Resposta:**
```json
{
  "status": 400,
  "error": "Dados inválidos",
  "message": "Falha na validação dos dados fornecidos",
  "validationErrors": [
    {
      "field": "nome",
      "message": "Nome deve ter entre 2 e 100 caracteres",
      "rejectedValue": "A"
    },
    {
      "field": "email", 
      "message": "Email deve ter um formato válido",
      "rejectedValue": "email-invalido"
    },
    {
      "field": "cpf",
      "message": "CPF deve conter exatamente 11 dígitos",
      "rejectedValue": "123"
    }
  ]
}
```

### 2. Recurso Não Encontrado (404 Not Found)
- **Quando ocorre**: Tentativa de acessar/modificar recurso inexistente
- **Endpoints afetados**: GET, PUT, DELETE com IDs inválidos

**Exemplo:**
```bash
GET /api/users/999
```

**Resposta:**
```json
{
  "status": 404,
  "error": "Recurso não encontrado",
  "message": "User não encontrado com id: 999",
  "path": "/api/users/999",
  "timestamp": "2025-07-10T23:09:20"
}
```

### 3. Violação de Integridade (409 Conflict)
- **Quando ocorre**: Violação de restrições únicas ou de integridade
- **Casos comuns**:
  - Email ou CPF duplicados
  - Tentativa de deletar usuário com contas associadas

**Exemplo:**
```bash
POST /api/users
{
  "nome": "João Silva",
  "email": "joao@email.com", // Email já existe
  "cpf": "12345678901"
}
```

**Resposta:**
```json
{
  "status": 409,
  "error": "Violação de integridade",
  "message": "Já existe um registro com esses dados únicos (email, CPF, etc.)",
  "path": "/api/users",
  "timestamp": "2025-07-10T23:09:20"
}
```

### 4. Erros de Regra de Negócio (422 Unprocessable Entity)
- **Quando ocorre**: Dados válidos mas que violam regras de negócio
- **Exemplos**:
  - Saldo insuficiente para transação
  - Operações não permitidas baseadas no estado do recurso

### 5. Parâmetros Inválidos (400 Bad Request)
- **Quando ocorre**: Tipos incorretos em parâmetros de URL
- **Exemplo**: Usar texto onde esperamos número

**Exemplo:**
```bash
GET /api/users/abc
```

**Resposta:**
```json
{
  "status": 400,
  "error": "Parâmetro inválido",
  "message": "Parâmetro 'id' deve ser do tipo Integer",
  "path": "/api/users/abc",
  "timestamp": "2025-07-10T23:09:20"
}
```

### 6. JSON Malformado (400 Bad Request)
- **Quando ocorre**: JSON inválido ou campos incompatíveis

**Exemplo:**
```bash
POST /api/users
{
  "nome": "João",
  "email": "joao@email.com",
  "cpf": 12345678901  // CPF deve ser string
}
```

### 7. Erro Interno do Servidor (500 Internal Server Error)
- **Quando ocorre**: Erros inesperados no servidor
- **Ação**: Logs detalhados são gravados para análise

## Validações Implementadas

### User
- `nome`: Obrigatório, 2-100 caracteres
- `email`: Obrigatório, formato válido, único, máximo 100 caracteres
- `cpf`: Obrigatório, exatamente 11 dígitos, único

### Account
- `name`: Obrigatório, 2-100 caracteres
- `description`: Opcional, máximo 255 caracteres
- `balance`: Obrigatório, maior ou igual a 0

### Transaction
- `description`: Opcional, máximo 255 caracteres
- `amount`: Obrigatório
- `category`: Obrigatório, máximo 50 caracteres
- `date`: Opcional, não pode ser futura
- `account`: Obrigatório

### Family
- `name`: Obrigatório, 2-20 caracteres

## Logs

O sistema gera logs estruturados para facilitar o monitoramento:

- **WARN**: Erros de validação, recursos não encontrados, violações de integridade
- **ERROR**: Erros internos do servidor

## Integração com Frontend

O frontend pode usar a estrutura padronizada para:

1. **Exibir mensagens de erro específicas** usando o campo `message`
2. **Destacar campos inválidos** usando o array `validationErrors`
3. **Implementar retry logic** baseado no status HTTP
4. **Logging** usando o campo `path` e `timestamp`

## Exemplos de Uso

### Validação de Formulário
```javascript
// Frontend pode processar validationErrors
if (response.status === 400 && response.data.validationErrors) {
  response.data.validationErrors.forEach(error => {
    highlightField(error.field, error.message);
  });
}
```

### Tratamento de Recurso Não Encontrado
```javascript
if (response.status === 404) {
  showNotification(response.data.message);
  redirectToList();
}
```

### Tratamento de Conflitos
```javascript
if (response.status === 409) {
  showError("Dados já existem no sistema");
  suggestAlternatives();
}
```
