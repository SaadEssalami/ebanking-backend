package org.saad.ebankingbackend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.saad.ebankingbackend.entities.BankAccount;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class CustomerDto {
    private Long id;
    private String name;
    private String email;
}
