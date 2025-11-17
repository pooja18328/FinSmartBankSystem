package com.finsmartbank.FinSmartBankSystem.controller;

import com.finsmartbank.FinSmartBankSystem.model.Transaction;
import com.finsmartbank.FinSmartBankSystem.model.Account;
import com.finsmartbank.FinSmartBankSystem.repository.TransactionRepository;
import com.finsmartbank.FinSmartBankSystem.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    // ✅ Load transaction history page (when user clicks from Services)
    @GetMapping("/history")
    public String showTransactionHistoryPage() {
        return "transactionHistory"; // Loads transactionHistory.html
    }

    // ✅ Handle search by account number and show result in same page
    @PostMapping("/history")
    public String getTransactionsByAccount(@RequestParam("accountNumber") String accountNumber, Model model) {
        Account account = accountRepository.findByAccountNumber(accountNumber);

        if (account != null) {
            List<Transaction> transactions = transactionRepository.findByAccount(account);
            model.addAttribute("transactions", transactions);
            model.addAttribute("accountNumber", accountNumber);
        } else {
            model.addAttribute("error", "Account not found");
        }

        return "transactionHistory"; // Return same page with data/error message
    }
}
