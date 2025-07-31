package com.github.schwengber17.scontroll.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.schwengber17.scontroll.dto.UserDTO;
import com.github.schwengber17.scontroll.exception.ResourceNotFoundException;
import com.github.schwengber17.scontroll.model.entity.Family;
import com.github.schwengber17.scontroll.model.entity.User;
import com.github.schwengber17.scontroll.model.repository.UserRepository;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public User createUser(User user) {
        // Garantir que accounts não seja processado na criação via UserService
        user.setAccounts(null);
        return userRepository.save(user);
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }
    
    public UserDTO getUserDTOById(Integer id) {
        User user = getUserById(id);
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getNome())
                .email(user.getEmail())
                .build();
    }
    
    public void updateUser(Integer id, User userUpdated) {
        userRepository.findById(id)
                .map(user -> {
                    // Apenas campos básicos do usuário são atualizados
                    // Contas (accounts) NÃO podem ser editadas via UserService
                    user.setNome(userUpdated.getNome());
                    user.setEmail(userUpdated.getEmail());
                    user.setCpf(userUpdated.getCpf());
                    
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }
    
    public void deleteUser(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        // Remove o usuário de todas as famílias antes de deletar
        if (user.getFamilies() != null) {
            for (Family family : user.getFamilies()) {
                family.getUsers().remove(user);
            }
            user.getFamilies().clear();
        }
        
        // As contas e transações serão deletadas em cascata devido à configuração
        // cascade = CascadeType.ALL na relação @OneToMany
        userRepository.delete(user);
    }
    
    public boolean userExists(Integer id) {
        return userRepository.existsById(id);
    }
}
