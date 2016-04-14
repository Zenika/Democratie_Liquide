# Règles de délégation

## Champs composant une délégation

### Vote

|     Nom             |      Type           |  Obligatoire                     | Description                         |
|---------------------|---------------------|----------------------------------|-------------------------------------|
| id                  |     String          | Oui (généré)                     | id du vote                          |
| collaborateurIdFrom |     String          |      Oui (@mail de l'utilisateur)| @mail de l'utilisateur              |
| collaborateurIdTo   |     String          |      Oui (@mail)                 | @mail de la personne à qui déléguer |


### Règles

#### Ajout de délégation

* Le sujet doit exister
* Le sujet ne doit pas être terminé, ce qui revient à dire que la deadline ne doit pas être dépasée
* L'utilisateur ne doit pas avoir déjà donné sa délégation
* On ne peut pas déléguer à quelqu'un qui a délégué
* On ne peut se donner son pouvoir à soi-même
* On ne peut pas déléguer si l'on a déjà voté
* On ne peut pas déléguer à quelqu'un qui a déjà voté

#### Suppression de délégation

* Le sujet doit exister
* Le sujet ne doit pas être terminé, ce qui revient à dire que la deadline ne doit pas être dépasée
* La délégation doit avoir été donnée
* La personne à qui l'on a délégué ne doit pas avoir déjà voté


[Index](00_INDEX.md)
