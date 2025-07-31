package com.github.schwengber17.scontroll.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountEvolution {
    private Integer accountId;
    private String accountName;
    private String ownerName;
    
   // Colocar o balanco na classe diaria private BigDecimal balance;
    private List<DailyBalance> dailyBalances;
}
