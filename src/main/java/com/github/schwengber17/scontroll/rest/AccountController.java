package com.github.schwengber17.scontroll.rest;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;

import com.github.schwengber17.scontroll.model.entity.Account;
import com.github.schwengber17.scontroll.model.repository.AccountRepository;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    
    private final AccountRepository accountRepository;

    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // @PostMapping
    // @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    // public Account salvar(@RequestBody Account account) {
    //     return accountRepository.save(account);
    // }

    @GetMapping
    public List<Account> listarTodas() {
        return accountRepository.findAll();
    }

    @GetMapping("{id}")
    public Account acharPorId(@PathVariable Integer id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Account not found"));
    }

    @GetMapping("/user/{userId}")
    public List<Account> listarPorUsuario(@PathVariable Integer userId) {
        return accountRepository.findByUserId(userId);
    }

    @PutMapping("{id}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void atualizar(@PathVariable Integer id, @Valid @RequestBody Account accountUpdated) {
        accountRepository.findById(id)
                .map(account -> {
                    account.setName(accountUpdated.getName());
                    account.setDescription(accountUpdated.getDescription());
                    account.setBalance(accountUpdated.getBalance());
                    return accountRepository.save(account);
                })
                .orElseThrow(() -> new ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Account not found"));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        accountRepository.findById(id)
                .map(account -> {
                    accountRepository.delete(account);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Account not found"));
    }
}
