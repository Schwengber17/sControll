package com.github.schwengber17.scontroll.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyBalance {
    private BigDecimal balance; //saldo no final do dia
    private BigDecimal dailyMovement;
    private LocalDate date; //data do dia
    private Integer transactionsCount; //quantidade de transações no dia
}
