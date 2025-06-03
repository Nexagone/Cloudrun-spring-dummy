# API de Données Fictives

Cette API Spring Boot génère et expose des données fictives qui évoluent toutes les 5 minutes.

## Fonctionnalités

- Génération automatique de données fictives toutes les 5 minutes
- Endpoint REST pour récupérer les données
- Base de données H2 en mémoire

## Endpoints

- `GET /api/users` - Récupère la liste des utilisateurs fictifs

## Configuration

L'application utilise une base de données H2 en mémoire et tourne sur le port 8080 par défaut.

## Démarrage

1. Clonez le dépôt
2. Exécutez `mvn clean install`
3. Lancez l'application avec `mvn spring-boot:run`
4. Accédez à l'API via `http://localhost:8080/api/users`
5. La console H2 est disponible à `http://localhost:8080/h2-console`

## Intégration avec Google App Engine et Google Sheets

Voici un exemple de code pour Google App Script permettant d'appeler l'API et mettre à jour une feuille Google:

```javascript
function fetchDataAndUpdateSheet() {
  const apiUrl = 'http://votre-api-url/api/users';
  const response = UrlFetchApp.fetch(apiUrl);
  const data = JSON.parse(response.getContentText());

  // Obtenir la feuille active
  const sheet = SpreadsheetApp.getActiveSheet();

  // Ajouter les en-têtes si nécessaire
  if (sheet.getRange(1, 1).getValue() === "") {
    sheet.getRange(1, 1, 1, 5).setValues([["ID", "Nom", "Email", "Entreprise", "Âge"]]);
  }

  // Préparer les données pour la feuille
  const values = data.map(user => [
    user.id,
    user.name,
    user.email,
    user.company,
    user.age
  ]);

  // Mettre à jour la feuille
  sheet.getRange(2, 1, values.length, values[0].length).setValues(values);
}

// Créer un déclencheur pour exécuter la fonction périodiquement
function createTrigger() {
  ScriptApp.newTrigger('fetchDataAndUpdateSheet')
    .timeBased()
    .everyMinutes(5)
    .create();
}
```
