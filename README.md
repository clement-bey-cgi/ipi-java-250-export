# REMARQUE DE RENDU 

## Un BirtPDFService à la mer !

Le modèle que vous nous avez proposé pour le PDFBirtExportService ne fonctionnait pas chez moi, le fichier se générait et était reçu en front,
mais impossible de l'ouvrir, il était endommagé. Meme avec l'exemple que vous aviez laissé dans le repo. Le code de l'erreur était " Extension registry provider is already set " ET "error.CannotStartupOSGIPlatform". Les sujet que j'ai put trouvé sur le net à ce propos dataient de 2009. 
J'ai essayé de me familiariser avec les reports pour se faire (il est fonctionnel en local ave votre XML de test). 

## Des bonnes nouvelles...

J'ai quand même préparé la méthode pour créer le fichier XML avec le FACTURE DTO (birtPDFService), et essayé de compenser l'échec du birt en me concentrant sur le xlsxService et le pdfService.

## PS 

Mes excuses par avance pour le pdf je l'ai fini tard dans la nuit et j'ai légèrement craqué.