package ht.ueh.first.spring.restatm.controllers;

import ht.ueh.first.spring.restatm.manager.AtmManager;
import ht.ueh.first.spring.restatm.models.Account;
import ht.ueh.first.spring.restatm.models.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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
@RequestMapping("api/elendie/atm")

//@RestController est une specialisation de @Controller utilisée pour créer des API rest

/*La difference est que @RestController combine @Controller et @ResponseBody,ce qui signifie que les méthodes
renvoient directement des données (JSON/XML) dans la réponse HTTP, tandis que @Controller est destiné aux
applications MVC classiques qui renvoient des vues (HTML, JSP, Thymeleaf).*/


/*Le chemin de base des endpoints dépend de la façon dont tu déclares ton contrôleur
et éventuellement de la configuration globale*/

public class ElendieAtmController {

    private final AtmManager atmManager;

    /**
     * Constructeur pour l'injection de dépendances
     * Spring injectera automatiquement l'instance d'AtmManager
     */
    public ElendieAtmController(AtmManager atmManager) {
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
        // TODO : Implémenter cette méthode
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
        // TODO : Implémenter cette méthode avec try-catch
    try {
        Account createdAccount = atmManager.createAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }
    catch (IllegalArgumentException e) {
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
        // TODO : Implémenter cette méthode
        double balance = atmManager.getBalance(accountNumber);

        Map<String, Object> response = new HashMap<>();
        response.put("accountNumber", accountNumber);
        response.put("balance", balance);

        return ResponseEntity.ok(response);

    }

    /**
     * Endpoint 5 : Vérifie le PIN d'un compte
     *
     * TODO : Ajouter @PostMapping("/accounts/{accountNumber}/verify-pin")
     * TODO : Recevoir le PIN dans le body (Map<String, String>)
     * TODO : Retourner un Map avec "valid" : true/false
     */
    @PostMapping("/accounts/{accountNumber}/verify-pin")
    public ResponseEntity<Map<String,Boolean>> verifyPin(
            // TODO : Implémenter cette méthode
          @PathVariable  String accountNumber,
         @RequestBody   Map<String, String> requestBody) {
        String pin=requestBody.get("pin");
        boolean isValid = atmManager.verifyPin(accountNumber, pin);
        Map<String, Boolean> response = new HashMap<>();
        response.put("valid", isValid);
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
    public ResponseEntity<Map<String, Object>> deposit(
            @PathVariable    String accountNumber,
           @RequestBody Map<String, Double> requestBody) {
        // TODO : Implémenter cette méthode
        if (!requestBody.containsKey("amount")) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Missing 'amount' in request body"));
        }
        double amount = requestBody.get("amount");

        try {

            Account updatedAccount = atmManager.deposit(accountNumber, amount);


            Map<String, Object> response = new HashMap<>();
            response.put("accountNumber", updatedAccount.getAccountNumber());
            response.put("balance", updatedAccount.getBalance());
            response.put("message", "Deposit successful");

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
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
            // TODO : Implémenter cette méthode
            @PathVariable String accountNumber,
            @RequestBody Map<String, Double> requestBody) {
        try {

            Double amount = requestBody.get("amount");
            Account updatedAccount = atmManager.withdraw(accountNumber, amount);

            return ResponseEntity.ok(updatedAccount);
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
    public ResponseEntity<Map<String, String>> transfer(@RequestBody Map<String, Object> request) {
        // TODO : Implémenter cette méthode
        try {
            // Récupérer les paramètres depuis le body JSON
            String from = (String) request.get("from");
            String to = (String) request.get("to");
            double amount = ((Number) request.get("amount")).doubleValue();

            // Appeler la logique métier
            atmManager.transfer(from, to, amount);

            // Retourner un message de succès
            return ResponseEntity.ok(Map.of(
                    "message", "Transfer successful",
                    "from", from,
                    "to", to,
                    "amount", String.valueOf(amount) // conversion en String
            ));
        } catch (IllegalArgumentException e) {
            // Retourner un message d'erreur clair
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));
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
        // TODO : Implémenter cette méthode
        try {

            List<Transaction> transactions = atmManager.getTransactions(accountNumber);

            return ResponseEntity.ok(transactions);
        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest().build();
        }

    }

    /**
     * Endpoint 10 : Récupère toutes les transactions
     *
     * TODO : Ajouter @GetMapping("/transactions")
     * TODO : Retourner toutes les transactions du système
     */
    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        // TODO : Implémenter cette méthode
        try {

            List<Transaction> transactions = atmManager.getAllTransactions();

            return ResponseEntity.ok(transactions);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
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
}

