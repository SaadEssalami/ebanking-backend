package org.saad.ebankingbackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.saad.ebankingbackend.enums.AccountStatus;

import java.util.Date;

@Data @AllArgsConstructor @NoArgsConstructor

public class CurrentBankAccountDTO extends BankAccountDTO{
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus Status;
    private CustomerDto customerDto;
    private double overDraft;
}