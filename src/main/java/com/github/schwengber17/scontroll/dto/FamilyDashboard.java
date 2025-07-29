package com.github.schwengber17.scontroll.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
// Resumo de informações da família
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FamilyDashboard {
    private String familyName; // Nome da família
    private BigDecimal totalBalance;      
    private BigDecimal monthlyIncome;       
    private BigDecimal monthlyExpenses;     
    private BigDecimal monthlyDifference;   
    private Integer totalAccounts;     
    private Integer totalMembers;
    private LocalDate lastUpdate;
    private List<UserDTO> members; // Lista de membros da família
}
