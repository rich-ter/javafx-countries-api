Τελική ατομική άσκηση για το μάθημα java του μεταπτυχιακού προγράμματος 'Προηγμένα Πληροφοριακά Συστήματα'.

still left to finish:

- try -catch exceptions on most properties that might crash
- error messages to the user for certain actions #
- unit tests 
- null pointer exceptions. 

Για την εργασία αυτή χρησιμοποίθηκαν τα εξης εργαλεια:

- Eclipse IDE
- JavaFX maven archetype 0.0.6
- Dependencies:
	Jackson data bind
	Jackson X
	Jackson Y
	
Με βάση τα παραπάνω οι λειτουργίες που αναπτύχθηκαν είναι οι εξής:

1. Μία κλάση POJO /src/main/java/com.richterCCountry.java η δημιουργεί Χ για τα δεδομένα που μας ενδιαφέρουν
2. Μία κλάση CountryService η οποία διαχειρίζεται τα διαφορετικά api calls στα api endpoint του restcountries 
3. Μία κλάση App.java η οποία ξεκινάει το GUI JavaFX