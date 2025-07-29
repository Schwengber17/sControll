package com.github.schwengber17.scontroll.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.github.schwengber17.scontroll.model.enums.CategoryEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 255, message = "Descrição deve ter no máximo 255 caracteres")
    private String description;

    @NotNull(message = "Valor é obrigatório")
    @Column(nullable = false)
    private BigDecimal amount;

    @NotNull(message = "Conta é obrigatória")
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @PastOrPresent(message = "Data não pode ser futura")
    @Column
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Categoria é obrigatória")
    @Size(max = 50, message = "Categoria deve ter no máximo 50 caracteres")
    @Column(nullable = false)
    private CategoryEnum category;
}
