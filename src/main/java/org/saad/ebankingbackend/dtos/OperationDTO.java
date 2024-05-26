package org.saad.ebankingbackend.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.saad.ebankingbackend.entities.BankAccount;
import org.saad.ebankingbackend.enums.OperationType;

import java.util.Date;


@Data
public class OperationDTO {
    private Long id;
    private Date operationdate;
    private double amount;
    private OperationType type;
    private String description;
}
