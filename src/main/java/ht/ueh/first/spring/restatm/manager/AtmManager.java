package ht.ueh.first.spring.restatm.manager;

import ht.ueh.first.spring.restatm.models.Account;
import ht.ueh.first.spring.restatm.models.Transaction;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class AtmManager {

    private final Map<String, Account> accounts = new ConcurrentHashMap<>();
    private final List<Transaction> transactions = Collections.synchronizedList(new ArrayList<>());
    private final AtomicInteger transactionCounter = new AtomicInteger(1);

    public AtmManager() {
        // Initialisation avec quelques comptes de test
        accounts.put("123456", new Account("123456", "Jean Dupont", 1000.0, "1234"));
        accounts.put("789012", new Account("789012", "Marie Martin", 2500.0, "5678"));
        accounts.put("345678", new Account("345678", "Pierre Durand", 500.0, "9012"));
    }



    /**
     * Récupère tous les comptes*/

    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }

    /**
     * Récupère un compte par son numéro
     */
    public Account getAccountByNumber(String accountNumber) {
        return accounts.get(accountNumber);
    }


    /**
     * Crée un nouveau compte
     */
    public Account createAccount(Account account) {
        if (accounts.containsKey(account.getAccountNumber())) {
            throw new IllegalArgumentException("Account already exists");
        }
        accounts.put(account.getAccountNumber(), account);
        return account;
    }

    /**
     * Vérifie le PIN d'un compte
     */
    public boolean verifyPin(String accountNumber, String pin) {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            return false;
        }
        return account.getPin().equals(pin);
    }

    /**
     * Consulte le solde d'un compte
     */
    public double getBalance(String accountNumber) {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }
        return account.getBalance();
    }

    /**
     * Effectue un dépôt
     */
    public Account deposit(String accountNumber, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        Account account = accounts.get(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }

        account.setBalance(account.getBalance() + amount);
        addTransaction(accountNumber, "DEPOT", amount, account.getBalance());
        return account;
    }

    /**
     * Effectue un retrait
     */
    public Account withdraw(String accountNumber, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        Account account = accounts.get(accountNumber);
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
     * Effectue un virement entre deux comptes
     */
    public void transfer(String fromAccount, String toAccount, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        Account from = accounts.get(fromAccount);
        Account to = accounts.get(toAccount);

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
     * Récupère l'historique des transactions d'un compte
     */
    public List<Transaction> getTransactions(String accountNumber) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getAccountNumber().equals(accountNumber)) {
                result.add(t);
            }
        }
        return result;
    }

    /**
     * Récupère toutes les transactions
     */
    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }

    /**
     * Ajoute une transaction à l'historique
     */
    private void addTransaction(String accountNumber, String type, double amount, double balanceAfter) {
        Transaction transaction = new Transaction(
                String.valueOf(transactionCounter.getAndIncrement()),
                accountNumber,
                type,
                amount,
                balanceAfter
        );
        transactions.add(transaction);
    }

    /**
     * Supprime un compte par son numéro
     */
    public boolean deleteAccount(String accountNumber) {
        Account removed = accounts.remove(accountNumber);
        return removed != null; // true si kont la te egziste epi li efase, false sinon
    }

    public void updatePin(String accountNumber, String newPin) {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }
        account.setPin(newPin);
    }



}





