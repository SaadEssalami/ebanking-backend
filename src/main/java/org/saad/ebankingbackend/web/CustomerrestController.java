package org.saad.ebankingbackend.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.saad.ebankingbackend.dtos.BankAccountDTO;
import org.saad.ebankingbackend.dtos.CustomerDto;
import org.saad.ebankingbackend.entities.Customer;
import org.saad.ebankingbackend.exceptions.CustomerNotFoundException;
import org.saad.ebankingbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping(/customers)
@AllArgsConstructor
@Slf4j

public class CustomerrestController {
    private BankAccountService bankAccountService;
    @GetMapping("/customers")
    public List<CustomerDto> customers(){
       return bankAccountService.listCustomer();
    }

    @GetMapping("/customers/{id}")
    public CustomerDto getCustomerDto(@PathVariable(name= "id") Long customerId) throws CustomerNotFoundException {
        return bankAccountService.getCustomerDto(customerId);
    }

    @PostMapping("/addCustomer")
    public CustomerDto saveCustomer(
           @RequestBody CustomerDto customerDto){
       return bankAccountService.saveCustomer(customerDto);
    }

    @PutMapping("/updateCustomer/{customerId}")
    public CustomerDto customerDto(@PathVariable Long customerId, @RequestBody CustomerDto customerDto){
        customerDto.setId(customerId);
        return bankAccountService.updateCustomer(customerDto);
    }
    @DeleteMapping("customers/{id}")
    public void deleteCustomer(@PathVariable Long id){
        bankAccountService.deleteCustomer(id);
    }


}


