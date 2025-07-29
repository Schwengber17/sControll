package com.github.schwengber17.scontroll.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
//Pizza
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryExpense {
    private String categoryName;
    private BigDecimal amount;
    private Integer transactionCount;
    private Double percentage;
}
