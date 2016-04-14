 # Règles de création d'un sujet

## Champs composant un sujet

### Sujet

|     Nom         |      Type           |  Obligatoire                     | Description                        |
| --------------- |: -----------------: | -------------------------------: | ---------------------------------: |
| uuid            |     String          | Oui (généré)                     | id du sujet                        |
| version         |     String          | Oui (généré)                     | numéro de version du sujet         |
| title           |     String          |      Oui                         | Titre du sujet                     |
| description     |     String          |      Oui                         | Description du sujet               |
| maxPoints       |     String          |      Oui (1 par défaut)          | Nombre de point maximum à distribuer pour le vote|
| deadLine        |     Date            |      Non                         | Date limite pour voter             |
| submitDate      |     Date            |      Oui (date du jour)          | Date de création du sujet          |
| collaborateurId |     String          |      Oui (@mail de l'utilisateur)| @mail du créateur du sujet         |
| propositions    |Liste de Propositions|      Oui (au moins 2)            | Liste de propositions pour ce sujet|

### Proposition

|     Nom         |      Type           |  Obligatoire                     | Description                       |
| --------------- |: -----------------: | -------------------------------: | --------------------------------: |
| uuid            |     String          | Oui (généré)                     | id de la proposition              |
| title           |     String          |      Oui                         | Titre de la proposition           |
| description     |     String          |      Non                         | Description de la proposition     |
| points          |     int             |      Non                         | Nombre de points attribués à cette proposition par les votes|      
