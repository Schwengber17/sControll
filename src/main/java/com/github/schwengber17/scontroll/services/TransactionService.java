package com.github.schwengber17.scontroll.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.github.schwengber17.scontroll.exception.ResourceNotFoundException;
import com.github.schwengber17.scontroll.model.entity.Account;
import com.github.schwengber17.scontroll.model.entity.Transaction;
import com.github.schwengber17.scontroll.model.enums.CategoryEnum;
import com.github.schwengber17.scontroll.model.repository.TransactionRepository;

@Service
public class TransactionService {
    
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    
    public TransactionService(TransactionRepository transactionRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }
    
    public Transaction createTransaction(Transaction transaction) {
        // Se a data não foi informada, usar a data atual
        if (transaction.getDate() == null) {
            transaction.setDate(LocalDate.now());
        }
        
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        // Atualizar o saldo da conta
        Account account = transaction.getAccount();
        BigDecimal newBalance = account.getBalance().add(transaction.getAmount());
        accountService.updateAccountBalance(account.getId(), newBalance);
        
        return savedTransaction;
    }
    
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
    
    public Transaction getTransactionById(Integer id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", id));
    }
    
    public List<Transaction> getTransactionsByAccountId(Integer accountId) {
        // Verificar se a conta existe
        accountService.getAccountById(accountId);
        return transactionRepository.findByAccountId(accountId);
    }
    
    public List<Transaction> getTransactionsByCategory(CategoryEnum category) {
        return transactionRepository.findByCategory(category);
    }
    
    public void updateTransaction(Integer id, Transaction transactionUpdated) {
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
                    accountService.updateAccountBalance(account.getId(), newBalance);
                    
                    return transactionRepository.save(transaction);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", id));
    }
    
    public void deleteTransaction(Integer id) {
        Transaction transaction = getTransactionById(id);
        
        // Reverter o valor da transação do saldo da conta
        Account account = transaction.getAccount();
        BigDecimal newBalance = account.getBalance().subtract(transaction.getAmount());
        accountService.updateAccountBalance(account.getId(), newBalance);
        
        transactionRepository.delete(transaction);
    }
    
    public boolean transactionExists(Integer id) {
        return transactionRepository.existsById(id);
    }
}
