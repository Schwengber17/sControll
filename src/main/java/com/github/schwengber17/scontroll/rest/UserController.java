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

import com.github.schwengber17.scontroll.model.entity.User;
import com.github.schwengber17.scontroll.model.entity.Family;
import com.github.schwengber17.scontroll.model.entity.Account;
import com.github.schwengber17.scontroll.model.repository.UserRepository;
import com.github.schwengber17.scontroll.model.repository.AccountRepository;
import com.github.schwengber17.scontroll.exception.ResourceNotFoundException;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    
    public UserController(UserRepository userRepository, AccountRepository accountRepository) {
		this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    @PostMapping
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public User salvar(@Valid @RequestBody User user) {
        // Garantir que accounts não seja processado na criação via UserController
        user.setAccounts(null);
        return userRepository.save(user);
    }

    @GetMapping
    public List<User> listarTodos() {
        return userRepository.findAll();
    }

    @GetMapping("{id}")
    public User acharPorId(@PathVariable Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }
    
    // @GetMapping("{id}/with-accounts")
    // public User acharPorIdComContas(@PathVariable Integer id) {
    //     User user = userRepository.findById(id)
    //             .orElseThrow(() -> new ResponseStatusException(
    //                     org.springframework.http.HttpStatus.NOT_FOUND, "User not found"));
        
    //     // Força o carregamento das contas para este endpoint específico
    //     // O @JsonIgnore na entidade Account.user evita referência circular
    //     user.getAccounts().size(); // Força o carregamento lazy
    //     return user;
    // }
    
    @DeleteMapping("{id}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        // Remove o usuário de todas as famílias antes de deletar
        if (user.getFamilies() != null) {
            for (Family family : user.getFamilies()) {
                family.getUsers().remove(user);
            }
            user.getFamilies().clear();
        }
        
        // As contas e transações serão deletadas em cascata devido à configuração
        // cascade = CascadeType.ALL na relação @OneToMany
        userRepository.delete(user);
    }

    @PutMapping("{id}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void atualizar(@PathVariable Integer id, @Valid @RequestBody User userUpdated) {
        userRepository.findById(id)
                .map( user -> {
                    // ATENÇÃO: Apenas campos básicos do usuário são atualizados
                    // Contas (accounts) NÃO podem ser editadas via UserController
                    // Use AccountController para gerenciar contas
                    user.setNome(userUpdated.getNome());
                    user.setEmail(userUpdated.getEmail());
                    user.setCpf(userUpdated.getCpf());
                    
                    // Explicitamente ignora qualquer alteração em accounts
                    // mesmo que venha no JSON de entrada
                    // accounts deve ser gerenciado apenas pelo AccountController
                    
                    return userRepository.save(user);
                }
                        )
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
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
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        Account account = Account.builder()
                .name(request.name)
                .description(request.description)
                .balance(request.balance)
                .user(user)
                .build();
        
        return accountRepository.save(account);
    }

    // GET /api/users/{id}/accounts - Listar contas de um usuário
    @GetMapping("{id}/accounts")
    public List<Account> listarContas(@PathVariable Integer id) {
        // Verificar se o usuário existe
        userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        return accountRepository.findByUserId(id);
    }
}
