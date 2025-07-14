package com.github.schwengber17.scontroll;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.github.schwengber17.scontroll.model.entity.Account;
import com.github.schwengber17.scontroll.model.entity.Family;
import com.github.schwengber17.scontroll.model.entity.Transaction;
import com.github.schwengber17.scontroll.model.entity.User;
import com.github.schwengber17.scontroll.model.repository.AccountRepository;
import com.github.schwengber17.scontroll.model.repository.FamilyRepository;
import com.github.schwengber17.scontroll.model.repository.TransactionRepository;
import com.github.schwengber17.scontroll.model.repository.UserRepository;

@SpringBootApplication
public class ScontrollApplication {

	@Bean
	public CommandLineRunner run(@Autowired UserRepository userRepository,
								 @Autowired FamilyRepository familyRepository,
								 @Autowired AccountRepository accountRepository,
								 @Autowired TransactionRepository transactionRepository) {
		return args -> {
			// Criar usuários
			User user1 = User.builder()
					.nome("João Silva")
					.email("joao@email.com")
					.cpf("12345678901")
					.build();
			
			User user2 = User.builder()
					.nome("Maria Santos")
					.email("maria@email.com")
					.cpf("98765432100")
					.build();
			
			userRepository.saveAll(Arrays.asList(user1, user2));
			
			// Criar família
			Family family = Family.builder()
					.name("Família Silva")
					.users(Arrays.asList(user1, user2))
					.build();
			
			familyRepository.save(family);
			
			// Criar contas
			Account contaJoao = Account.builder()
					.name("Conta Corrente João")
					.description("Conta principal do João")
					.balance(new BigDecimal("5000.00"))
					.user(user1)
					.build();
			
			Account poupancaJoao = Account.builder()
					.name("Poupança João")
					.description("Conta poupança do João")
					.balance(new BigDecimal("10000.00"))
					.user(user1)
					.build();
			
			Account contaMaria = Account.builder()
					.name("Conta Corrente Maria")
					.description("Conta principal da Maria")
					.balance(new BigDecimal("3000.00"))
					.user(user2)
					.build();
			
			accountRepository.saveAll(Arrays.asList(contaJoao, poupancaJoao, contaMaria));
			
			// Criar algumas transações de exemplo
			Transaction receita1 = Transaction.builder()
					.description("Salário João")
					.amount(new BigDecimal("3500.00"))
					.account(contaJoao)
					.date(LocalDate.now().minusDays(5))
					.category("Salário")
					.build();
			
			Transaction gasto1 = Transaction.builder()
					.description("Supermercado")
					.amount(new BigDecimal("-450.00"))
					.account(contaJoao)
					.date(LocalDate.now().minusDays(3))
					.category("Alimentação")
					.build();
			
			Transaction receita2 = Transaction.builder()
					.description("Salário Maria")
					.amount(new BigDecimal("2800.00"))
					.account(contaMaria)
					.date(LocalDate.now().minusDays(4))
					.category("Salário")
					.build();
			
			Transaction gasto2 = Transaction.builder()
					.description("Combustível")
					.amount(new BigDecimal("-200.00"))
					.account(contaMaria)
					.date(LocalDate.now().minusDays(2))
					.category("Transporte")
					.build();
			
			Transaction gasto3 = Transaction.builder()
					.description("Energia Elétrica")
					.amount(new BigDecimal("-180.00"))
					.account(contaJoao)
					.date(LocalDate.now().minusDays(1))
					.category("Utilidades")
					.build();
			
			transactionRepository.saveAll(Arrays.asList(receita1, gasto1, receita2, gasto2, gasto3));
			
			System.out.println("=== Dados de exemplo criados com sucesso! ===");
			System.out.println("Usuários: " + userRepository.count());
			System.out.println("Famílias: " + familyRepository.count());
			System.out.println("Contas: " + accountRepository.count());
			System.out.println("Transações: " + transactionRepository.count());
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(ScontrollApplication.class, args);
	}
}
