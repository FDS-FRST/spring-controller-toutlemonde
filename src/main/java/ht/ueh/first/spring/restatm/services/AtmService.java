package ht.ueh.first.spring.restatm.services;

import ht.ueh.first.spring.restatm.models.accounts.Account;
import ht.ueh.first.spring.restatm.models.accounts.Transaction;
import ht.ueh.first.spring.restatm.repositories.AtmRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AtmService {

    private AtmRepository atmRepository;

    /**
     * I
     * @param atmRepository
     */
    public AtmService(AtmRepository atmRepository) {
        this.atmRepository = atmRepository;
    }

    /**
     * Retrieves all accounts
     */
    public List<Account> getAllAccounts() {
        return new ArrayList<>(atmRepository.getAccounts().values());
    }

    /**
     * Retrieves an account
     * @param accountNumber number of account to find
     */
    public Account getAccount(String accountNumber) {
        return atmRepository.getAccounts().get(accountNumber);
    }


    /**
     * Creates a new account
     */
    public Account createAccount(Account account) {
        if (atmRepository.getAccounts().containsKey(account.getAccountNumber())) {
            throw new IllegalArgumentException("Account already exists");
        }
        atmRepository.getAccounts().put(account.getAccountNumber(), account);
        return account;
    }

    /**
     * Deletes an account
     */
    public void deleteAccount(String accountNumber) {
        if (!atmRepository.getAccounts().containsKey(accountNumber)) {
            throw new IllegalArgumentException("Account does not exist");
        }
        atmRepository.getAccounts().remove(accountNumber);
    }

    /**
     * Checks the pin of an account
     */
    public boolean verifyPin(String accountNumber, String pin) {
        Account account = atmRepository.getAccounts().get(accountNumber);
        if (account == null) {
            return false;
        }
        return account.getPin().equals(pin);
    }

    /**
     * Checks the balance of an account
     *
     * @param accountNumber A given account nnumber
     */
    public double getBalance(String accountNumber) {
        Account account = atmRepository.getAccounts().get(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }
        return account.getBalance();
    }

    /**
     * Makes a deposit
     */
    public Account deposit(String accountNumber, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        Account account = atmRepository.getAccounts().get(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }

        account.setBalance(account.getBalance() + amount);
        addTransaction(accountNumber, "DEPOT", amount, account.getBalance());
        return account;
    }

    /**
     * Makes a withdrawal
     */
    public Account withdraw(String accountNumber, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        Account account = atmRepository.getAccounts().get(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }

        if (account.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        account.setBalance(account.getBalance() - amount);
        addTransaction(accountNumber, "RETRAIT", amount, account.getBalance());
        return account;
    }

    /**
     * Makes a transfer between two accounts
     */
    public void transfer(String fromAccount, String toAccount, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        Account from = atmRepository.getAccounts().get(fromAccount);
        Account to = atmRepository.getAccounts().get(toAccount);

        if (from == null || to == null) {
            throw new IllegalArgumentException("Account not found");
        }

        if (from.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + amount);

        addTransaction(fromAccount, "VIREMENT_DEBIT", amount, from.getBalance());
        addTransaction(toAccount, "VIREMENT_CREDIT", amount, to.getBalance());
    }

    /**
     * Retrieves the transaction history of an account
     */
    public List<Transaction> getTransactions(String accountNumber) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction t : atmRepository.getTransactions()) {
            if (t.getAccountNumber().equals(accountNumber)) {
                result.add(t);
            }
        }
        return result;
    }

    /**
     * Retrieves the transactions
     */
    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(atmRepository.getTransactions());
    }

    /**
     * Adds a transaction
     */
    private void addTransaction(String accountNumber, String type, double amount, double balanceAfter) {
        Transaction transaction = new Transaction(
                String.valueOf(atmRepository.getTransactionCounter().getAndIncrement()),
                accountNumber,
                type,
                amount,
                balanceAfter
        );
        atmRepository.getTransactions().add(transaction);
    }

    /**
     * Modifies the pin of an account
     */
    public void updatePin(String accountNumber, String newPin) {
        Account account = atmRepository.getAccounts().get(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }
        account.setPin(newPin);
    }


}




