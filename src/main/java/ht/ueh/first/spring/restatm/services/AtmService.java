package ht.ueh.first.spring.restatm.services;

import ht.ueh.first.spring.restatm.models.accounts.Account;
import ht.ueh.first.spring.restatm.models.accounts.Transaction;
import ht.ueh.first.spring.restatm.repositories.AtmRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AtmService {

    private AtmRepository atmRepository;

    public AtmService(AtmRepository atmRepository) {
        this.atmRepository = atmRepository;
    }

    /**
     * Récupère tous les comptes
     */

    public List<Account> getAllAccounts() {
        return new ArrayList<>(atmRepository.getAccounts().values());
    }

    /**
     * Récupère un compte par son numéro
     */
    public Account getAccount(String accountNumber) {
        return atmRepository.getAccounts().get(accountNumber);
    }


    /**
     * Crée un nouveau compte
     */
    public Account createAccount(Account account) {
        if (atmRepository.getAccounts().containsKey(account.getAccountNumber())) {
            throw new IllegalArgumentException("Account already exists");
        }
        atmRepository.getAccounts().put(account.getAccountNumber(), account);
        return account;
    }

    /**
     * Supprime un compte
     */
    public void deleteAccount(String accountNumber) {
        if (!atmRepository.getAccounts().containsKey(accountNumber)) {
            throw new IllegalArgumentException("Account does not exist");
        }
        atmRepository.getAccounts().remove(accountNumber);
    }

    /**
     * Vérifie le PIN d'un compte
     */
    public boolean verifyPin(String accountNumber, String pin) {
        Account account = atmRepository.getAccounts().get(accountNumber);
        if (account == null) {
            return false;
        }
        return account.getPin().equals(pin);
    }

    /**
     * Consulte le solde d'un compte
     */
    public double getBalance(String accountNumber) {
        Account account = atmRepository.getAccounts().get(accountNumber);
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

        Account account = atmRepository.getAccounts().get(accountNumber);
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
     * Effectue un virement entre deux comptes
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
     * Récupère l'historique des transactions d'un compte
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
     * Récupère toutes les transactions
     */
    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(atmRepository.getTransactions());
    }

    /**
     * Ajoute une transaction à l'historique
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
     * Supprime un compte par son numéro
     */
    public void updatePin(String accountNumber, String newPin) {
        Account account = atmRepository.getAccounts().get(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }
        account.setPin(newPin);
    }


}




