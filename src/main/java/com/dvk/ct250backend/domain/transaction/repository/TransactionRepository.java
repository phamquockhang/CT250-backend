package com.dvk.ct250backend.domain.transaction.repository;

import com.dvk.ct250backend.domain.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Integer>, JpaSpecificationExecutor<Transaction> {
    Optional<Transaction> findByTxnRef(String txnRef);
}
