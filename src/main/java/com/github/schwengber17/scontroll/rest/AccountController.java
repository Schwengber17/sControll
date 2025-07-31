package com.github.schwengber17.scontroll.rest;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import com.github.schwengber17.scontroll.dto.AccountDTO;
import com.github.schwengber17.scontroll.model.entity.Account;
import com.github.schwengber17.scontroll.services.AccountService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public List<Account> listarTodas() {
        return accountService.getAllAccounts();
    }

    @GetMapping("{id}")
    public AccountDTO acharPorId(@PathVariable Integer id) {
        return accountService.getAccountDTOById(id);
    }

    @GetMapping("/user/{userId}")
    public List<Account> listarPorUsuario(@PathVariable Integer userId) {
        return accountService.getAccountsByUserId(userId);
    }

    @PutMapping("{id}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void atualizar(@PathVariable Integer id, @Valid @RequestBody Account accountUpdated) {
        accountService.updateAccount(id, accountUpdated);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        accountService.deleteAccount(id);
    }
}
