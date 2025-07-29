package com.github.schwengber17.scontroll.rest;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.schwengber17.scontroll.model.enums.CategoryEnum;
@RestController
@RequestMapping("/api/categories")
public class CategoryController {


    @GetMapping
    public CategoryEnum[] listarCategorias() {
        return CategoryEnum.values();
    }
}
