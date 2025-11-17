package com.finsmartbank.FinSmartBankSystem.controller;

import com.finsmartbank.FinSmartBankSystem.model.Account;
import com.finsmartbank.FinSmartBankSystem.model.Transaction;
import com.finsmartbank.FinSmartBankSystem.repository.AccountRepository;
import com.finsmartbank.FinSmartBankSystem.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
@CrossOrigin(origins = "http://localhost:5500") // adjust if using different frontend port
public class FundTransferController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @PostMapping("/transfer")
    public Map<String, Object> transferFunds(
            @RequestParam String fromAccountNumber,
            @RequestParam String toAccountNumber,
            @RequestParam BigDecimal amount,
            @RequestParam(required = false) String description) {

        Map<String, Object> response = new HashMap<>();

        Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber);
        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber);

        if (fromAccount == null || toAccount == null) {
            response.put("status", "error");
            response.put("message", "Invalid account number(s).");
            return response;
        }

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            response.put("status", "error");
            response.put("message", "Insufficient funds.");
            return response;
        }

        // Perform transfer
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // Log debit transaction
        Transaction debitTxn = new Transaction();
        debitTxn.setAccount(fromAccount);
        debitTxn.setTransactionType("debit");
        debitTxn.setAmount(amount);
        debitTxn.setDescription("Transfer to " + toAccountNumber);
        transactionRepository.save(debitTxn);

        // Log credit transaction
        Transaction creditTxn = new Transaction();
        creditTxn.setAccount(toAccount);
        creditTxn.setTransactionType("credit");
        creditTxn.setAmount(amount);
        creditTxn.setDescription("Received from " + fromAccountNumber);
        transactionRepository.save(creditTxn);

        response.put("status", "success");
        response.put("message", "Transfer successful");
        response.put("fromBalance", fromAccount.getBalance());

        return response;
    }
}
