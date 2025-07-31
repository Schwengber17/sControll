package com.github.schwengber17.scontroll.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.schwengber17.scontroll.dto.FamilyDashboard;
import com.github.schwengber17.scontroll.dto.UserDTO;
import com.github.schwengber17.scontroll.exception.ResourceNotFoundException;
import com.github.schwengber17.scontroll.model.entity.Account;
import com.github.schwengber17.scontroll.model.entity.Family;
import com.github.schwengber17.scontroll.model.entity.Transaction;
import com.github.schwengber17.scontroll.model.entity.User;
import com.github.schwengber17.scontroll.model.enums.CategoryEnum;
import com.github.schwengber17.scontroll.model.repository.AccountRepository;
import com.github.schwengber17.scontroll.model.repository.FamilyRepository;
import com.github.schwengber17.scontroll.model.repository.TransactionRepository;
import com.github.schwengber17.scontroll.model.repository.UserRepository;

@Service
public class DashboardService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private FamilyRepository familyRepository;

    /*
     * FamilyDashboard possui os seguintes atributos
     * private String familyName; // Nome da família
     * private BigDecimal totalBalance; // Saldo total
     * private BigDecimal monthlyIncome; // Receita mensal
     * private BigDecimal monthlyExpenses; // Despesa mensal
     * private BigDecimal monthlyDifference; // Diferença mensal
     * private Integer totalAccounts; // Total de contas
     * private Integer totalMembers; // Total de membros
     * private LocalDate lastUpdate; // Última atualização
     * private List<UserDTO> members; // Membros da família
     */
    public FamilyDashboard getFamilyDashboard(Integer familyId) {
        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new ResourceNotFoundException("Family not found with id: " + familyId));

                
                List<Account> familyAccounts = family.getUsers()
                .stream()
                .flatMap(user -> accountRepository.findByUserId(user.getId()).stream())
                .collect(Collectors.toList());
                
                BigDecimal totalBalance = familyAccounts.stream()
                .map(Account::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
                
                LocalDate startMonth = LocalDate.now().withDayOfMonth(1);
                LocalDate endMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
                
                
                List<Transaction> monthlyTransactions = familyAccounts.stream()
                .flatMap(account -> transactionRepository.findByAccountId(account.getId()).stream())
                .filter(transaction -> {
                    LocalDate transactionDate = transaction.getDate();
                    return !transactionDate.isBefore(startMonth) && !transactionDate.isAfter(endMonth);
                })
                .collect(Collectors.toList());
                
                BigDecimal monthlyIncome = monthlyTransactions.stream()
                .filter(this::isIncome)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
                
                BigDecimal monthlyExpenses = monthlyTransactions.stream()
                .filter(this::isExpense)
                .map(Transaction::getAmount)
                .map(BigDecimal::abs)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
                
                List<UserDTO> members = family.getUsers()
                        .stream()
                        .map(user -> buildUserDTOWithMonthlyData(user, startMonth, endMonth))
                        .collect(Collectors.toList());
                
                return FamilyDashboard.builder()
                .members(members)
                .totalMembers(members.size())
        .familyName(family.getName())
                .lastUpdate(LocalDate.now())
                .monthlyDifference(monthlyIncome.subtract(monthlyExpenses))
                .monthlyExpenses(monthlyExpenses)
                .monthlyIncome(monthlyIncome)
                .totalMembers(family.getUsers().size())
                .totalBalance(totalBalance)
                .totalAccounts(familyAccounts.size())
                .build();
    }

    private boolean isIncome(Transaction transaction){
        CategoryEnum category = transaction.getCategory();
        return category == CategoryEnum.SALARIO || 
               category == CategoryEnum.INVESTIMENTOS || 
               category == CategoryEnum.MESADA;
    }
    private boolean isExpense(Transaction transaction){
        return !isIncome(transaction);
    }

    private UserDTO buildUserDTOWithMonthlyData(User user, LocalDate startMonth, LocalDate endMonth) {
    List<Account> accounts = accountRepository.findByUserId(user.getId());

    BigDecimal personalBalance = accounts.stream()
        .map(Account::getBalance)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    // ✅ CALCULAR transações mensais do USUÁRIO
    List<Transaction> userMonthlyTransactions = accounts.stream()
        .flatMap(account -> transactionRepository.findByAccountId(account.getId()).stream())
        .filter(transaction -> {
            LocalDate transactionDate = transaction.getDate();
            return !transactionDate.isBefore(startMonth) && !transactionDate.isAfter(endMonth);
        })
        .collect(Collectors.toList());

    // ✅ CALCULAR receitas e despesas mensais do USUÁRIO
    BigDecimal userMonthlyIncome = userMonthlyTransactions.stream()
        .filter(this::isIncome)
        .map(Transaction::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal userMonthlyExpenses = userMonthlyTransactions.stream()
        .filter(this::isExpense)
        .map(Transaction::getAmount)
        .map(BigDecimal::abs)  // ✅ Garantir valor positivo
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    
    UserDTO dto = new UserDTO();
    dto.setId(user.getId());
    dto.setName(user.getNome());
    dto.setEmail(user.getEmail());
    dto.setPersonalBalance(personalBalance);
    dto.setAccountsCount(accounts.size());
    dto.setMonthlyIncome(userMonthlyIncome);                                    // ✅ USAR valor calculado
    dto.setMonthlyExpenses(userMonthlyExpenses);                                // ✅ USAR valor calculado
    dto.setMonthlyDifference(userMonthlyIncome.subtract(userMonthlyExpenses));  // ✅ CALCULAR diferença
    dto.setTotalTransactions(userMonthlyTransactions.size());                   // ✅ USAR valor calculado
    
    return dto;
}

    
}
