package com.finsmartbank.FinSmartBankSystem.repository;

import com.finsmartbank.FinSmartBankSystem.model.Transaction;
import com.finsmartbank.FinSmartBankSystem.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // ✅ Add this method
    List<Transaction> findByAccount(Account account);
}
