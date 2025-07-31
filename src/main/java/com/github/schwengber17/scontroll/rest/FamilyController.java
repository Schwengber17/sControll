package com.github.schwengber17.scontroll.rest;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import com.github.schwengber17.scontroll.model.entity.Family;
import com.github.schwengber17.scontroll.services.FamilyService;

@RestController
@RequestMapping("/api/families")
public class FamilyController {
    
    private final FamilyService familyService;

    public FamilyController(FamilyService familyService) {
        this.familyService = familyService;
    }

    @PostMapping
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public Family salvar(@Valid @RequestBody Family family) {
        return familyService.createFamily(family);
    }

    @GetMapping
    public List<Family> listarTodas() {
        return familyService.getAllFamilies();
    }

    @GetMapping("{id}")
    public Family acharPorId(@PathVariable Integer id) {
        return familyService.getFamilyById(id);
    }

    @PutMapping("{id}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void atualizar(@PathVariable Integer id, @Valid @RequestBody Family familyUpdated) {
        familyService.updateFamily(id, familyUpdated);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        familyService.deleteFamily(id);
    }

    @PostMapping("{familyId}/users/{userId}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void adicionarUsuario(@PathVariable Integer familyId, @PathVariable Integer userId) {
        familyService.addUserToFamily(familyId, userId);
    }

    @DeleteMapping("{familyId}/users/{userId}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void removerUsuario(@PathVariable Integer familyId, @PathVariable Integer userId) {
        familyService.removeUserFromFamily(familyId, userId);
    }
}
