package com.github.schwengber17.scontroll.model.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn; // Still needed for joinColumns within JoinTable
import jakarta.persistence.JoinTable; // New import for JoinTable
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Data
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Family {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Nome da família é obrigatório")
    @Size(min = 2, max = 20, message = "Nome da família deve ter entre 2 e 20 caracteres")
    @Column(nullable = false, length = 20)
    private String name;

    @ManyToMany
    @JoinTable(
        name = "family_users", // Name of the join table (e.g., family_user_junction)
        joinColumns = @JoinColumn(name = "family_id"), // Column in the join table referring to Family's ID
        inverseJoinColumns = @JoinColumn(name = "user_id") // Column in the join table referring to User's ID
    )
    private List<User> users;
}