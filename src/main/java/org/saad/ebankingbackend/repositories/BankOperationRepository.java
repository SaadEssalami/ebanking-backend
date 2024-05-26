package org.saad.ebankingbackend.repositories;

import org.saad.ebankingbackend.entities.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankOperationRepository extends JpaRepository<Operation,Long> {
    List<Operation> findByBankAccount_Id(String accountId);

    Page<Operation> findByBankAccount_Id(String accountId, Pageable pageable);
}
