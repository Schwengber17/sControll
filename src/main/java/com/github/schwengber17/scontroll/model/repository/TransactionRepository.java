package com.github.schwengber17.scontroll.model.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.schwengber17.scontroll.model.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    
    List<Transaction> findByAccountIdOrderByDateDesc(Integer accountId);
    
    List<Transaction> findByCategoryOrderByDateDesc(String category);
    
    List<Transaction> findByDateBetweenOrderByDateDesc(LocalDate startDate, LocalDate endDate);
    
    List<Transaction> findByAccountIdAndDateBetweenOrderByDateDesc(Integer accountId, LocalDate startDate, LocalDate endDate);
}
