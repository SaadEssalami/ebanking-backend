package org.saad.ebankingbackend.services;

import org.saad.ebankingbackend.dtos.*;
import org.saad.ebankingbackend.entities.BankAccount;
import org.saad.ebankingbackend.entities.CurrentAccount;
import org.saad.ebankingbackend.entities.Customer;
import org.saad.ebankingbackend.entities.SavingAccount;
import org.saad.ebankingbackend.exceptions.BalanceNotSufficientException;
import org.saad.ebankingbackend.exceptions.BankAccountNotFoundException;
import org.saad.ebankingbackend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService  {
     CustomerDto saveCustomer(CustomerDto customerDto);
    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, Long customerId, double overDraft) throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, Long customerId, double interestRate) throws CustomerNotFoundException;

     List<CustomerDto> listCustomer();
     BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
     void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
     void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;
     void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;

    List<BankAccountDTO> bankAccountList();

     CustomerDto getCustomerDto(Long CustomerId) throws CustomerNotFoundException;

    CustomerDto updateCustomer(CustomerDto customerDto);

    void deleteCustomer(Long customerId);

    List<OperationDTO> accountOperationDTOS(String accountId);

    OpeartionHistoriqueDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;
}
