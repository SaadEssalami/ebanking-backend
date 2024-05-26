package org.saad.ebankingbackend.exceptions;

public class BalanceNotSufficientException extends Exception {
    public BalanceNotSufficientException(String balanceNotSufficent) {
        super(balanceNotSufficent);
    }
}
