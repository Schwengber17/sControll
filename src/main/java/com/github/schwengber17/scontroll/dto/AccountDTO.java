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
public class AccountDTO {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal balance;
    private String ownerName;
    private Integer ownerId;
    private Integer transactionsCount;
}
