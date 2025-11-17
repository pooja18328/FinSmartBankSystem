package com.finsmartbank.FinSmartBankSystem.controller;

import com.finsmartbank.FinSmartBankSystem.model.Loan;
import com.finsmartbank.FinSmartBankSystem.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/loans")
public class AdminLoanController {

    @Autowired
    private LoanRepository loanRepository;

    // SHOW ALL LOANS
    @GetMapping("")
    public String viewLoans(Model model) {
        model.addAttribute("loans", loanRepository.findAll());
        return "admin-manage-loans";
    }

    // APPROVE LOAN
    @GetMapping("/approve/{id}")
    public String approveLoan(@PathVariable Long id) {
        Loan loan = loanRepository.findById(id).orElse(null);
        if (loan != null) {
            loan.setStatus("Approved");
            loanRepository.save(loan);
        }
        return "redirect:/admin/loans";
    }

    // REJECT LOAN
    @GetMapping("/reject/{id}")
    public String rejectLoan(@PathVariable Long id) {
        Loan loan = loanRepository.findById(id).orElse(null);
        if (loan != null) {
            loan.setStatus("Rejected");
            loanRepository.save(loan);
        }
        return "redirect:/admin/loans";
    }
}
