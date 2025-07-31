package com.github.schwengber17.scontroll.rest;

import java.math.BigDecimal;
import java.time.LocalDate;
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
import com.github.schwengber17.scontroll.model.entity.Transaction;
import com.github.schwengber17.scontroll.model.enums.CategoryEnum;
import com.github.schwengber17.scontroll.model.repository.AccountRepository;
import com.github.schwengber17.scontroll.model.repository.TransactionRepository;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionController(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @PostMapping
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public Transaction salvar(@Valid @RequestBody Transaction transaction) {
        // Se a data não foi informada, usar a data atual
        if (transaction.getDate() == null) {
            transaction.setDate(LocalDate.now());
        }
        
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        // Atualizar o saldo da conta
        Account account = transaction.getAccount();
        BigDecimal newBalance = account.getBalance().add(transaction.getAmount());
        account.setBalance(newBalance);
        accountRepository.save(account);
        
        return savedTransaction;
    }

    @GetMapping
    public List<Transaction> listarTodas() {
        return transactionRepository.findAll();
    }

    @GetMapping("{id}")
    public Transaction acharPorId(@PathVariable Integer id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Transaction not found"));
    }

    @GetMapping("/account/{accountId}")
    public List<Transaction> listarPorConta(@PathVariable Integer accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    @GetMapping("/category/{category}")
    public List<Transaction> listarPorCategoria(@PathVariable CategoryEnum category) {
        return transactionRepository.findByCategory(category);
    }

    @PutMapping("{id}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void atualizar(@PathVariable Integer id, @Valid @RequestBody Transaction transactionUpdated) {
        transactionRepository.findById(id)
                .map(transaction -> {
                    // Reverter o valor anterior do saldo
                    Account account = transaction.getAccount();
                    BigDecimal currentBalance = account.getBalance();
                    BigDecimal balanceAfterRevert = currentBalance.subtract(transaction.getAmount());
                    
                    // Atualizar a transação
                    transaction.setDescription(transactionUpdated.getDescription());
                    transaction.setAmount(transactionUpdated.getAmount());
                    transaction.setDate(transactionUpdated.getDate());
                    transaction.setCategory(transactionUpdated.getCategory());
                    
                    // Aplicar o novo valor ao saldo
                    BigDecimal newBalance = balanceAfterRevert.add(transaction.getAmount());
                    account.setBalance(newBalance);
                    accountRepository.save(account);
                    
                    return transactionRepository.save(transaction);
                })
                .orElseThrow(() -> new ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Transaction not found"));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        transactionRepository.findById(id)
                .map(transaction -> {
                    // Reverter o valor da transação do saldo da conta
                    Account account = transaction.getAccount();
                    BigDecimal newBalance = account.getBalance().subtract(transaction.getAmount());
                    account.setBalance(newBalance);
                    accountRepository.save(account);
                    
                    transactionRepository.delete(transaction);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Transaction not found"));
    }
}
