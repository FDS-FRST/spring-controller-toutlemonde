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
 *
 * TODO : Ajouter l'annotation @RestController
 * TODO : Ajouter l'annotation @RequestMapping avec le chemin de base "/api/atm"
 */
@RestController
@RequestMapping("/api/pcharlesatm")
public class PcharlesAtmController {

    private final AtmService atmService;

    /**
     * Constructeur pour l'injection de dépendances
     * Spring injectera automatiquement l'instance d'AtmManager
     */
    public PcharlesAtmController(AtmService atmService) {
        this.atmService = atmService;
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
        return new ResponseEntity<>(atmService.getAllAccounts(), HttpStatus.OK);
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
        // TODO : Implémenter cette méthode
        Account account = atmService.getAccount(accountNumber);
        if(account == null) {
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
        // TODO : Implémenter cette méthode avec try-catch
        try {
            Account accountCreated = atmService.createAccount(account);
            return ResponseEntity.status(HttpStatus.CREATED).body(accountCreated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
        // TODO : Implémenter cette méthode
        try {
            // Appelle le manager pour récupérer le solde du compte spécifié
            double balance = atmService.getBalance(accountNumber);

            // Crée une Map pour construire la réponse JSON
            Map<String, Object> response = new HashMap<>();

            // Ajoute le numéro de compte dans la réponse
            response.put("accountNumber", accountNumber);

            // Ajoute le solde du compte dans la réponse
            response.put("balance", balance);

            // Retourne la Map en JSON avec un code HTTP 200 OK
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            // Si le compte n'existe pas, on renvoie un code HTTP 404 Not Found
            return ResponseEntity.notFound().build();

        } catch (Exception e) {
            // Pour toute autre erreur inattendue, on log l'erreur pour débogage
            e.printStackTrace();

            // Retourne un code HTTP 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
        // 1. Récupérer le PIN envoyé par le client
        String pin = request.get("pin");
        // 2. Vérifier le PIN via le manager
        boolean valid = atmService.verifyPin(accountNumber, pin);
        // 3. Préparer la réponse JSON
        Map<String, Boolean> response = new HashMap<>();
        response.put("valid", valid);
        // 4. Retourner la réponse avec HTTP 200 OK
        // -------------------------------
        return ResponseEntity.ok(response);
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
            // 1. Récupérer le montant à déposer depuis le body JSON
            double amount = request.get("amount");
            // 2. Effectuer le dépôt via le manager
            //    Met à jour le solde du compte et enregistre la transaction
            Account account = atmService.deposit(accountNumber, amount);
            // 3. Retourner le compte mis à jour avec HTTP 200 OK
            return ResponseEntity.ok(account);

        } catch (IllegalArgumentException e) {

            // 4. Cas d'erreur client :
            //    - compte inexistant
            //    - montant invalide (<= 0)
            //    Retourne HTTP 400 Bad Request

            return ResponseEntity.badRequest().build();

        } catch (Exception e) {

            // 5. Toute autre erreur inattendue
            //    Retourne HTTP 500 Internal Server Error

            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
            // Récupérer le montant à retirer depuis le body JSON
            double amount = request.get("amount");

            // Effectuer le retrait via le manager
            // Vérifie le solde, met à jour le compte et enregistre la transaction
            Account account = atmService.withdraw(accountNumber, amount);

            // Retourner le compte mis à jour avec HTTP 200 OK
            return ResponseEntity.ok(account);

        } catch (IllegalArgumentException e) {
            // Cas d'erreur client : compte inexistant, montant invalide ou solde insuffisant
            // Retourne HTTP 400 Bad Request
            return ResponseEntity.badRequest().build();

        } catch (Exception e) {
            // Toute autre erreur inattendue
            // Retourne HTTP 500 Internal Server Error
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
    public ResponseEntity<Map<String, String>> transfer(@RequestBody Map<String, Object> request) {

        try {
            // Récupérer les informations du virement depuis le body JSON
            // "from" = compte source, "to" = compte destination, "amount" = montant du virement
            String from = (String) request.get("from");
            String to = (String) request.get("to");
            double amount = ((Number) request.get("amount")).doubleValue();

            // Effectuer le virement via le manager
            // Vérifie le solde, met à jour les deux comptes et enregistre les transactions
            atmService.transfer(from, to, amount);

            // Préparer la réponse JSON avec un message de succès
            Map<String, String> response = new HashMap<>();
            response.put("message", "Transfer successful");

            // Retourner la réponse avec HTTP 200 OK
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            // Cas d'erreur client : compte inexistant ou solde insuffisant
            // Retourne HTTP 400 Bad Request avec le message d'erreur
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            // Toute autre erreur inattendue
            // Retourne HTTP 500 Internal Server Error
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint 9 : Récupère l'historique des transactions d'un compte
     *
     * TODO : Ajouter @GetMapping("/accounts/{accountNumber}/transactions")
     * TODO : Retourner la liste des transactions
     */
    @GetMapping("/accounts/{accountNumber}/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable String accountNumber) {

        // Récupérer toutes les transactions liées au compte via le manager
        List<Transaction> transactions = atmService.getTransactions(accountNumber);

        // Retourner la liste des transactions (vide si aucune transaction)
        return ResponseEntity.ok(transactions);
    }

    /**
     * Endpoint 10 : Récupère toutes les transactions
     *
     * TODO : Ajouter @GetMapping("/transactions")
     * TODO : Retourner toutes les transactions du système
     */
    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {

        // Récupérer toutes les transactions du système via le manager
        List<Transaction> transactions = atmService.getAllTransactions();

        // Retourner la liste complète des transactions
        return ResponseEntity.ok(transactions);
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

