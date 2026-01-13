# 🎓 TP : Création d'un Controller REST pour ATM

## 📝 Objectif du TP

Dans ce TP, vous allez créer de A à Z un **Controller REST** pour gérer les opérations d'un distributeur automatique de billets (ATM). Vous apprendrez à utiliser les annotations Spring pour mapper les requêtes HTTP vers des méthodes Java.

---

## 🎯 Ce que vous allez apprendre

- ✅ Créer un `@RestController`
- ✅ Définir les chemins avec `@RequestMapping`
- ✅ Gérer les requêtes GET avec `@GetMapping`
- ✅ Gérer les requêtes POST avec `@PostMapping`
- ✅ Extraire des paramètres d'URL avec `@PathVariable`
- ✅ Recevoir des données JSON avec `@RequestBody`
- ✅ Retourner les codes HTTP appropriés avec `ResponseEntity`

---

## 📚 Prérequis

Avant de commencer, assurez-vous que :
- ✅ L'application compile : `./mvnw clean compile`
- ✅ Les classes `Account`, `Transaction` et `AtmManager` existent
- ✅ Java 17+ est installé
- ✅ Maven fonctionne

---

## 🏗️ Architecture du projet

```
src/main/java/ht/ueh/first/spring/restatm/
├── models/
│   ├── Account.java         ✅ Déjà fourni
│   └── Transaction.java     ✅ Déjà fourni
├── manager/
│   └── AtmManager.java      ✅ Déjà fourni
└── controllers/
    └── AtmController.java   ❌ À CRÉER (c'est votre mission!)
```

---

## 📋 Étape 1 : Créer la classe du Controller

### Instructions

Vous avez deux options :

#### Option A : Partir de zéro
1. Créez un nouveau fichier Java dans le package `controllers`
2. Nommez-le `AtmController.java`

#### Option B : Utiliser le template (recommandé pour débuter)
1. Copiez le fichier `AtmControllerTemplate.java`
2. Renommez-le en `AtmController.java`
3. Le template contient déjà la structure avec des TODO à compléter

### Code de départ

```java
package ht.ueh.first.spring.restatm.controllers;

import ht.ueh.first.spring.restatm.services.AtmService;

// TODO : Ajouter les annotations @RestController et @RequestMapping
public class AtmController {

    private final AtmService atmManager;

    // TODO : Créer le constructeur pour injecter AtmManager

}
```

### ✍️ Votre mission

Ajoutez les annotations suivantes :

```java
@RestController
@RequestMapping("/api/atm")
```

**Question** : Que signifie `@RestController` ? Quelle est la différence avec `@Controller` ?

**Question** : Quel sera le chemin de base de tous vos endpoints ?

### ✅ Solution

<details>
<summary>Cliquez pour voir la solution</summary>

```java
@RestController
@RequestMapping("/api/atm")
public class AtmController {

    private final AtmManager atmManager;

    public AtmController(AtmManager atmManager) {
        this.atmManager = atmManager;
    }
}
```

</details>

---

## 📋 Étape 2 : Endpoint GET - Liste des comptes

### Mission

Créez un endpoint qui retourne la liste de tous les comptes.

### Spécifications

- **URL** : `GET /api/atm/accounts`
- **Retour** : Liste de tous les comptes (JSON)
- **Code HTTP** : 200 OK

### Code à compléter

```java
// TODO : Ajouter l'annotation @GetMapping
public ResponseEntity<List<Account>> getAllAccounts() {
    // TODO : Appeler atmManager.getAllAccounts()
    // TODO : Retourner ResponseEntity.ok() avec la liste
}
```

### 💡 Indices

- Utilisez `@GetMapping("/accounts")`
- Appelez `atmManager.getAllAccounts()`
- Retournez avec `ResponseEntity.ok(liste)`

### ✅ Solution

<details>
<summary>Cliquez pour voir la solution</summary>

```java
@GetMapping("/accounts")
public ResponseEntity<List<Account>> getAllAccounts() {
    return ResponseEntity.ok(atmManager.getAllAccounts());
}
```

</details>

### 🧪 Test

```bash
curl http://localhost:8080/api/atm/accounts
```

**Résultat attendu** : Liste JSON de 3 comptes

---

## 📋 Étape 3 : Endpoint GET avec paramètre - Détails d'un compte

### Mission

Créez un endpoint qui retourne les détails d'un compte spécifique.

### Spécifications

- **URL** : `GET /api/atm/accounts/{accountNumber}`
- **Paramètre** : accountNumber (dans l'URL)
- **Retour** : Détails du compte (JSON)
- **Code HTTP** : 200 OK si trouvé, 404 Not Found sinon

### Code à compléter

```java
// TODO : Ajouter @GetMapping avec {accountNumber}
public ResponseEntity<Account> getAccount(/* TODO : ajouter @PathVariable */) {
    // TODO : Récupérer le compte avec atmManager
    // TODO : Si null, retourner 404
    // TODO : Sinon retourner 200 avec le compte
}
```

### 💡 Indices

- Le chemin doit contenir `{accountNumber}`
- Utilisez `@PathVariable String accountNumber`
- Testez si le compte est `null`
- `ResponseEntity.notFound().build()` pour 404

### ✅ Solution

<details>
<summary>Cliquez pour voir la solution</summary>

```java
@GetMapping("/accounts/{accountNumber}")
public ResponseEntity<Account> getAccount(@PathVariable String accountNumber) {
    Account account = atmManager.getAccount(accountNumber);
    if (account == null) {
        return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(account);
}
```

</details>

### 🧪 Tests

```bash
# Compte existant
curl http://localhost:8080/api/atm/accounts/123456

# Compte inexistant
curl http://localhost:8080/api/atm/accounts/999999
```

---

## 📋 Étape 4 : Endpoint POST - Créer un compte

### Mission

Créez un endpoint pour créer un nouveau compte.

### Spécifications

- **URL** : `POST /api/atm/accounts`
- **Body** : JSON avec accountNumber, owner, balance, pin
- **Retour** : Le compte créé
- **Code HTTP** : 201 Created si succès, 400 Bad Request si erreur

### Code à compléter

```java
// TODO : Ajouter @PostMapping
public ResponseEntity<Account> createAccount(/* TODO : @RequestBody */) {
    try {
        // TODO : Appeler atmManager.createAccount()
        // TODO : Retourner 201 Created avec le compte
    } catch (IllegalArgumentException e) {
        // TODO : Retourner 400 Bad Request
    }
}
```

### 💡 Indices

- Utilisez `@PostMapping("/accounts")`
- Paramètre : `@RequestBody Account account`
- `ResponseEntity.status(HttpStatus.CREATED).body(created)`
- Gérez l'exception avec try-catch

### ✅ Solution

<details>
<summary>Cliquez pour voir la solution</summary>

```java
@PostMapping("/accounts")
public ResponseEntity<Account> createAccount(@RequestBody Account account) {
    try {
        Account created = atmManager.createAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    }
}
```

</details>

### 🧪 Test

```bash
curl -X POST http://localhost:8080/api/atm/accounts \
  -H "Content-Type: application/json" \
  -d '{
    "accountNumber": "111222",
    "owner": "Nouveau Client",
    "balance": 1500.0,
    "pin": "4321"
  }'
```

---

## 📋 Étape 5 : Endpoint POST - Dépôt d'argent

### Mission

Créez un endpoint pour effectuer un dépôt sur un compte.

### Spécifications

- **URL** : `POST /api/atm/accounts/{accountNumber}/deposit`
- **Body** : JSON avec `amount`
- **Retour** : Le compte mis à jour
- **Code HTTP** : 200 OK, 404 si compte inexistant, 400 si montant invalide

### Code à compléter

```java
// TODO : Ajouter @PostMapping avec le bon chemin
public ResponseEntity<Account> deposit(
        /* TODO : @PathVariable */,
        /* TODO : @RequestBody Map<String, Double> */) {
    try {
        // TODO : Extraire le montant de la Map
        // TODO : Appeler atmManager.deposit()
        // TODO : Gérer le cas null (404)
        // TODO : Retourner 200 avec le compte
    } catch (IllegalArgumentException e) {
        // TODO : Retourner 400
    }
}
```

### 💡 Indices

- Chemin : `"/accounts/{accountNumber}/deposit"`
- Body : `@RequestBody Map<String, Double> request`
- Montant : `request.get("amount")`
- Vérifiez si le compte est null

### ✅ Solution

<details>
<summary>Cliquez pour voir la solution</summary>

```java
@PostMapping("/accounts/{accountNumber}/deposit")
public ResponseEntity<Account> deposit(
        @PathVariable String accountNumber,
        @RequestBody Map<String, Double> request) {
    try {
        double amount = request.get("amount");
        Account account = atmManager.deposit(accountNumber, amount);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(account);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    }
}
```

</details>

### 🧪 Test

```bash
curl -X POST http://localhost:8080/api/atm/accounts/123456/deposit \
  -H "Content-Type: application/json" \
  -d '{"amount": 100.0}'
```

---

## 📋 Étape 6 : Endpoint POST - Retrait d'argent

### Mission

Créez un endpoint similaire au dépôt, mais pour les retraits.

### Spécifications

- **URL** : `POST /api/atm/accounts/{accountNumber}/withdraw`
- **Body** : JSON avec `amount`
- Gérer le cas de solde insuffisant (400 Bad Request)

### ✍️ À vous de jouer !

Créez cet endpoint en vous inspirant de l'endpoint `deposit`.

### ✅ Solution

<details>
<summary>Cliquez pour voir la solution</summary>

```java
@PostMapping("/accounts/{accountNumber}/withdraw")
public ResponseEntity<Account> withdraw(
        @PathVariable String accountNumber,
        @RequestBody Map<String, Double> request) {
    try {
        double amount = request.get("amount");
        Account account = atmManager.withdraw(accountNumber, amount);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(account);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    }
}
```

</details>

---

## 📋 Étape 7 : Endpoint POST - Virement

### Mission

Créez un endpoint pour effectuer un virement entre deux comptes.

### Spécifications

- **URL** : `POST /api/atm/transfer`
- **Body** : JSON avec `from`, `to`, `amount`
- **Retour** : Message de succès

### Code à compléter

```java
// TODO : Ajouter @PostMapping("/transfer")
public ResponseEntity<Map<String, String>> transfer(
        /* TODO : @RequestBody Map<String, Object> */) {
    try {
        // TODO : Extraire from, to, amount
        // TODO : Appeler atmManager.transfer()
        // TODO : Retourner message de succès
    } catch (IllegalArgumentException e) {
        // TODO : Retourner erreur avec message
    }
}
```

### 💡 Indices

- Body contient 3 champs : from (String), to (String), amount (Number)
- Conversion : `((Number) request.get("amount")).doubleValue()`
- Retour : `Map.of("message", "Transfer successful")`

### ✅ Solution

<details>
<summary>Cliquez pour voir la solution</summary>

```java
@PostMapping("/transfer")
public ResponseEntity<Map<String, String>> transfer(@RequestBody Map<String, Object> request) {
    try {
        String from = (String) request.get("from");
        String to = (String) request.get("to");
        double amount = ((Number) request.get("amount")).doubleValue();
        
        atmManager.transfer(from, to, amount);
        return ResponseEntity.ok(Map.of("message", "Transfer successful"));
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
}
```

</details>

### 🧪 Test

```bash
curl -X POST http://localhost:8080/api/atm/transfer \
  -H "Content-Type: application/json" \
  -d '{
    "from": "123456",
    "to": "789012",
    "amount": 200.0
  }'
```

---

## 📋 Étape 8 : Endpoint GET - Historique des transactions

### Mission

Créez un endpoint pour récupérer l'historique des transactions d'un compte.

### Spécifications

- **URL** : `GET /api/atm/accounts/{accountNumber}/transactions`
- **Retour** : Liste des transactions

### ✍️ À vous de jouer !

### ✅ Solution

<details>
<summary>Cliquez pour voir la solution</summary>

```java
@GetMapping("/accounts/{accountNumber}/transactions")
public ResponseEntity<List<Transaction>> getTransactions(@PathVariable String accountNumber) {
    List<Transaction> transactions = atmManager.getTransactions(accountNumber);
    return ResponseEntity.ok(transactions);
}
```

</details>

---

## 🎯 Exercices Supplémentaires

### Exercice 1 : Consulter le solde

Créez un endpoint `GET /api/atm/accounts/{accountNumber}/balance` qui retourne uniquement le solde du compte.

**Retour attendu** :
```json
{
  "accountNumber": "123456",
  "balance": 1000.0
}
```

### Exercice 2 : Vérifier le PIN

Créez un endpoint `POST /api/atm/accounts/{accountNumber}/verify-pin` qui vérifie si un PIN est correct.

**Body** :
```json
{
  "pin": "1234"
}
```

**Retour** :
```json
{
  "valid": true
}
```

### Exercice 3 : Endpoint DELETE

Ajoutez une méthode `deleteAccount()` dans `AtmManager`, puis créez un endpoint DELETE.

### Exercice 4 : Filtrage des transactions

Créez un endpoint pour récupérer uniquement les dépôts d'un compte :
`GET /api/atm/accounts/{accountNumber}/transactions/deposits`

---

## 🧪 Tests Complets

### Scénario de test complet

```bash
# 1. Lister les comptes
curl http://localhost:8080/api/atm/accounts

# 2. Créer un nouveau compte
curl -X POST http://localhost:8080/api/atm/accounts \
  -H "Content-Type: application/json" \
  -d '{"accountNumber": "555555", "owner": "Test User", "balance": 1000.0, "pin": "0000"}'

# 3. Effectuer un dépôt
curl -X POST http://localhost:8080/api/atm/accounts/555555/deposit \
  -H "Content-Type: application/json" \
  -d '{"amount": 500.0}'

# 4. Effectuer un retrait
curl -X POST http://localhost:8080/api/atm/accounts/555555/withdraw \
  -H "Content-Type: application/json" \
  -d '{"amount": 200.0}'

# 5. Consulter l'historique
curl http://localhost:8080/api/atm/accounts/555555/transactions
```

---

## 📊 Checklist de Validation

Avant de considérer votre TP terminé, vérifiez que :

- [ ] Le controller compile sans erreur
- [ ] L'application démarre (`./mvnw spring-boot:run`)
- [ ] `GET /api/atm/accounts` retourne la liste des comptes
- [ ] `GET /api/atm/accounts/{id}` retourne un compte spécifique
- [ ] `GET /api/atm/accounts/{id}` retourne 404 pour un compte inexistant
- [ ] `POST /api/atm/accounts` crée un nouveau compte
- [ ] `POST /api/atm/accounts/{id}/deposit` effectue un dépôt
- [ ] `POST /api/atm/accounts/{id}/withdraw` effectue un retrait
- [ ] Le retrait avec solde insuffisant retourne 400
- [ ] `POST /api/atm/transfer` effectue un virement
- [ ] `GET /api/atm/accounts/{id}/transactions` retourne l'historique

---

## 🎓 Concepts Clés à Retenir

### Annotations
- `@RestController` = `@Controller` + `@ResponseBody`
- `@RequestMapping` définit le chemin de base
- `@GetMapping` pour les lectures (GET)
- `@PostMapping` pour les créations/modifications (POST)

### Paramètres
- `@PathVariable` pour extraire des variables de l'URL
- `@RequestBody` pour recevoir des données JSON

### Codes HTTP
- `200 OK` : Succès
- `201 Created` : Ressource créée
- `400 Bad Request` : Requête invalide
- `404 Not Found` : Ressource non trouvée

---

## 🚀 Prochaines Étapes

Une fois ce TP terminé, vous pouvez :

1. **Ajouter des validations** avec `@Valid`
2. **Documenter l'API** avec Swagger
3. **Ajouter une base de données** avec Spring Data JPA
4. **Sécuriser l'API** avec Spring Security
5. **Écrire des tests** avec MockMvc

---

## 📚 Ressources

- Documentation Spring : https://spring.io/guides
- Guide REST : https://restfulapi.net/
- HTTP Status Codes : https://httpstatuses.com/

---

**Bon courage ! 💪**

Si vous rencontrez des difficultés, consultez le `GUIDE_PEDAGOGIQUE.md` ou demandez de l'aide à votre enseignant.

