package org.saad.ebankingbackend;

import org.saad.ebankingbackend.dtos.BankAccountDTO;
import org.saad.ebankingbackend.dtos.CurrentBankAccountDTO;
import org.saad.ebankingbackend.dtos.CustomerDto;
import org.saad.ebankingbackend.dtos.SavingBankAccountDTO;
import org.saad.ebankingbackend.entities.*;
import org.saad.ebankingbackend.enums.AccountStatus;
import org.saad.ebankingbackend.enums.OperationType;
import org.saad.ebankingbackend.exceptions.BalanceNotSufficientException;
import org.saad.ebankingbackend.exceptions.BankAccountNotFoundException;
import org.saad.ebankingbackend.exceptions.CustomerNotFoundException;
import org.saad.ebankingbackend.repositories.AccountRepository;
import org.saad.ebankingbackend.repositories.BankOperationRepository;
import org.saad.ebankingbackend.repositories.CustomerRepository;
import org.saad.ebankingbackend.services.BankAccountService;
import org.saad.ebankingbackend.services.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankingBackendApplication.class, args);
    }
    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService){
        return args -> {
            Stream.of("Saad", "Halima","Reda")
                    .forEach(name -> {
                        CustomerDto customer= new CustomerDto();
                        customer.setName(name);
                        customer.setEmail(name+"@gmail.com");
                        bankAccountService.saveCustomer(customer);
                    });
            bankAccountService.listCustomer().forEach(cust -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random()*4000, cust.getId(),9000);
                    bankAccountService.saveSavingBankAccount(Math.random()*6000, cust.getId(), 4.5);

                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }
            });
            List<BankAccountDTO> bankAccounts= bankAccountService.bankAccountList();
            for(BankAccountDTO bankAccountDTO:bankAccounts){

                for(int i = 0; i < 10; i++){
                    String accountId;
                    if (bankAccountDTO instanceof SavingBankAccountDTO) {
                        accountId= ((SavingBankAccountDTO) bankAccountDTO).getId();
                    }else{
                        accountId= ((CurrentBankAccountDTO) bankAccountDTO).getId();
                    }
                    bankAccountService.credit(accountId, 150000+Math.random()*160000,"Credit");
                    bankAccountService.debit(accountId, 1000+Math.random()*9000,"Debit");
                }
            }
        };
    }
//@Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            AccountRepository accountRepository,
                            BankOperationRepository bankOperationRepository){
        return args -> {
           Stream.of("Saad","Hamza", "Reda").forEach(name -> {
               Customer customer = new Customer();
               customer.setName(name);
               customer.setEmail(name+"@gmail.com");
               customerRepository.save(customer);
           });
           customerRepository.findAll().forEach(cust -> {
               CurrentAccount currentAccount = new CurrentAccount();
               currentAccount.setId(UUID.randomUUID().toString());
               currentAccount.setBalance(Math.random()*90000);
               currentAccount.setCreatedAt(new Date());
               currentAccount.setStatus(AccountStatus.CREATED);
               currentAccount.setCustomer(cust);
               currentAccount.setOverDraft(5000);
               accountRepository.save(currentAccount);

               SavingAccount savingAccount = new SavingAccount();
               savingAccount.setId(UUID.randomUUID().toString());
               savingAccount.setBalance(Math.random()*90000);
               savingAccount.setCreatedAt(new Date());
               savingAccount.setStatus(AccountStatus.CREATED);
               savingAccount.setCustomer(cust);
               savingAccount.setInterestRate(4.5);
               accountRepository.save(savingAccount);
           });
           accountRepository.findAll().forEach(acc -> {
               for(int i = 0; i < 10 ; i++){
                   Operation operation = new Operation();
                   operation.setAmount(Math.random()*12000);
                   operation.setType(Math.random() >0.5 ?OperationType.DEBIT : OperationType.CREDIT);
                   operation.setOperationdate(new Date());
                   operation.setBankAccount(acc);
                   bankOperationRepository.save(operation);
               }
           });

        };
    }

}
