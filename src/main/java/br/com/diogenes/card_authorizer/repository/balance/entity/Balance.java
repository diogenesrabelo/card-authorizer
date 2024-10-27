package br.com.diogenes.card_authorizer.repository.balance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table
@NoArgsConstructor
public class Balance {

    @Id
    @Column(nullable = false, unique = true)
    private String account;

    private BigDecimal meal;

    private BigDecimal cash;

    private BigDecimal food;

    public Balance buildBalance(String account, BigDecimal meal, BigDecimal cash, BigDecimal food) {
        var balance = new Balance();
        balance.setAccount(account);
        balance.setMeal(meal);
        balance.setCash(cash);
        balance.setFood(food);
        return balance;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public BigDecimal getMeal() {
        return meal;
    }

    public void setMeal(BigDecimal meal) {
        this.meal = meal;
    }

    public BigDecimal getCash() {
        return cash;
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }

    public BigDecimal getFood() {
        return food;
    }

    public void setFood(BigDecimal food) {
        this.food = food;
    }
}
