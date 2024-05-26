package org.saad.ebankingbackend.services;

import jakarta.transaction.Transactional;
import org.saad.ebankingbackend.entities.BankAccount;
import org.saad.ebankingbackend.entities.CurrentAccount;
import org.saad.ebankingbackend.entities.SavingAccount;
import org.saad.ebankingbackend.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BankService {
    @Autowired
    private AccountRepository accountRepository;
    public void consulter(){
        BankAccount bankAccount=
                accountRepository.findById("6c708e09-9ec2-4f79-aa6d-4bc3878f126f").orElse(null);
        if(bankAccount!=null) {
            System.out.println("*****************************");
            System.out.println(bankAccount.getId());
            System.out.println(bankAccount.getBalance());
            System.out.println(bankAccount.getStatus());
            System.out.println(bankAccount.getCreatedAt());
            System.out.println(bankAccount.getCustomer().getName());
            System.out.println(bankAccount.getClass().getSimpleName());
            if (bankAccount instanceof CurrentAccount) {
                System.out.println("Over Draft=>" + ((CurrentAccount) bankAccount).getOverDraft());
            } else if (bankAccount instanceof SavingAccount) {
                System.out.println("Rate=>" + ((SavingAccount) bankAccount).getInterestRate());
            }
            bankAccount.getAccountOperationList().forEach(op -> {
                System.out.println(op.getType() + "\t" + op.getOperationdate() + "\t" + op.getAmount());
            });
        }
    }
}
