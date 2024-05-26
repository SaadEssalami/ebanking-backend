package org.saad.ebankingbackend.mappers;

import org.saad.ebankingbackend.dtos.CurrentBankAccountDTO;
import org.saad.ebankingbackend.dtos.CustomerDto;
import org.saad.ebankingbackend.dtos.OperationDTO;
import org.saad.ebankingbackend.dtos.SavingBankAccountDTO;
import org.saad.ebankingbackend.entities.CurrentAccount;
import org.saad.ebankingbackend.entities.Customer;
import org.saad.ebankingbackend.entities.Operation;
import org.saad.ebankingbackend.entities.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapperImpl {
    public CustomerDto fromCustomer(Customer customer){
        CustomerDto customerDto= new CustomerDto();
        BeanUtils.copyProperties(customer,customerDto);
//        customerDto.setId(customer.getId());
//        customerDto.setName(customer.getName());
//        customerDto.setEmail(customer.getEmail());
        return customerDto;
    }
    public Customer fromCustomerDto(CustomerDto customerDto){
        Customer customer= new Customer();
        BeanUtils.copyProperties(customerDto, customer);
        return customer;
    }

    public SavingBankAccountDTO fromSavingBankAccount(SavingAccount savingAccount){
        SavingBankAccountDTO savingBankAccountDTO = new SavingBankAccountDTO();
        BeanUtils.copyProperties(savingAccount,savingBankAccountDTO);
        savingBankAccountDTO.setCustomerDto(fromCustomer(savingAccount.getCustomer()));
        savingBankAccountDTO.setType(savingAccount.getClass().getSimpleName());
        return savingBankAccountDTO;
    }

    public SavingAccount fromSavingAccountDTO(SavingBankAccountDTO savingBankAccountDTO){
        SavingAccount savingAccount= new SavingAccount();
        BeanUtils.copyProperties(savingBankAccountDTO, savingAccount);
        savingAccount.setCustomer(fromCustomerDto(savingBankAccountDTO.getCustomerDto()));
        return savingAccount;
    }

    public CurrentBankAccountDTO fromCurrentAccount(CurrentAccount currentAccount){
        CurrentBankAccountDTO currentBankAccountDTO= new CurrentBankAccountDTO();
        BeanUtils.copyProperties(currentAccount, currentBankAccountDTO);
        currentBankAccountDTO.setCustomerDto(fromCustomer(currentAccount.getCustomer()));
        currentBankAccountDTO.setType(currentAccount.getClass().getSimpleName());
        return currentBankAccountDTO;
    }

    public CurrentAccount fromCurrentAccountDTO(CurrentBankAccountDTO currentBankAccountDTO){
        CurrentAccount currentAccount= new CurrentAccount();
        BeanUtils.copyProperties(currentBankAccountDTO, currentAccount);
        currentAccount.setCustomer(fromCustomerDto(currentBankAccountDTO.getCustomerDto()));
        return currentAccount;
    }

    public OperationDTO fromOperation(Operation operation){
        OperationDTO operationDTO = new OperationDTO();
        BeanUtils.copyProperties(operation, operationDTO);
        return operationDTO;
    }

//    public Operation fromOperation(OperationDTO operationDTO){
//        Operation operation = new Operation();
//        BeanUtils.copyProperties(operationDTO, operation);
//        return null;
//    }
}
