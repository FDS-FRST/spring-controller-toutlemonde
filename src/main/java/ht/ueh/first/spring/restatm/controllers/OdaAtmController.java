package ht.ueh.first.spring.restatm.controllers;

import ht.ueh.first.spring.restatm.services.AtmService;
import ht.ueh.first.spring.restatm.models.accounts.Account;
import ht.ueh.first.spring.restatm.models.accounts.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;

/**
 * Contrôleur REST pour la gestion des opérations ATM
 * <p>
 * TODO : Ajouter l'annotation @RestController
 * TODO : Ajouter l'annotation @RequestMapping avec le chemin de base "/api/atm"
 */

@RestController
@RequestMapping("api/oda/atm")

public class OdaAtmController {


    private final AtmService atmService;

    /**
     * Constructeur pour l'injection de dépendances
     * Spring injectera automatiquement l'instance d'AtmManager
     */
    public OdaAtmController(AtmService atmService) {
        this.atmService = atmService;
    }

    @GetMapping("/")

    public String index() {
        return "welcome to oda atm ooo";
    }

    /**
     * Endpoint 1 : Récupère la liste de tous les comptes
     * <p>
     * TODO : Ajouter @GetMapping("/accounts")
     * TODO : Retourner ResponseEntity.ok() avec la liste des comptes
     */
    @GetMapping("/accounts")

    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = atmService.getAllAccounts();
        // TODO : Implémenter cette méthode
        return ResponseEntity.ok(accounts);
    }

    /**
     * Endpoint 2 : Récupère un compte spécifique par son numéro
     * <p>
     * TODO : Ajouter @GetMapping("/accounts/{accountNumber}")
     * TODO : Ajouter @PathVariable pour extraire accountNumber
     * TODO : Retourner 404 si le compte n'existe pas
     */
    @GetMapping("/accounts/{accountNumber}")
    public ResponseEntity<Account> getAccount(
            @PathVariable String accountNumber) {
        Account account = atmService.getAccount(accountNumber);
        if (account == null) {
            return ResponseEntity.notFound().build(); // 404
        }

        return ResponseEntity.ok(account); // 200
    }

    /**
     * Endpoint 3 : Crée un nouveau compte
     * <p>
     * TODO : Ajouter @PostMapping("/accounts")
     * TODO : Ajouter @RequestBody pour recevoir le compte en JSON
     * TODO : Retourner 201 Created en cas de succès
     * TODO : Retourner 400 Bad Request en cas d'erreur
     */
    @PostMapping("/account")

    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        // TODO : Implémenter cette méthode avec try-catch
        try {
            Account createdAccount = atmService.createAccount(account);
            return ResponseEntity.status(201).body(createdAccount); // 201 Created
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // 400
        }
    }

    /**
     * Endpoint 4 : Consulte le solde d'un compte
     * <p>
     * TODO : Ajouter @GetMapping("/accounts/{accountNumber}/balance")
     * TODO : Retourner un Map avec accountNumber et balance
     */
    @GetMapping("/accounts/{accountNumber}/balance")
    public ResponseEntity<Map<String, Object>> getBalance(@PathVariable String accountNumber) {
        try {
            double balance = atmService.getBalance(accountNumber);

            Map<String, Object> response = new HashMap<>();
            response.put("accountNumber", accountNumber);
            response.put("balance", balance);

            return ResponseEntity.ok(response); // retounen Map la kòm JSON
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build(); // 404 si kont pa egziste
        }
    }


    /**
     * Endpoint 5 : Vérifie le PIN d'un compte
     * <p>
     * TODO : Ajouter @PostMapping("/accounts/{accountNumber}/verify-pin")
     * TODO : Recevoir le PIN dans le body (Map<String, String>)
     * TODO : Retourner un Map avec "valid" : true/false
     */

    @PostMapping("/accounts/{accountNumber}/verify-pin")
    public ResponseEntity<Map<String, Boolean>> verifyPin(
            @PathVariable String accountNumber,
            @RequestBody Map<String, String> request) {

        Map<String, Boolean> response = new HashMap<>();

        // Ranmase PIN nan body
        String pin = request.get("pin");
        if (pin == null) {
            response.put("valid", false);
            return ResponseEntity.badRequest().body(response);
        }

        // Verify PIN ak AtmManager
        boolean isValid = atmService.verifyPin(accountNumber, pin);

        response.put("valid", isValid);
        return ResponseEntity.ok(response); // 200 OK
    }


    /**
     * Endpoint 6 : Effectue un dépôt sur un compte
     * <p>
     * TODO : Ajouter @PostMapping("/accounts/{accountNumber}/deposit")
     * TODO : Recevoir le montant dans le body (Map<String, Double>)
     * TODO : Extraire "amount" de la Map
     * TODO : Gérer les erreurs (compte inexistant, montant invalide)
     */

    @PostMapping("/accounts/{accountNumber}/deposit")
    public ResponseEntity<Account> deposit(
            @PathVariable String accountNumber,
            @RequestBody Map<String, Double> request) {

        if (!request.containsKey("amount")) {
            return ResponseEntity.badRequest().build(); // 400 si pa gen amount
        }

        double amount = request.get("amount");

        try {
            Account updatedAccount = atmService.deposit(accountNumber, amount);
            return ResponseEntity.ok(updatedAccount); // 200 OK
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // 400 si erè (eg. kont pa egziste, montant negatif)
        }
    }

    /**
     * Endpoint 7 : Effectue un retrait sur un compte
     * <p>
     * TODO : Ajouter @PostMapping("/accounts/{accountNumber}/withdraw")
     * TODO : Similaire au dépôt, mais avec withdraw()
     * TODO : Gérer le cas de solde insuffisant
     */
    @PostMapping("/accounts/{accountNumber}/withdraw")
    public ResponseEntity<Account> withdraw(
            @PathVariable String accountNumber,
            @RequestBody Map<String, Double> request) {

        if (request == null || !request.containsKey("amount")) {
            return ResponseEntity.badRequest().build(); // 400 si pa gen amount
        }

        double amount = request.get("amount");

        // Si kont pa egziste oswa balans < amount, AtmManager ap lanse IllegalArgumentException
        Account updatedAccount = atmService.withdraw(accountNumber, amount);

        return ResponseEntity.ok(updatedAccount); // 200 OK si tout mache
    }

    /**
     * Endpoint 8 : Effectue un virement entre deux comptes
     * <p>
     * TODO : Ajouter @PostMapping("/transfer")
     * TODO : Recevoir from, to, amount dans le body (Map<String, Object>)
     * TODO : Convertir amount en double : ((Number) request.get("amount")).doubleValue()
     * TODO : Retourner un message de succès ou d'erreur
     */
    @PostMapping("/transfer")
    public ResponseEntity<Map<String, String>> transfer(
            @RequestBody Map<String, Object> request) {

        Map<String, String> response = new HashMap<>();

        try {
            String from = (String) request.get("from");
            String to = (String) request.get("to");
            double amount = ((Number) request.get("amount")).doubleValue();

            atmService.transfer(from, to, amount);

            response.put("message", "Transfer successful");
            return ResponseEntity.ok(response); // 200 OK

        } catch (Exception e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response); // 400
        }
    }


    /**
     * Endpoint 9 : Récupère l'historique des transactions d'un compte
     * <p>
     * TODO : Ajouter @GetMapping("/accounts/{accountNumber}/transactions")
     * TODO : Retourner la liste des transactions
     */

    @GetMapping("/accounts/{accountNumber}/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(
            @PathVariable String accountNumber) {

        List<Transaction> transactions = atmService.getTransactions(accountNumber);

        return ResponseEntity.ok(transactions); // 200 OK
    }

    /**
     * Endpoint 10 : Récupère toutes les transactions
     * <p>
     * TODO : Ajouter @GetMapping("/transactions")
     * TODO : Retourner toutes les transactions du système
     */
    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> allTransactions = atmService.getAllTransactions();
        // TODO : Implémenter cette méthode
        return ResponseEntity.ok(allTransactions);
    }

    // ============================================
    // EXERCICES SUPPLÉMENTAIRES (OPTIONNEL)
    // ============================================

    /**
     * EXERCICE 1 : Ajouter un endpoint DELETE
     *
     * @DeleteMapping("/accounts/{accountNumber}") - Supprime un compte
     * - Retourne 200 si succès, 404 si compte inexistant
     * - Note : Vous devrez d'abord ajouter la méthode deleteAccount() dans AtmManager
     */

    @DeleteMapping("/accounts/{accountNumber}")
    public ResponseEntity<String> deleteAccount(@PathVariable String accountNumber) {
        try {
            atmService.deleteAccount(accountNumber);
            return ResponseEntity.ok("Account deleted successfully"); // 200 OK
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Account not found"); // 404 Not Found
        }
    }


    /**
     * EXERCICE 2 : Ajouter un endpoint PUT
     *
     * @PutMapping("/accounts/{accountNumber}/pin") - Modifie le PIN d'un compte
     * - Body : { "oldPin": "1234", "newPin": "5678" }
     * - Vérifier l'ancien PIN avant de changer
     */

    @PutMapping("/accounts/{accountNumber}/pin")
    public ResponseEntity<Map<String, String>> updatePin(
            @PathVariable String accountNumber,
            @RequestBody Map<String, String> request) {

        Map<String, String> response = new HashMap<>();

        String oldPin = request.get("oldPin");
        String newPin = request.get("newPin");

        if (oldPin == null || newPin == null) {
            response.put("error", "Old PIN and new PIN are required");
            return ResponseEntity.badRequest().body(response);
        }

        // Verify si PIN aktyèl la kòrèk
        if (!atmService.verifyPin(accountNumber, oldPin)) {
            response.put("error", "Old PIN is incorrect");
            return ResponseEntity.status(403).body(response); // 403 Forbidden
        }

        // Chanje PIN nan kont lan
        atmService.updatePin(accountNumber, newPin); // Fè metòd sa nan AtmManager

        response.put("message", "PIN updated successfully");
        return ResponseEntity.ok(response); // 200 OK
    }


    /**
     * EXERCICE 3 : Filtrer les transactions
     *
     * @GetMapping("/accounts/{accountNumber}/transactions/deposits") - Retourne uniquement les dépôts
     * - Filtrer les transactions où type = "DEPOT"
     */

    @GetMapping("/accounts/{accountNumber}/transactions/deposits")
    public ResponseEntity<List<Transaction>> getDeposits(@PathVariable String accountNumber) {
        // Ranmase tout tranzaksyon pou kont lan
        List<Transaction> allTransactions = atmService.getTransactions(accountNumber);

        // Filtre pou kenbe sèlman tranzaksyon ki se "DEPOT"
        List<Transaction> deposits = allTransactions.stream()
                .filter(t -> "DEPOT".equals(t.getType()))
                .toList();

        // Retounen lis depo yo
        return ResponseEntity.ok(deposits);
    }


    /**
     * EXERCICE 4 : Statistiques de compte
     *
     * @GetMapping("/accounts/{accountNumber}/stats")
     * - Retourne : nombre de transactions, total dépôts, total retraits, solde actuel
     * - Format : Map<String, Object>
     */
}

