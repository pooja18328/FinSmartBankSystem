package com.finsmartbank.FinSmartBankSystem.controller;

import com.finsmartbank.FinSmartBankSystem.model.Account;
import com.finsmartbank.FinSmartBankSystem.model.User;
import com.finsmartbank.FinSmartBankSystem.repository.AccountRepository;
import com.finsmartbank.FinSmartBankSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/services")
    public String services() {
        return "services";
    }

    @GetMapping("/open-account")
    public String openAccount() {
        return "openAccount";
    }

    @GetMapping("/fund-transfer")
    public String fundTransfer() {
        return "fundtransfer";
    }

    @GetMapping("/transaction-history")
    public String transactionHistory() {
        return "transactionHistory";
    }

    @GetMapping("/loan-calculator")
    public String loanCalculator() {
        return "loanCalculator";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/chatbot")
    public String chatbot() {
        return "chatbot";
    }

    @GetMapping("/admin-dashboard")
    public String adminDashboardPage() {
        return "admin-dashboard";
    }

    // ✅ View all users with role = "customer"
    @GetMapping("/view-users")
    public String viewUsersPage(Model model) {
        List<User> customers = userRepository.findByRole("customer");
        model.addAttribute("customers", customers);
        return "view-users";
    }

    // ✅ View all accounts
    @GetMapping("/admin/accounts")
    public String viewAccountsPage(Model model) {
        List<Account> accounts = accountRepository.findAll();
        model.addAttribute("accounts", accounts);
        return "view-accounts";
    }

    // ✅ Delete an account
    @GetMapping("/admin/accounts/delete/{id}")
    public String deleteAccount(@PathVariable Long id) {
        accountRepository.deleteById(id);
        return "redirect:/admin/accounts";
    }
 // Inside your HomeController class
    @GetMapping("/admin/fund-transfers")
    public String fundTransfersPage(Model model) {
        // Optional: fetch transfer data from your service/repository
        // Example:
        // List<Transfer> transfers = transferService.getAllTransfers();
        // model.addAttribute("transfers", transfers);

        return "fund-transfers"; // points to templates/admin/fund-transfers.html
    }

}
