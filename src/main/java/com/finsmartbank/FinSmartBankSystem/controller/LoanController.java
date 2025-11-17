package com.finsmartbank.FinSmartBankSystem.controller;

import com.finsmartbank.FinSmartBankSystem.model.Loan;
import com.finsmartbank.FinSmartBankSystem.model.User;
import com.finsmartbank.FinSmartBankSystem.repository.LoanRepository;
import com.finsmartbank.FinSmartBankSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Controller
@RequestMapping("/loan")
public class LoanController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoanRepository loanRepository;

    // -----------------------------
    // CUSTOMER APPLY LOAN
    // -----------------------------
    @GetMapping("/apply")
    public String showLoanForm() {
        return "loan-form";
    }

    @PostMapping("/apply")
    public String applyLoan(@RequestParam String email,
                            @RequestParam BigDecimal loanAmount,
                            @RequestParam double interestRate,
                            @RequestParam int tenureMonths,
                            Model model) {
        try {
            User user = userRepository.findByEmail(email);
            if (user == null) {
                model.addAttribute("message", "❌ User not found! Please register first.");
                return "loan-form";
            }

            // EMI calculation
            double monthlyRate = interestRate / 12 / 100;
            double emiValue = (loanAmount.doubleValue() * monthlyRate * Math.pow(1 + monthlyRate, tenureMonths))
                    / (Math.pow(1 + monthlyRate, tenureMonths) - 1);

            BigDecimal emi = BigDecimal.valueOf(emiValue).setScale(2, RoundingMode.HALF_UP);
            BigDecimal totalPayment = emi.multiply(BigDecimal.valueOf(tenureMonths));

            Loan loan = new Loan();
            loan.setUser(user);
            loan.setLoanAmount(loanAmount);
            loan.setInterestRate(interestRate);
            loan.setTenureMonths(tenureMonths);
            loan.setEmi(emi);
            loan.setTotalPayment(totalPayment);
            loan.setStatus("Pending");   // DEFAULT

            loanRepository.save(loan);

            model.addAttribute("message",
                    "✅ Loan Request Submitted! Status: PENDING | EMI: ₹" + emi + ", Total: ₹" + totalPayment);

        } catch (Exception e) {
            model.addAttribute("message", "❌ Error: " + e.getMessage());
        }

        return "loan-form";
    }

    // -----------------------------
    // ADMIN VIEW LOANS
    // -----------------------------
    @GetMapping("/admin/manage")
    public String viewAllLoans(Model model) {
        List<Loan> loans = loanRepository.findAll();
        model.addAttribute("loans", loans);
        return "admin-loan-management";
    }

    // -----------------------------
    // ADMIN APPROVE
    // -----------------------------
    @PostMapping("/admin/approve/{id}")
    public String approveLoan(@PathVariable Long id) {
        Loan loan = loanRepository.findById(id).orElse(null);
        if (loan != null) {
            loan.setStatus("Approved");
            loanRepository.save(loan);
        }
        return "redirect:/loan/admin/manage";
    }

    // -----------------------------
    // ADMIN REJECT
    // -----------------------------
    @PostMapping("/admin/reject/{id}")
    public String rejectLoan(@PathVariable Long id) {
        Loan loan = loanRepository.findById(id).orElse(null);
        if (loan != null) {
            loan.setStatus("Rejected");
            loanRepository.save(loan);
        }
        return "redirect:/loan/admin/manage";
    }
}
