package org.saad.ebankingbackend.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.saad.ebankingbackend.dtos.BankAccountDTO;
import org.saad.ebankingbackend.dtos.OpeartionHistoriqueDTO;
import org.saad.ebankingbackend.dtos.OperationDTO;
import org.saad.ebankingbackend.exceptions.BankAccountNotFoundException;
import org.saad.ebankingbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
//@Data
@AllArgsConstructor
public class bankAccountRestController {
    private BankAccountService bankAccountService;

    @GetMapping("/accounts/{accountID}")
    public BankAccountDTO getBankAccount(@PathVariable String accountID) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountID);
    }

    @GetMapping("/allAccounts")
    public List<BankAccountDTO> listAccounts(){
        return bankAccountService.bankAccountList();
    }


    @GetMapping("/account/{accountId}/operations")
    public List<OperationDTO> getOperationDTOS(@PathVariable String accountId){
        return bankAccountService.accountOperationDTOS(accountId);
    }

    @GetMapping("/account/{accountId}/pageOperation")
    public OpeartionHistoriqueDTO getOperationHistory(@PathVariable String accountId,
                                                      @RequestParam(name = "page", defaultValue = "0") int page,
                                                      @RequestParam(name = "size", defaultValue = "5") int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(accountId,page,size);
    }
}
