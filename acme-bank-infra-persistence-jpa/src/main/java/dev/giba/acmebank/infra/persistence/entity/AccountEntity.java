package dev.giba.acmebank.infra.persistence.entity;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "account")
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String number;
    private BigDecimal balance;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "account_id")
    private List<TransactionEntity> transactions = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<TransactionEntity> getTransactions() {
        return transactions;
    }
    public void setTransactions(List<TransactionEntity> transactions) {
        this.transactions = transactions;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AccountEntity that = (AccountEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number);
    }
}
