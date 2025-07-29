package com.github.schwengber17.scontroll.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Integer id; // ID do usuário
    private String name; // Nome do usuário
    private String email; // Email do usuário
    private BigDecimal personalBalance; // Saldo do usuário
    private BigDecimal monthlyExpenses;
    private BigDecimal monthlyIncome;
    private BigDecimal monthlyDifference;

    private Integer accountsCount;
    private Integer totalTransactions;

}
