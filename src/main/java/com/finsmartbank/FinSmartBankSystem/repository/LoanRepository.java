package com.finsmartbank.FinSmartBankSystem.repository;

import com.finsmartbank.FinSmartBankSystem.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}
