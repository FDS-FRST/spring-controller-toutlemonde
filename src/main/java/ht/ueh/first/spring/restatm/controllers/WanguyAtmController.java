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

/**
 * Contrôleur REST pour la gestion des opérations ATM
 */
@RestController
@RequestMapping("/api/atm")
public class WanguyAtmController {

    private final AtmService atmService;

    /**
     * Injection de dépendance via le constructeur
     */
    public WanguyAtmController(AtmService atmService) {
        this.atmService = atmService;
    }

    /**
     * Endpoint 1 : Liste de tous les comptes
     */
    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(atmService.getAllAccounts());
    }

    /**
     * Endpoint 2 : Récupérer un compte par numéro
     */
    @GetMapping("/accounts/{accountNumber}")
    public ResponseEntity<Account> getAccount(@PathVariable String accountNumber) {
        Account account = atmService.getAccount(accountNumber);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(account);
    }

    /**
     * Endpoint 3 : Créer un compte
     */
    @PostMapping("/accounts")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        try {
            Account createdAccount = atmService.createAccount(account);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint 4 : Consulter le solde
     */
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

    /**
     * Endpoint 5 : Vérifier le PIN
     */
    @PostMapping("/accounts/{accountNumber}/verify-pin")
    public ResponseEntity<Map<String, Boolean>> verifyPin(
            @PathVariable String accountNumber,
            @RequestBody Map<String, String> request) {

        String pin = request.get("pin");
        boolean valid = atmService.verifyPin(accountNumber, pin);

        Map<String, Boolean> response = new HashMap<>();
        response.put("valid", valid);

        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint 6 : Dépôt
     */
    @PostMapping("/accounts/{accountNumber}/deposit")
    public ResponseEntity<Account> deposit(
            @PathVariable String accountNumber,
            @RequestBody Map<String, Double> request) {

        Double amount = request.get("amount");
        if (amount == null || amount <= 0) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Account account = atmService.deposit(accountNumber, amount);
            return ResponseEntity.ok(account);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint 7 : Retrait
     */
    @PostMapping("/accounts/{accountNumber}/withdraw")
    public ResponseEntity<Account> withdraw(
            @PathVariable String accountNumber,
            @RequestBody Map<String, Double> request) {

        Double amount = request.get("amount");
        if (amount == null || amount <= 0) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Account account = atmService.withdraw(accountNumber, amount);
            return ResponseEntity.ok(account);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint 8 : Virement
     */
    @PostMapping("/transfer")
    public ResponseEntity<Map<String, String>> transfer(
            @RequestBody Map<String, Object> request) {

        String from = (String) request.get("from");
        String to = (String) request.get("to");
        double amount = ((Number) request.get("amount")).doubleValue();

        Map<String, String> response = new HashMap<>();

        try {
            atmService.transfer(from, to, amount);
            response.put("message", "Virement effectué avec succès");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Erreur lors du virement");
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Endpoint 9 : Transactions d’un compte
     */
    @GetMapping("/accounts/{accountNumber}/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(
            @PathVariable String accountNumber) {

        List<Transaction> transactions = atmService.getTransactions(accountNumber);
        if (transactions == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transactions);
    }

    /**
     * Endpoint 10 : Toutes les transactions
     */
    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(atmService.getAllTransactions());
    }
}

// ============================================
// EXERCICES SUPPLÉMENTAIRES (OPTIONNEL)
// ============================================

/**
 * EXERCICE 1 : Ajouter un endpoint DELETE
 *
 * @DeleteMapping("/accounts/{accountNumber}")
 * - Supprime un compte
 * - Retourne 200 si succès, 404 si compte inexistant
 * - Note : Vous devrez d'abord ajouter la méthode deleteAccount() dans AtmManager
 */

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
