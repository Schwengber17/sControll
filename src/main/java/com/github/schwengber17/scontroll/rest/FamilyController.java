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
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;

import com.github.schwengber17.scontroll.model.entity.Family;
import com.github.schwengber17.scontroll.model.entity.User;
import com.github.schwengber17.scontroll.model.repository.FamilyRepository;
import com.github.schwengber17.scontroll.model.repository.UserRepository;

@RestController
@RequestMapping("/api/families")
public class FamilyController {
    
    private final FamilyRepository familyRepository;
    private final UserRepository userRepository;

    public FamilyController(FamilyRepository familyRepository, UserRepository userRepository) {
        this.familyRepository = familyRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public Family salvar(@Valid @RequestBody Family family) {
        return familyRepository.save(family);
    }

    @GetMapping
    public List<Family> listarTodas() {
        return familyRepository.findAll();
    }

    @GetMapping("{id}")
    public Family acharPorId(@PathVariable Integer id) {
        return familyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Family not found"));
    }

    @PutMapping("{id}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void atualizar(@PathVariable Integer id, @Valid @RequestBody Family familyUpdated) {
        familyRepository.findById(id)
                .map(family -> {
                    family.setName(familyUpdated.getName());
                    family.setUsers(familyUpdated.getUsers());
                    return familyRepository.save(family);
                })
                .orElseThrow(() -> new ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Family not found"));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        familyRepository.findById(id)
                .map(family -> {
                    familyRepository.delete(family);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Family not found"));
    }

    @PostMapping("{familyId}/users/{userId}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void adicionarUsuario(@PathVariable Integer familyId, @PathVariable Integer userId) {
        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Family not found"));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "User not found"));

        if (!family.getUsers().contains(user)) {
            family.getUsers().add(user);
            familyRepository.save(family);
        }
    }

    @DeleteMapping("{familyId}/users/{userId}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void removerUsuario(@PathVariable Integer familyId, @PathVariable Integer userId) {
        Family family = familyRepository.findById(familyId)
                .orElseThrow(() -> new ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Family not found"));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "User not found"));

        family.getUsers().remove(user);
        familyRepository.save(family);
    }
}
