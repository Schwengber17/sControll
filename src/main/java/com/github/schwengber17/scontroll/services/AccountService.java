package com.github.schwengber17.scontroll.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.schwengber17.scontroll.dto.AccountDTO;
import com.github.schwengber17.scontroll.exception.ResourceNotFoundException;
import com.github.schwengber17.scontroll.model.entity.Account;
import com.github.schwengber17.scontroll.model.entity.User;
import com.github.schwengber17.scontroll.model.repository.AccountRepository;

@Service
public class AccountService {
    
    private final AccountRepository accountRepository;
    private final UserService userService;
    
    public AccountService(AccountRepository accountRepository, UserService userService) {
        this.accountRepository = accountRepository;
        this.userService = userService;
    }
    
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }
    
    public Account createAccountForUser(Integer userId, String name, String description, java.math.BigDecimal balance) {
        User user = userService.getUserById(userId);
        
        Account account = Account.builder()
                .name(name)
                .description(description)
                .balance(balance)
                .user(user)
                .build();
        
        return accountRepository.save(account);
    }
    
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
    
    public Account getAccountById(Integer id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", id));
    }
    
    public AccountDTO getAccountDTOById(Integer id) {
        Account account = getAccountById(id);
        return AccountDTO.builder()
                .id(account.getId())
                .name(account.getName())
                .description(account.getDescription())
                .balance(account.getBalance())
                .ownerName(account.getUser().getNome())
                .ownerId(account.getUser().getId())
                .build();
    }
    
    public List<Account> getAccountsByUserId(Integer userId) {
        // Verificar se o usuário existe
        userService.getUserById(userId);
        return accountRepository.findByUserId(userId);
    }
    
    public void updateAccount(Integer id, Account accountUpdated) {
        accountRepository.findById(id)
                .map(account -> {
                    account.setName(accountUpdated.getName());
                    account.setDescription(accountUpdated.getDescription());
                    account.setBalance(accountUpdated.getBalance());
                    return accountRepository.save(account);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", id));
    }
    
    public void deleteAccount(Integer id) {
        Account account = getAccountById(id);
        accountRepository.delete(account);
    }
    
    public void updateAccountBalance(Integer accountId, java.math.BigDecimal newBalance) {
        Account account = getAccountById(accountId);
        account.setBalance(newBalance);
        accountRepository.save(account);
    }
    
    public boolean accountExists(Integer id) {
        return accountRepository.existsById(id);
    }
}
