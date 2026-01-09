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
@RequestMapping("/api/whitchyatm")
public class WhitchyAtmController {

    private final AtmManager atmManager;

    /**
     * Constructeur pour l'injection de dépendances
     * Spring injectera automatiquement l'instance d'AtmManager
     */
    public WhitchyAtmController(AtmManager atmManager) {
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
        return ResponseEntity.ok(atmManager.getAllAccounts());
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
        for  (Account account : atmManager.getAllAccounts()) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return ResponseEntity.ok(account);
            }
        }
        return ResponseEntity.notFound().build();
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
          Account created = atmManager.createAccount(account);
          return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint 4 : Consulte le solde d'un compte
     *
     * TODO : Ajouter @GetMapping("/accounts/{accountNumber}/balance")
     * TODO : Retourner un Map avec accountNumber et balance
     */
    @GetMapping("/accounts/{accountNumber}/balance")
    public ResponseEntity<Map<String, Object>> getBalance(@PathVariable String accountNumber) {
        for  (Account account : atmManager.getAllAccounts()) {
            if (account.getAccountNumber().equals(accountNumber)) {
                double balance = atmManager.getBalance(accountNumber);
                Map<String, Object> result = Map.of(
                        "accountNumber", accountNumber,
                        "balance", balance
                );
                return ResponseEntity.ok(result);
            }
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Endpoint 5 : Vérifie le PIN d'un compte
     *
     * TODO : Ajouter @PostMapping("/accounts/{accountNumber}/verify-pin")
     * TODO : Recevoir le PIN dans le body (Map<String, String>)
     * TODO : Retourner un Map avec "valid" : true/false
     */
    @PostMapping("/accounts/{accountNumber}/verify-pin")
    public ResponseEntity<Map<String, Boolean>> verifyPin(
            @PathVariable String accountNumber,
            @RequestBody Map<String, String> request) {
        try {
            String pin = request.get("pin");
            boolean isValid = atmManager.verifyPin(accountNumber, pin);
            Map<String, Boolean> result = Map.of("valid", isValid);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
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
            double amount = request.get("amount");
            Account updatedAccount = atmManager.deposit(accountNumber, amount);
            return ResponseEntity.ok(updatedAccount);
        } catch (Exception e) {
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
    public ResponseEntity<Account> withdraw(
            String accountNumber,
            Map<String, Double> request) {
        // TODO : Implémenter cette méthode
        return null;
    }

    /**
     * Endpoint 8 : Effectue un virement entre deux comptes
     *
     * TODO : Ajouter @PostMapping("/transfer")
     * TODO : Recevoir from, to, amount dans le body (Map<String, Object>)
     * TODO : Convertir amount en double : ((Number) request.get("amount")).doubleValue()
     * TODO : Retourner un message de succès ou d'erreur
     */
    public ResponseEntity<Map<String, String>> transfer(Map<String, Object> request) {
        // TODO : Implémenter cette méthode
        return null;
    }

    /**
     * Endpoint 9 : Récupère l'historique des transactions d'un compte
     *
     * TODO : Ajouter @GetMapping("/accounts/{accountNumber}/transactions")
     * TODO : Retourner la liste des transactions
     */
    public ResponseEntity<List<Transaction>> getTransactions(String accountNumber) {
        // TODO : Implémenter cette méthode
        return null;
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

