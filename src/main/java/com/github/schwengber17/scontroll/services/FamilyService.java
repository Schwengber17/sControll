package com.github.schwengber17.scontroll.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.schwengber17.scontroll.exception.ResourceNotFoundException;
import com.github.schwengber17.scontroll.model.entity.Family;
import com.github.schwengber17.scontroll.model.entity.User;
import com.github.schwengber17.scontroll.model.repository.FamilyRepository;

@Service
public class FamilyService {
    
    private final FamilyRepository familyRepository;
    private final UserService userService;
    
    public FamilyService(FamilyRepository familyRepository, UserService userService) {
        this.familyRepository = familyRepository;
        this.userService = userService;
    }
    
    public Family createFamily(Family family) {
        return familyRepository.save(family);
    }
    
    public List<Family> getAllFamilies() {
        return familyRepository.findAll();
    }
    
    public Family getFamilyById(Integer id) {
        return familyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Family", "id", id));
    }
    
    public void updateFamily(Integer id, Family familyUpdated) {
        familyRepository.findById(id)
                .map(family -> {
                    family.setName(familyUpdated.getName());
                    family.setUsers(familyUpdated.getUsers());
                    return familyRepository.save(family);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Family", "id", id));
    }
    
    public void deleteFamily(Integer id) {
        Family family = getFamilyById(id);
        familyRepository.delete(family);
    }
    
    public void addUserToFamily(Integer familyId, Integer userId) {
        Family family = getFamilyById(familyId);
        User user = userService.getUserById(userId);

        if (!family.getUsers().contains(user)) {
            family.getUsers().add(user);
            familyRepository.save(family);
        }
    }
    
    public void removeUserFromFamily(Integer familyId, Integer userId) {
        Family family = getFamilyById(familyId);
        User user = userService.getUserById(userId);

        family.getUsers().remove(user);
        familyRepository.save(family);
    }
    
    public boolean familyExists(Integer id) {
        return familyRepository.existsById(id);
    }
}
