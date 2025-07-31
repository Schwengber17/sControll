package com.github.schwengber17.scontroll.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.schwengber17.scontroll.model.entity.Transaction;
import com.github.schwengber17.scontroll.model.enums.CategoryEnum;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    
    List<Transaction> findByAccountId(Integer accountId);
    
    List<Transaction> findByCategory(CategoryEnum category);
    
}
