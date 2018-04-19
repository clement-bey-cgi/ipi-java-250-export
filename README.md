# Export

Ajouter des données de tests (des nouveaux clients ou articles ...)
Ajouter de nouvelles colonnes (age, adresse, marque, score, ...)

## CSV
Un fichier CSV est un fichier texte "à plat" chaque ligne correspond à une ligne du tableau, chaque colonne est séparée par des ";"
Le contenu d'une cellule peut être entourée de double quotes
* Exporter la liste des clients au format CSV (colonnes: nom, prénom)
* Bonus ajouter le caractère ; dans le nom d'un des client (ex PETR;ILLO) et corriger le code pour faire en sorte que le fichier CSV reste correct.

## XLSX
```
Workbook workbook = new XSSFWorkbook();
Sheet sheet = workbook.createSheet("Clients");
Row headerRow = sheet.createRow(0);
Cell cellPrenom = headerRow.createCell(0);
cellPrenom.setCellValue("Prénom");
workbook.write(fileOutputStream);
workbook.close();
```
* Créer un export XLSX de tous les clients (colonnes: nom, prénom)
* Créer un export XLSX pour un client, et créer un onglet par facture
    chaque onglet contiendra le détail de la facture (colonnes : désignation, quantité, prixUnitaire, prixLigne) et rajouter le prix total de la facture en bas (utilise un colspan)

## PDF (iText)
* Créer le PDF d'une facture entete : Nom prénom du client, tableau contenant le détail de la facture, prix total de la facture en dessous
Astuce, il faut importer les classe com.itextpdf.* et non com.lowagi.*
AIDE : https://developers.itextpdf.com/examples/itext-action-second-edition/chapter-1

## CSV amélioré (BONUS a faire après)
Créer un objet permettant d'améliorer l'export.
* Créer une classe permettant de réaliser des exports en rassemblant la définition de l'entête et du contenu

## PDF (birt) (BONUS on verra ensemble)
Birt est un outil de templating pour générer des PDF (et autres)
Télécharer birt all in one  http://download.eclipse.org/birt/downloads/
créer un nouveau report
utiliser une datasource XML
* Créer un service de génération de PDF via BIRT : le service utilise un template birt et founit un dataset xml pour créer le PDF.

PS :
Ajouter des bordures, des images, des polices, des couleurs, du gras souligné etc...
