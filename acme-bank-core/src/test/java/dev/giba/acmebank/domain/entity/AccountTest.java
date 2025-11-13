package dev.giba.acmebank.domain.entity;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    @DisplayName("Should raise an exception when given an invalid deposit amount")
    void shouldRaiseAnExceptionWhenGivenAnInvalidDepositAmount() {
        //Given
        var id = Long.MAX_VALUE;
        var number = "00011";
        var balance = BigDecimal.ONE;
        var transactions = Collections.<AccountTransaction>emptyList();
        var amount = BigDecimal.ZERO;

        //When
        var account = new Account(id, number, balance, transactions);
        var exception = assertThrows(IllegalArgumentException.class,
                () -> account.deposit(amount));

        //Then
        assertEquals("A valid amount is mandatory", exception.getMessage());
    }

    @Test
    @DisplayName("Should deposit successfully")
    void shouldDepositSuccessfully() {
        //Given
        var id = Long.MAX_VALUE;
        var number = "0001";
        var balance = BigDecimal.ZERO;
        var transactions = Collections.<AccountTransaction>emptyList();
        var amount = BigDecimal.TEN;

        //When
        var account = new Account(id, number, balance, transactions);
        var updatedAccount = account.deposit(amount);

        //Then
        assertEquals(amount, updatedAccount.balance());
        assertEquals(1, updatedAccount.transactions().size());
        assertEquals(TransactionType.DEPOSIT, updatedAccount.transactions().getFirst().type());
        assertEquals(amount, updatedAccount.transactions().getFirst().amount());
        assertEquals("Deposited amount", updatedAccount.transactions().getFirst().description());
    }

    @Test
    @DisplayName("Should raise an exception when given an invalid withdraw amount")
    void shouldRaiseAnExceptionWhenGivenAnInvalidWithdrawAmount() {
        //Given
        var id = Long.MAX_VALUE;
        var number = "00012";
        var balance = BigDecimal.TEN;
        var transactions = Collections.<AccountTransaction>emptyList();
        var amount = BigDecimal.ZERO;

        //When
        var account = new Account(id, number, balance, transactions);
        var exception = assertThrows(IllegalArgumentException.class,
                () -> account.withdraw(amount));

        //Then
        assertEquals("A valid amount is mandatory", exception.getMessage());
    }

    @Test
    @DisplayName("Should raise an exception when given a balance less then withdraw amount")
    void shouldRaiseAnExceptionWhenGivenAnBalanceLessThenWithdrawAmount() {
        //Given
        var id = Long.MAX_VALUE;
        var number = "00014";
        var balance = BigDecimal.ONE;
        var transactions = Collections.<AccountTransaction>emptyList();
        var amount = BigDecimal.TWO;

        //When
        var account = new Account(id, number, balance, transactions);
        var exception = assertThrows(IllegalArgumentException.class,
                () -> account.withdraw(amount));

        //Then
        assertEquals("Insufficient balance", exception.getMessage());
    }

    @Test
    @DisplayName("Should withdraw successfully")
    void shouldWithdrawSuccessfully() {
        //Given
        var id = Long.MAX_VALUE;
        var number = "00015";
        var balance = BigDecimal.TEN;
        var transactions = Collections.<AccountTransaction>emptyList();
        var amount = BigDecimal.TWO;

        //When
        var account = new Account(id, number, balance, transactions);
        var updatedAccount = account.withdraw(amount);

        //Then
        assertEquals(BigDecimal.valueOf(8), updatedAccount.balance());
        assertEquals(1, updatedAccount.transactions().size());
        assertEquals(TransactionType.WITHDRAW, updatedAccount.transactions().getFirst().type());
        assertEquals(amount, updatedAccount.transactions().getFirst().amount());
        assertEquals("Amount withdrawn", updatedAccount.transactions().getFirst().description());
    }

    @Test
    @DisplayName("Should add transaction successfully")
    void shouldAddTransactionSuccessfully() {
        //Given
        var id = Long.MAX_VALUE;
        var number = "0007";
        var balance = BigDecimal.ZERO;
        var transactions = Collections.<AccountTransaction>emptyList();
        var amount = BigDecimal.TEN;

        //When
        var account = new Account(id, number, balance, transactions);
        var updatedAccount = account.addTransaction(TransactionType.TRANSFER_IN, amount, "Transaction 1");

        //Then
        assertEquals(balance, updatedAccount.balance());
        assertEquals(1, updatedAccount.transactions().size());
        assertEquals(TransactionType.TRANSFER_IN, updatedAccount.transactions().getFirst().type());
        assertEquals(amount, updatedAccount.transactions().getFirst().amount());
        assertEquals( "Transaction 1", updatedAccount.transactions().getFirst().description());
    }

}
