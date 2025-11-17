package com.finsmartbank.FinSmartBankSystem.controller;

import com.finsmartbank.FinSmartBankSystem.model.Account;
import com.finsmartbank.FinSmartBankSystem.model.User;
import com.finsmartbank.FinSmartBankSystem.repository.AccountRepository;
import com.finsmartbank.FinSmartBankSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.UUID;

@Controller
public class AccountController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    // 🟩 Show Open Account Page
    @GetMapping("/accounts/open")
    public String openAccountPage() {
        return "openAccount"; // make sure file name = openAccount.html (case-sensitive)
    }

    // 🟦 Handle form submission
    @PostMapping("/accounts/create")
    public String createAccount(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("initialDeposit") BigDecimal initialDeposit,
            Model model) {

        // ✅ Check if user exists or create one
        User user = userRepository.findByEmail(email);
        if (user == null) {
            user = new User();
            user.setFullName(name);
            user.setEmail(email);
            user.setPassword("default123"); // default password
            user.setRole("USER");
            userRepository.save(user);
        }

        // ✅ Create new account
        Account account = new Account();
        account.setUser(user);
        account.setAccountNumber(UUID.randomUUID().toString().substring(0, 8)); // 8-char unique
        account.setBalance(initialDeposit);
        account.setAccountType("Savings");
        accountRepository.save(account);

        // ✅ Pass data to frontend
        model.addAttribute("message", "Account created successfully!");
        model.addAttribute("accountNumber", account.getAccountNumber());
        model.addAttribute("name", user.getFullName());
        model.addAttribute("deposit", account.getBalance());

        return "openAccount"; // reload page with success box
    }
}
