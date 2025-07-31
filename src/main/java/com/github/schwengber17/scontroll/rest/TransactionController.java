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
import jakarta.validation.Valid;

import com.github.schwengber17.scontroll.model.entity.Transaction;
import com.github.schwengber17.scontroll.model.enums.CategoryEnum;
import com.github.schwengber17.scontroll.services.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public Transaction salvar(@Valid @RequestBody Transaction transaction) {
        return transactionService.createTransaction(transaction);
    }

    @GetMapping
    public List<Transaction> listarTodas() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("{id}")
    public Transaction acharPorId(@PathVariable Integer id) {
        return transactionService.getTransactionById(id);
    }

    @GetMapping("/account/{accountId}")
    public List<Transaction> listarPorConta(@PathVariable Integer accountId) {
        return transactionService.getTransactionsByAccountId(accountId);
    }

    @GetMapping("/category/{category}")
    public List<Transaction> listarPorCategoria(@PathVariable CategoryEnum category) {
        return transactionService.getTransactionsByCategory(category);
    }

    @PutMapping("{id}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void atualizar(@PathVariable Integer id, @Valid @RequestBody Transaction transactionUpdated) {
        transactionService.updateTransaction(id, transactionUpdated);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        transactionService.deleteTransaction(id);
    }
}
