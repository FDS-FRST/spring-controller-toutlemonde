package ht.ueh.first.spring.restatm.controllers;

import ht.ueh.first.spring.restatm.manager.AtmManager;
import ht.ueh.first.spring.restatm.models.Account;
import ht.ueh.first.spring.restatm.models.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Contrôleur REST pour la gestion des opérations ATM
 *
 * TODO : Ajouter l'annotation @RestController
 * TODO : Ajouter l'annotation @RequestMapping avec le chemin de base "/api/atm"
 */

@RestController
@RequestMapping

public class MitoAtmController {

    private final AtmManager atmManager;

    /**
     * Constructeur pour l'injection de dépendances
     * Spring injectera automatiquement l'instance d'AtmManager
     */
    public MitoAtmController(AtmManager atmManager) {
        this.atmManager = atmManager;
    }

    /**
     * Endpoint 1 : Récupère la liste de tous les comptes
     *
     * TODO : Ajouter @GetMapping("/accounts")
     * TODO : Retourner ResponseEntity.ok() avec la liste des comptes
     */
    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        // TODO : Implémenter cette méthode
        List<Account> accounts = atmManager.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    /**
     * Endpoint 2 : Récupère un compte spécifique par son numéro
     *
     * TODO : Ajouter @GetMapping("/accounts/{accountNumber}")
     * TODO : Ajouter @PathVariable pour extraire accountNumber
     * TODO : Retourner 404 si le compte n'existe pas
     */

    @GetMapping("/accounts/{accountNumber}")
    public ResponseEntity<Account> getAccount(@PathVariable String accountNumber) {
        Account account = atmManager.getAccount(accountNumber);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(account);
    }

    /**
     * Endpoint 3 : Crée un nouveau compte
     *
     * TODO : Ajouter @PostMapping("/accounts")
     * TODO : Ajouter @RequestBody pour recevoir le compte en JSON
     * TODO : Retourner 201 Created en cas de succès
     * TODO : Retourner 400 Bad Request en cas d'erreur
     */
    @PostMapping("/accounts")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        try {
            Account createdAccount = atmManager.createAccount(account);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint 4 : Consulte le solde d'un compte
     *
     * TODO : Ajouter @GetMapping("/accounts/{accountNumber}/balance")
     * TODO : Retourner un Map avec accountNumber et balance
     */
    public ResponseEntity<Map<String, Object>> getBalance(String accountNumber) {
        // TODO : Implémenter cette méthode
        return null;
    }

    /**
     * Endpoint 5 : Vérifie le PIN d'un compte
     *
     * TODO : Ajouter @PostMapping("/accounts/{accountNumber}/verify-pin")
     * TODO : Recevoir le PIN dans le body (Map<String, String>)
     * TODO : Retourner un Map avec "valid" : true/false
     */
    public ResponseEntity<Map<String, Boolean>> verifyPin(
            String accountNumber,
            Map<String, String> request) {
        // TODO : Implémenter cette méthode
        return null;
    }

    /**
     * Endpoint 6 : Effectue un dépôt sur un compte
     *
     * TODO : Ajouter @PostMapping("/accounts/{accountNumber}/deposit")
     * TODO : Recevoir le montant dans le body (Map<String, Double>)
     * TODO : Extraire "amount" de la Map
     * TODO : Gérer les erreurs (compte inexistant, montant invalide)
     */
    @PostMapping("/accounts/{accountNumber}/deposit")
    public ResponseEntity<Account> deposit(
            @PathVariable String accountNumber,
            @RequestBody Map<String, Double> request) {
        try {
            Double amount = request.get("amount");
            if (amount == null || amount <= 0) {
                return ResponseEntity.badRequest().build();
            }
            Account account = atmManager.deposit(accountNumber, amount);
            if (account == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(account);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint 7 : Effectue un retrait sur un compte
     *
     * TODO : Ajouter @PostMapping("/accounts/{accountNumber}/withdraw")
     * TODO : Similaire au dépôt, mais avec withdraw()
     * TODO : Gérer le cas de solde insuffisant
     */
    @PostMapping("/accounts/{accountNumber}/withdraw")
    public ResponseEntity<Account> withdraw(
            @PathVariable String accountNumber,
            @RequestBody Map<String, Double> request) {
        try {
            Double amount = request.get("amount");
            if (amount == null || amount <= 0) {
                return ResponseEntity.badRequest().build();
            }
            Account account = atmManager.withdraw(accountNumber, amount);
            if (account == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(account);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint 8 : Effectue un virement entre deux comptes
     *
     * TODO : Ajouter @PostMapping("/transfer")
     * TODO : Recevoir from, to, amount dans le body (Map<String, Object>)
     * TODO : Convertir amount en double : ((Number) request.get("amount")).doubleValue()
     * TODO : Retourner un message de succès ou d'erreur
     */
    @PostMapping("/transfer")
    public ResponseEntity<Map<String, String>> transfer(
            @RequestBody Map<String, Object> request) {
        try {
            String from = (String) request.get("from");
            String to = (String) request.get("to");

            Double amount = ((Number) request.get("amount")).doubleValue();

            atmManager.transfer(from, to, amount);
            return ResponseEntity.ok(Map.of("message", "Transfer successful"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Endpoint 9 : Récupère l'historique des transactions d'un compte
     *
     * TODO : Ajouter @GetMapping("/accounts/{accountNumber}/transactions")
     * TODO : Retourner la liste des transactions
     */
    @GetMapping("/accounts/{accountNumber}/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(
            @PathVariable String accountNumber) {
        List<Transaction> transactions = atmManager.getTransactions(accountNumber);

        if (transactions == null) {
            return ResponseEntity.notFound().build();
        }return ResponseEntity.ok(transactions);
    }

    /**
     * Endpoint 10 : Récupère toutes les transactions
     *
     * TODO : Ajouter @GetMapping("/transactions")
     * TODO : Retourner toutes les transactions du système
     */
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        // TODO : Implémenter cette méthode
        return null;
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
    @DeleteMapping("/accounts/{accountNumber}")
    public ResponseEntity<Map<String, String>> deleteAccount(
            @PathVariable String accountNumber) {

        try {
            // Supprime le compte via AtmManager
            boolean deleted = atmManager.deleteAccount(accountNumber);

            if (!deleted) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Compte non trouvé"));
            }

            return ResponseEntity.ok(Map.of("message", "Compte supprimé avec succès"));

        } catch (IllegalArgumentException e) {
            // Gère les cas comme "solde non nul", etc.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
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

