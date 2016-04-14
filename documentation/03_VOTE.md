# Règles de vote

## Champs composant un vote

### Vote

|     Nom         |      Type           |  Obligatoire                     | Description                        |
|-----------------|---------------------|----------------------------------|------------------------------------|
| id              |     String          | Oui (généré)                     | id du vote                         |
| collaborateurId |     String          |      Oui (@mail de l'utilisateur)| @mail du voteur                    |
| choix           |Liste de Choix       |      Non                         | Liste des choix pondérés du voteur |

### Choix

|     Nom         |      Type           |  Obligatoire                     | Description                        |
|-----------------|---------------------|----------------------------------|------------------------------------|
| points          |     int             | Oui                              | nombre de points attribués         |
| propositionId   |     String          | Oui                              | id de la proposition votée         |

### Règles

* Le sujet doit exister
* Le sujet ne doit pas être terminé, ce qui revient à dire que la deadline ne doit pas être dépasée
* L'utilisateur ne peut pas avoir déjà voté
* L'utilisateur ne peut pas avoir déjà donné son pouvoir pour ce sujet
* Le nombre de points distribué ne peut pas excéder le nombre de points maximum du sujet
* Chaque choix voté doit correspondre à une proposition du sujet
* On ne peut pas voter deux fois pour la même proposition (mais on peut lui attribuer plusieurs points)
