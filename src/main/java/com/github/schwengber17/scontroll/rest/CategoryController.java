package com.github.schwengber17.scontroll.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.schwengber17.scontroll.model.repository.TransactionRepository;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final TransactionRepository transactionRepository;

    public CategoryController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @GetMapping
    public List<String> listarCategorias() {
        return transactionRepository.findAll().stream()
                .map(transaction -> transaction.getCategory())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}
