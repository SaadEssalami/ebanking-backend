package org.saad.ebankingbackend.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.saad.ebankingbackend.dtos.*;
import org.saad.ebankingbackend.entities.*;
import org.saad.ebankingbackend.enums.OperationType;
import org.saad.ebankingbackend.exceptions.*;
import org.saad.ebankingbackend.exceptions.CustomerNotFoundException;
import org.saad.ebankingbackend.mappers.BankAccountMapperImpl;
import org.saad.ebankingbackend.repositories.AccountRepository;
import org.saad.ebankingbackend.repositories.BankOperationRepository;
import org.saad.ebankingbackend.repositories.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImp implements BankAccountService{

    private CustomerRepository customerRepository;
    private AccountRepository accountRepository;
    private BankOperationRepository bankOperationRepository;
    private BankAccountMapperImpl bankAccountMapper;

    /*public BankAccountServiceImp(CustomerRepository customerRepository,AccountRepository accountRepository, BankOperationRepository bankOperationRepository) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.bankOperationRepository = bankOperationRepository;
    }*/

    @Override
    public CustomerDto saveCustomer(CustomerDto customerDto) {
        log.info("Saving new customer");
        Customer customer= bankAccountMapper.fromCustomerDto(customerDto);
        Customer savedCustomer = customerRepository.save(customer);
        return bankAccountMapper.fromCustomer(savedCustomer);
    }


    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, Long customerId, double overDraft) throws CustomerNotFoundException {
        Customer customer= customerRepository.findById(customerId).orElse(null);
        if(customer == null){
            throw new CustomerNotFoundException("Customer not found");
        }
        CurrentAccount currentAccount= new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(overDraft);
        CurrentAccount savedAccount= accountRepository.save(currentAccount);
        return bankAccountMapper.fromCurrentAccount(savedAccount);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, Long customerId, double interestRate) throws CustomerNotFoundException {
        Customer customer= customerRepository.findById(customerId).orElse(null);
        if(customer == null){
            throw new CustomerNotFoundException("Customer not found");
        }
        SavingAccount savingAccount= new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);
        SavingAccount savedAccountSaving= accountRepository.save(savingAccount);
        return bankAccountMapper.fromSavingBankAccount(savedAccountSaving);
    }


    @Override
    public List<CustomerDto> listCustomer() {

        List<Customer> customers =customerRepository.findAll();
        List<CustomerDto> customerDtos = customers.stream()
                .map(cust -> bankAccountMapper.fromCustomer(cust))
                .collect(Collectors.toList());
        return customerDtos;
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount= accountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException(""));
        if (bankAccount instanceof SavingAccount) {
            SavingAccount savingAccount= (SavingAccount) bankAccount;
            return bankAccountMapper.fromSavingBankAccount(savingAccount);
        }else{
            CurrentAccount currentAccount= (CurrentAccount) bankAccount;
            return bankAccountMapper.fromCurrentAccount(currentAccount);
        }
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount= accountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException(""));
        if (bankAccount.getBalance()< amount) {
            throw new BalanceNotSufficientException("Balance not sufficent");
        }
        Operation operation= new Operation();
        operation.setType(OperationType.DEBIT);
        operation.setAmount(amount);
        operation.setDescription(description);
        operation.setOperationdate(new Date());
        operation.setBankAccount(bankAccount);
        bankOperationRepository.save(operation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        accountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount= accountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException(""));

        Operation operation= new Operation();
        operation.setType(OperationType.CREDIT);
        operation.setAmount(amount);
        operation.setDescription(description);
        operation.setOperationdate(new Date());
        operation.setBankAccount(bankAccount);
        bankOperationRepository.save(operation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        accountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource,amount,"Transfer to "+accountIdDestination);
        credit(accountIdDestination, amount,"Transfer to "+accountIdSource);
    }

    @Override
    public List<BankAccountDTO> bankAccountList(){
        List<BankAccount> bankAccounts =accountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOS =bankAccounts.stream().map(banc -> {
            if (banc instanceof  SavingAccount) {
                SavingAccount savingAccount= (SavingAccount) banc;
                return bankAccountMapper.fromSavingBankAccount(savingAccount);
            }else{
                CurrentAccount currentAccount= (CurrentAccount) banc;
                return bankAccountMapper.fromCurrentAccount(currentAccount);
            }
        }).collect(Collectors.toList());
        return bankAccountDTOS;
    }
    @Override
    public CustomerDto getCustomerDto(Long CustomerId) throws CustomerNotFoundException {
       Customer customer=  customerRepository.findById(CustomerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        return bankAccountMapper.fromCustomer(customer);
    }

    @Override
    public CustomerDto updateCustomer(CustomerDto customerDto) {
        log.info("Saving new customer");
        Customer customer= bankAccountMapper.fromCustomerDto(customerDto);
        Customer savedCustomer = customerRepository.save(customer);
        return bankAccountMapper.fromCustomer(savedCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId){
        customerRepository.deleteById(customerId);
    }

    @Override
    public List<OperationDTO> accountOperationDTOS(String accountId){
        List<Operation> opeartions = bankOperationRepository.findByBankAccount_Id(accountId);
        return opeartions.stream().map(op -> bankAccountMapper.fromOperation(op))
                .collect(Collectors.toList());

    }

    @Override
    public OpeartionHistoriqueDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount = accountRepository.findById(accountId).orElseThrow(null);
        if (bankAccount == null) {
            throw new BankAccountNotFoundException("Account not found");
        }
        Page<Operation> operations= bankOperationRepository.findByBankAccount_Id(accountId, PageRequest.of(page,size));
        OpeartionHistoriqueDTO opeartionHistoriqueDTO= new OpeartionHistoriqueDTO();
        List<OperationDTO> operationDTOS= operations.getContent().stream().map(op -> bankAccountMapper.fromOperation(op)).collect(Collectors.toList());
        opeartionHistoriqueDTO.setOperationDTOList(operationDTOS);
        opeartionHistoriqueDTO.setAccountId(bankAccount.getId());
        opeartionHistoriqueDTO.setBalance(bankAccount.getBalance());
        opeartionHistoriqueDTO.setCurrentPage(page);
        opeartionHistoriqueDTO.setSize(size);
        opeartionHistoriqueDTO.setTotalPages(operations.getTotalPages());
        return opeartionHistoriqueDTO;
    }
}
