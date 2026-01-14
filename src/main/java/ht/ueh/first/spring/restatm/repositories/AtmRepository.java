package ht.ueh.first.spring.restatm.repositories;

import ht.ueh.first.spring.restatm.models.accounts.Account;
import ht.ueh.first.spring.restatm.models.accounts.Transaction;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class AtmRepository {
    private final Map<String, Account> accounts = new ConcurrentHashMap<>();
    private final List<Transaction> transactions = Collections.synchronizedList(new ArrayList<>());
    private final AtomicInteger transactionCounter = new AtomicInteger(1);

    /**
     * Initialisation avec quelques comptes de test
     */
    public AtmRepository() {
        accounts.put("123456", new Account("123456", "Jean Dupont", 1000.0, "1234"));
        accounts.put("789012", new Account("789012", "Marie Martin", 2500.0, "5678"));
        accounts.put("345678", new Account("345678", "Pierre Durand", 500.0, "9012"));
        accounts.put("654321", new Account("654321", "Calvert Wanguy", 25500.0, "5678"));
    }

    /**
     * Retrieves all accounts
     */
    public Map<String, Account> getAccounts() {
        return accounts;
    }

    /**
     * Retrieves all transactions
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Retrieves the transaction counter
     */
    public AtomicInteger getTransactionCounter() {
        return transactionCounter;
    }
}