package com.github.schwengber17.scontroll.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.schwengber17.scontroll.model.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    
    List<Account> findByUserId(Integer userId);
}
