package ht.ueh.first.spring.restatm.models.accounts;

public class Account {
    private String accountNumber;
    private String owner;
    private double balance;
    private String pin;

    public Account() {
    }

    public Account(String accountNumber, String owner, double balance, String pin) {
        this.accountNumber = accountNumber;
        this.owner = owner;
        this.balance = balance;
        this.pin = pin;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}

