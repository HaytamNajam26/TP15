# 🏦 Service GraphQL Banking avec Spring Boot

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![GraphQL](https://img.shields.io/badge/GraphQL-Latest-e10098.svg)](https://graphql.org/)
[![H2](https://img.shields.io/badge/H2-Database-yellow.svg)](https://www.h2database.com/)

Un service bancaire moderne développé avec Spring Boot et GraphQL permettant la gestion complète des comptes bancaires et leurs transactions.

## 📋 Table des matières

- [Fonctionnalités](#-fonctionnalités)
- [Technologies utilisées](#-technologies-utilisées)
- [Prérequis](#-prérequis)
- [Installation](#-installation)
- [Configuration](#️-configuration)
- [Utilisation](#-utilisation)
- [Schéma GraphQL](#-schéma-graphql)
- [Exemples de requêtes](#-exemples-de-requêtes)
- [Structure du projet](#-structure-du-projet)
- [Tests](#-tests)
- [Auteur](#-auteur)

## ✨ Fonctionnalités

### Gestion des Comptes
- ✅ Création de comptes bancaires (COURANT, EPARGNE)
- ✅ Consultation de tous les comptes
- ✅ Recherche de compte par identifiant
- ✅ Calcul automatique des statistiques (nombre, somme, moyenne)

### Gestion des Transactions
- ✅ Ajout de transactions (DEPOT, RETRAIT)
- ✅ Consultation des transactions d'un compte
- ✅ Consultation de toutes les transactions
- ✅ Calcul des statistiques globales (dépôts, retraits)

### API GraphQL
- ✅ API GraphQL complète avec queries et mutations
- ✅ Interface GraphiQL intégrée pour les tests
- ✅ Gestion d'erreurs personnalisée
- ✅ Support des variables GraphQL

### Base de données
- ✅ Base de données H2 en mémoire
- ✅ Console H2 pour l'exploration des données
- ✅ Génération automatique du schéma

## 🛠 Technologies utilisées

- **Java 17** - Langage de programmation
- **Spring Boot 3.2.0** - Framework d'application
- **Spring Data JPA** - Persistance des données
- **Spring GraphQL** - Support GraphQL
- **H2 Database** - Base de données en mémoire
- **Lombok** - Réduction du code boilerplate
- **Maven** - Gestion des dépendances

## 📦 Prérequis

- Java 17 ou supérieur
- Maven 3.6+
- Git (optionnel)

## 🚀 Installation

### 1. Cloner le repository (si applicable)

```bash
git clone <votre-repository-url>
cd TP15
```

### 2. Compiler le projet

```bash
mvn clean install
```

### 3. Lancer l'application

```bash
mvn spring-boot:run
```

L'application sera disponible sur **http://localhost:8082**

## ⚙️ Configuration

Le fichier `src/main/resources/application.properties` contient la configuration de l'application :

```properties
# Configuration du serveur
server.port=8082

# Configuration H2
spring.datasource.url=jdbc:h2:mem:banque
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Configuration JPA
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Configuration GraphQL
spring.graphql.graphiql.enabled=true
spring.graphql.path=/graphql
```

## 💻 Utilisation

### Accès aux interfaces

| Interface | URL | Description |
|-----------|-----|-------------|
| **GraphiQL** | http://localhost:8082/graphiql | Interface de test GraphQL interactive |
| **GraphQL Endpoint** | http://localhost:8082/graphql | Point d'accès API GraphQL |
| **Console H2** | http://localhost:8082/h2-console | Interface de la base de données |

### Connexion à H2 Console

- **JDBC URL**: `jdbc:h2:mem:banque`
- **Username**: `sa`
- **Password**: (laisser vide)

## 📊 Schéma GraphQL

### Types principaux

```graphql
enum TypeCompte {
    COURANT
    EPARGNE
}

enum TypeTransaction {
    DEPOT
    RETRAIT
}

type Compte {
    id: ID
    solde: Float
    dateCreation: String
    type: TypeCompte
}

type Transaction {
    id: ID
    montant: Float
    date: String
    type: TypeTransaction
    compte: Compte
}

type SoldeStats {
    count: Int
    sum: Float
    average: Float
}

type TransactionStats {
    count: Int
    sumDepots: Float
    sumRetraits: Float
}
```

### Queries disponibles

```graphql
type Query {
    allComptes: [Compte]
    compteById(id: ID): Compte
    totalSolde: SoldeStats
    compteTransactions(id: ID): [Transaction]
    allTransactions: [Transaction]
    transactionStats: TransactionStats
}
```

### Mutations disponibles

```graphql
type Mutation {
    saveCompte(compte: CompteRequest): Compte
    addTransaction(transaction: TransactionRequest): Transaction
}

input CompteRequest {
    solde: Float
    dateCreation: String
    type: TypeCompte
}

input TransactionRequest {
    compteId: ID
    montant: Float
    date: String
    type: TypeTransaction
}
```

## 📝 Exemples de requêtes

### 1. Récupérer tous les comptes

```graphql
query {
  allComptes {
    id
    solde
    dateCreation
    type
  }
}
```

**Réponse exemple :**
```json
{
  "data": {
    "allComptes": [
      {
        "id": "1",
        "solde": 1500.0,
        "dateCreation": "2024-11-18",
        "type": "COURANT"
      },
      {
        "id": "2",
        "solde": 3000.0,
        "dateCreation": "2024-11-17",
        "type": "EPARGNE"
      }
    ]
  }
}
```

### 2. Récupérer un compte par ID

```graphql
query {
  compteById(id: 1) {
    id
    solde
    dateCreation
    type
  }
}
```

**Réponse exemple :**
```json
{
  "data": {
    "compteById": {
      "id": "1",
      "solde": 1500.0,
      "dateCreation": "2024-11-18",
      "type": "COURANT"
    }
  }
}
```

### 3. Récupérer un compte par ID (avec variable)

```graphql
query($id: ID) {
  compteById(id: $id) {
    id
    solde
    type
  }
}
```

**Variables :**
```json
{
  "id": "1"
}
```

### 4. Obtenir les statistiques des soldes

```graphql
query {
  totalSolde {
    count
    sum
    average
  }
}
```

**Réponse exemple :**
```json
{
  "data": {
    "totalSolde": {
      "count": 2,
      "sum": 4500.0,
      "average": 2250.0
    }
  }
}
```

### 5. Créer un nouveau compte

```graphql
mutation {
  saveCompte(compte: {
    solde: 1500.0
    dateCreation: "2024/11/18"
    type: COURANT
  }) {
    id
    solde
    type
    dateCreation
  }
}
```

**Réponse exemple :**
```json
{
  "data": {
    "saveCompte": {
      "id": "1",
      "solde": 1500.0,
      "type": "COURANT",
      "dateCreation": "2024-11-18"
    }
  }
}
```

### 6. Créer un compte avec variables

```graphql
mutation($compte: CompteRequest) {
  saveCompte(compte: $compte) {
    id
    solde
    type
    dateCreation
  }
}
```

**Variables :**
```json
{
  "compte": {
    "solde": 3000.0,
    "dateCreation": "2024/11/17",
    "type": "EPARGNE"
  }
}
```

### 7. Ajouter un dépôt

```graphql
mutation {
  addTransaction(transaction: {
    compteId: 1
    montant: 500.0
    date: "2024/11/18"
    type: DEPOT
  }) {
    id
    montant
    type
    compte {
      id
    }
  }
}
```

**Réponse exemple :**
```json
{
  "data": {
    "addTransaction": {
      "id": "1",
      "montant": 500.0,
      "type": "DEPOT",
      "compte": {
        "id": "1"
      }
    }
  }
}
```

### 8. Ajouter un retrait

```graphql
mutation {
  addTransaction(transaction: {
    compteId: 1
    montant: 200.0
    date: "2024/11/19"
    type: RETRAIT
  }) {
    id
    montant
    type
    compte {
      id
    }
  }
}
```

### 9. Récupérer les transactions d'un compte

```graphql
query {
  compteTransactions(id: 1) {
    id
    montant
    date
    type
  }
}
```

**Réponse exemple :**
```json
{
  "data": {
    "compteTransactions": [
      {
        "id": "1",
        "montant": 500.0,
        "date": "2024-11-18",
        "type": "DEPOT"
      },
      {
        "id": "2",
        "montant": 200.0,
        "date": "2024-11-19",
        "type": "RETRAIT"
      }
    ]
  }
}
```

### 10. Récupérer toutes les transactions

```graphql
query {
  allTransactions {
    id
    montant
    date
    type
    compte {
      id
      solde
    }
  }
}
```

### 11. Statistiques des transactions

```graphql
query {
  transactionStats {
    count
    sumDepots
    sumRetraits
  }
}
```

**Réponse exemple :**
```json
{
  "data": {
    "transactionStats": {
      "count": 2,
      "sumDepots": 500.0,
      "sumRetraits": 200.0
    }
  }
}
```

### 12. Gestion d'erreur - Compte inexistant

```graphql
query {
  compteById(id: 999) {
    id
    solde
    type
  }
}
```

**Réponse exemple :**
```json
{
  "errors": [
    {
      "message": "Compte 999 not found",
      "locations": null,
      "path": ["compteById"]
    }
  ],
  "data": {
    "compteById": null
  }
}
```

## 📁 Structure du projet

```
TP15/
├── src/
│   ├── main/
│   │   ├── java/ma/projet/graph/
│   │   │   ├── config/
│   │   │   │   └── GraphQLConfig.java
│   │   │   ├── controllers/
│   │   │   │   └── CompteControllerGraphQL.java
│   │   │   ├── entities/
│   │   │   │   ├── Compte.java
│   │   │   │   ├── CompteRequest.java
│   │   │   │   ├── SoldeStats.java
│   │   │   │   ├── Transaction.java
│   │   │   │   ├── TransactionRequest.java
│   │   │   │   ├── TypeCompte.java
│   │   │   │   └── TypeTransaction.java
│   │   │   ├── exception/
│   │   │   │   └── GraphQLExceptionHandler.java
│   │   │   ├── repositories/
│   │   │   │   ├── CompteRepository.java
│   │   │   │   └── TransactionRepository.java
│   │   │   └── GraphApplication.java
│   │   └── resources/
│   │       ├── graphql/
│   │       │   └── schema.graphqls
│   │       └── application.properties
│   └── test/
├── pom.xml
└── README.md
```

## 🧪 Tests

### Tests avec GraphiQL

1. Accédez à http://localhost:8082/graphiql
2. Utilisez les exemples de requêtes ci-dessus
3. Testez les mutations et queries interactivement

### Tests avec Postman

1. Créez une requête POST vers `http://localhost:8082/graphql`
2. Headers : `Content-Type: application/json`
3. Body (raw JSON) :
```json
{
  "query": "query { allComptes { id solde type } }"
}
```

### Ordre recommandé pour les tests

1. **Créer des comptes** : Utilisez les mutations `saveCompte` pour créer 2-3 comptes
2. **Vérifier les comptes** : Utilisez `allComptes` pour voir tous les comptes
3. **Ajouter des transactions** : Utilisez `addTransaction` pour ajouter des dépôts et retraits
4. **Vérifier les transactions** : Utilisez `compteTransactions` et `allTransactions`
5. **Tester les statistiques** : Utilisez `totalSolde` et `transactionStats`
6. **Tester les erreurs** : Testez avec des IDs inexistants

## 🎯 Fonctionnalités avancées

### Gestion des erreurs

L'application inclut un gestionnaire d'erreurs personnalisé (`GraphQLExceptionHandler`) qui :
- Intercepte toutes les exceptions GraphQL
- Retourne des messages d'erreur clairs et explicites
- Formate les erreurs selon le standard GraphQL

### Relations JPA

- **Compte ↔ Transaction** : Relation One-to-Many
- Les transactions sont automatiquement liées à leur compte
- Support des requêtes avec relations imbriquées

### Statistiques automatiques

- Calcul automatique des statistiques sur les soldes
- Calcul automatique des statistiques sur les transactions
- Requêtes optimisées avec des agrégations SQL

## 🔧 Développement

### Compiler le projet

```bash
mvn clean compile
```

### Exécuter les tests

```bash
mvn test
```

### Générer le JAR exécutable

```bash
mvn clean package
java -jar target/graph-0.0.1-SNAPSHOT.jar
```

## 📚 Ressources

- [Documentation Spring GraphQL](https://docs.spring.io/spring-graphql/docs/current/reference/html/)
- [Documentation GraphQL](https://graphql.org/learn/)
- [Documentation Spring Boot](https://spring.io/projects/spring-boot)
- [Documentation H2 Database](https://www.h2database.com/html/main.html)

## 🤝 Contribution

Les contributions sont les bienvenues ! N'hésitez pas à :

1. Fork le projet
2. Créer une branche (`git checkout -b feature/AmazingFeature`)
3. Commit vos changements (`git commit -m 'Add some AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrir une Pull Request

## 📄 Licence

Ce projet est sous licence MIT.

<img width="2088" height="961" alt="Screenshot 2025-12-11 165118" src="https://github.com/user-attachments/assets/c1903bad-ba13-4fbc-86a2-ed2e46d2efa2" />
<img width="2097" height="958" alt="Screenshot 2025-12-11 165134" src="https://github.com/user-attachments/assets/aad44cc2-255f-4dfa-8fbb-ee68a72edaa4" />
<img width="2107" height="1157" alt="Screenshot 2025-12-11 165145" src="https://github.com/user-attachments/assets/224592e0-ec09-4c97-b6d1-f91264b80066" />
<img width="2105" height="902" alt="Screenshot 2025-12-11 165158" src="https://github.com/user-attachments/assets/cba1077a-2172-4ed2-afcd-276caebb7ee0" />
<img width="2098" height="1297" alt="Screenshot 2025-12-11 165225" src="https://github.com/user-attachments/assets/f58954d2-dc44-4485-a64e-5d12687ab154" />
<img width="2126" height="895" alt="Screenshot 2025-12-11 165244" src="https://github.com/user-attachments/assets/7f494469-c35c-49aa-b349-14f5a0188220" />
<img width="2106" height="1074" alt="Screenshot 2025-12-11 165259" src="https://github.com/user-attachments/assets/70e77cb4-ad04-4ba0-a876-e517a485a425" />
<img width="2107" height="1082" alt="Screenshot 2025-12-11 165313" src="https://github.com/user-attachments/assets/0d556ff5-712b-440b-afab-17d29d4962aa" />
<img width="2139" height="1164" alt="Screenshot 2025-12-11 165328" src="https://github.com/user-attachments/assets/9fa3d2e8-d5b1-41ef-b379-825d19d07beb" />













