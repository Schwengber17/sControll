package com.github.schwengber17.scontroll.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.schwengber17.scontroll.model.entity.Family;

public interface FamilyRepository extends JpaRepository<Family, Integer> {

    
} 
