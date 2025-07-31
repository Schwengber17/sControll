package com.github.schwengber17.scontroll.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.github.schwengber17.scontroll.dto.UserDTO;
import com.github.schwengber17.scontroll.model.entity.User;
import com.github.schwengber17.scontroll.model.entity.Account;
import com.github.schwengber17.scontroll.services.UserService;
import com.github.schwengber17.scontroll.services.AccountService;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;
    private final AccountService accountService;
    
    public UserController(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @PostMapping
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public User salvar(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping
    public List<User> listarTodos() {
        return userService.getAllUsers();
    }

    @GetMapping("{id}")
    public UserDTO acharPorId(@PathVariable Integer id) {
        return userService.getUserDTOById(id);
    }
    
    @DeleteMapping("{id}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        userService.deleteUser(id);
    }

    @PutMapping("{id}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void atualizar(@PathVariable Integer id, @Valid @RequestBody User userUpdated) {
        userService.updateUser(id, userUpdated);
    }
    
    // DTO simples para criação de conta (sem precisar incluir userId)
    public static class CreateAccountRequest {
        @NotBlank(message = "Nome da conta é obrigatório")
        @Size(min = 2, max = 100, message = "Nome da conta deve ter entre 2 e 100 caracteres")
        public String name;
        
        @Size(max = 255, message = "Descrição deve ter no máximo 255 caracteres")
        public String description;
        
        @NotNull(message = "Saldo é obrigatório")
        @DecimalMin(value = "0.0", inclusive = true, message = "Saldo não pode ser negativo")
        public java.math.BigDecimal balance;
    }

    // POST /api/users/{id}/accounts - Criar conta para um usuário específico
    @PostMapping("{id}/accounts")
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public Account criarConta(@PathVariable Integer id, @Valid @RequestBody CreateAccountRequest request) {
        return accountService.createAccountForUser(id, request.name, request.description, request.balance);
    }

    // GET /api/users/{id}/accounts - Listar contas de um usuário
    @GetMapping("{id}/accounts")
    public List<Account> listarContas(@PathVariable Integer id) {
        return accountService.getAccountsByUserId(id);
    }
}
