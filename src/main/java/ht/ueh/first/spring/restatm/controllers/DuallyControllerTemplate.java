package ht.ueh.first.spring.restatm.controllers;

import ht.ueh.first.spring.restatm.services.AtmService;
import ht.ueh.first.spring.restatm.models.accounts.Account;
import ht.ueh.first.spring.restatm.models.accounts.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/duallyatm")

public class DuallyControllerTemplate {

    private final AtmService atmService;

    /**
     * Constructeur pour l'injection de dépendances
     * Spring injectera automatiquement l'instance d'AtmManager
     */

    public DuallyControllerTemplate(AtmService atmService) {
        this.atmService = atmService;
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(atmService.getAllAccounts());
    }

    @GetMapping("/accounts/{accountNumber}")
    public ResponseEntity<Account> getAccount(@PathVariable String accountNumber) {
        if (accountNumber == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(atmService.getAccount(accountNumber));
    }

    @PostMapping("/accounts")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        try {
            Account createdAccount = atmService.createAccount(account);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Endpoint 4 : Consulte le solde d'un compte

    @GetMapping("/accounts/{accountNumber}/balance")
    public ResponseEntity<Map<String, Object>> getBalance(@PathVariable String accountNumber) {
        Account account = atmService.getAccount(accountNumber);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("accountNumber", accountNumber);
        response.put("balance", account.getBalance());
        return ResponseEntity.ok(response);
    }


    // Endpoint 5 : Vérifie le PIN d'un compte

    @PostMapping("/accounts/{accountNumber}/verify-pin")
    public ResponseEntity<Map<String, Boolean>> verifyPin(
            @PathVariable String accountNumber,
            @RequestBody Map<String, String> request) {

        Account account = atmService.getAccount(accountNumber);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }

        String pin = request.get("pin");
        boolean isValid = atmService.verifyPin(accountNumber, pin);

        Map<String, Boolean> response = new HashMap<>();
        response.put("valid", isValid);
        return ResponseEntity.ok(response);
    }

    //Endpoint 6 : Effectue un dépôt sur un compte

    @PostMapping("/accounts/{accountNumber}/deposit")
    public ResponseEntity<Account> deposit(@PathVariable String accountNumber, @RequestBody Map<String, Double> request) {
        try {
            double amount = request.get("amount");
            Account account = atmService.deposit(accountNumber, amount);
            if (account == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(account);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    //Endpoint 7 : Effectue un retrait sur un compte

    @PostMapping("/accounts/{accountNumber}/withdraw")
    public ResponseEntity<Account> withdraw(@PathVariable
            String accountNumber,
            @RequestBody Map<String, Double> request) {
        try {
            double amount = request.get("amount");
            Account account = atmService.withdraw(accountNumber, amount);
            if (account == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(account);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Endpoint 8 : Effectue un virement entre deux comptes
    @PostMapping("/transfer")
    public ResponseEntity<Map<String, String>> transfer(@RequestBody Map<String, Object> request) {
        try {
            String From = (String) request.get("from");
            String To = (String) request.get("to");
            double amount = ((Number) request.get("amount")).doubleValue();

            atmService.transfer(From, To, amount);
            return ResponseEntity.ok(Map.of("message", "Transfer successful"));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
        }

    // Endpoint 9 : Récupère l'historique des transactions d'un compte

    @GetMapping("/accounts/{accountNumber}/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable String accountNumber) {
        List<Transaction> transactions = atmService.getTransactions(accountNumber);
        return ResponseEntity.ok(transactions);
    }

    // Endpoint 10 : Récupère toutes les transactions

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = atmService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    // ============================================
    // EXERCICES SUPPLÉMENTAIRES (OPTIONNEL)
    // ============================================

    // EXERCICE 1 : Ajouter un endpoint DELETE

     @DeleteMapping("/accounts/{accountNumber}")
     public ResponseEntity<Void> deleteAccount(@PathVariable String accountNumber) {
     Account account = atmService.getAccount(accountNumber);
         if (account == null) {
            return ResponseEntity.notFound().build();
         }
        return ResponseEntity.ok().build();
     }

    /**
     * EXERCICE 2 : Ajouter un endpoint PUT
     *
     * @PutMapping("/accounts/{accountNumber}/pin")
     * - Modifie le PIN d'un compte
     * - Body : { "oldPin": "1234", "newPin": "5678" }
     * - Vérifier l'ancien PIN avant de changer
     */

    /**
     * EXERCICE 3 : Filtrer les transactions
     *
     * @GetMapping("/accounts/{accountNumber}/transactions/deposits")
     * - Retourne uniquement les dépôts
     * - Filtrer les transactions où type = "DEPOT"
     */

    /**
     * EXERCICE 4 : Statistiques de compte
     *
     * @GetMapping("/accounts/{accountNumber}/stats")
     * - Retourne : nombre de transactions, total dépôts, total retraits, solde actuel
     * - Format : Map<String, Object>
     */
}

