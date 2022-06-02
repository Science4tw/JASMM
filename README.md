ANLEITUNG ZUR ANWENDUNG UNSERES WEBSERVICES JASMM 

Allgemeines: 
- Bei unserem Webshop handelt es ich um eine B2B-Lösung. Der Webshop ist deshalb erst nach erfolgreichem Login verfügbar. 
- Unser Lager befindet sich in 4600 Olten (massgebend für die Distanzberechnung bei den Transportkosten). 

Registrierung: 
- Neue Kunden können im Bereich 'Registrieren' ein Benutzerkonto anlegen. 
- Die Registrierung ist nur möglich, wenn kumulativ folgende Punkte erfüllt sind: 
	- Die gewählte E-Mail-Adresse (Benutzername) ist in der Datenbank noch nicht vorhanden. 
	- Das gesetzte Passwort stimmt mit der Passwortwiederholung überein. 
	- Bei der gesetzten PLZ handelt es sich um eine in der Schweiz gültige PLZ. 
	- Alle Felder sind ausgefüllt und überall wurden syntaktisch gültige Eingaben gemacht (Details siehe Punkt 3 Client-seitige Validierungen). 
- Nach erfolgreicher Registrierung erhält der Kunde automatisch eine eindeutige Kundennummer und er/sie kann sich mit dem gesetzten Benutzernamen und Passwort einloggen.  
- Das Passwort wird in der Datenbank verschlüsselt gespeichert (siehe Punkt 3 Passwortverschlüsselung). 
- Nebst den Kundendaten wird auch die Distanz zu Olten in der Datenbank gespeichert (anhand der gesetzten PLZ). 

Login 
- Das Login ist nur bei gültigen Credentials möglich. 
- Nach jedem Login wird im Hintergrund automatisch eine neue Bestellung mit eindeutiger Bestellnummer angelegt. 

Shop: 
- Der Kunde kann die Produkte P1-P4 mit beliebigen Mengen (positive, ganze Zahlen) dem Warenkorb hinzufügen. Negative Zahlen, Zahlen mit Nachkommastellen, leere Werte oder sonstige Zeichen (z.B. Buchstaben) können nicht hinzugefügt werden.  
- Um Produkte aus dem Warenkorb wieder zu entfernen, erfasst der Kunde die Menge 0 beim entsprechenden Produkt (oder lässt das Feld komplett leer) und klickt auf 'In den Warenkorb'. 
- Möchte der Kunde eine weitere Bestellung anlegen (neue Bestellnummer), so ist dies möglich durch Klick auf den Button 'Bestellung absenden' (Button wird aktiv, sobald man für die bestehende Bestellung die Versandkosten berechnet hat). 

Transportkostenberechnung 
- Die Berechnung der Transportkosten erfolgt im Shop durch Klick auf 'Versand berechnen'. Wenn sich keine Produkte im Warenkorb befinden, wird dem Kunden eine Warnmeldung angezeigt.  
- Wir haben das Stapeln der Paletten mit einer max. Höhe von 2200 cm pro Palette umgesetzt.  
- Alle Paletten werden zusammengezählt und ganz zum Schluss gerundet, bspw. 1.2 P + 2.4 P + 0.8 P = 4.4 P --> 5 Paletten 
- Wenn aufgrund der Mengeneingaben die max. Anzahl Paletten (12 Stück) überschritten wird, werden keine Transportkosten berechnet und der Kunde wird aufgefordert, die Bestellmenge zu reduzieren. 

Kundenkonto inkl. Passwort ändern: 
- Im Bereich 'Kundenkonto' kann der Kunde (abgesehen von der Kundennummer und dem Benutzernamen) seine Kundendaten ändern. 
- Für die Inputfelder gelten die gleichen syntaktischen Vorgaben wie im Bereich 'Registrieren' (Details siehe Punkt 3 Client-seitige Validierungen). 
- Die Passwortänderung wird nur dann durchgeführt, wenn kumulativ folgende Punkte erfüllt sind: 
	- Aktuelles Passwort korrekt. 
	- Das neu gesetzte Passwort und die Passwortwiederholung stimmen überein. 
	- Die Passwortrichtlinien werden eingehalten (Details siehe Punkt 3 Client-seitige Validierungen). 
- Das neue Passwort wird wieder verschlüsselt in der Datenbank gespeichert (siehe Punkt 3 Passwortverschlüsselung). 

Logout: 
- Nach dem Logout befindet sich der Kunde wieder auf der Login-Seite. 

ZUSÄTZLICH UMGESETZTE FEATURES 
- Frontend: Diverse Client-seitige Validierungen der Benutzereingaben, so dass nur syntaktisch gültige und vollständige Eingaben an den Server gesendet werden: 
	Registrieren: 
		1. Benutzername: Prüft, ob es vor und nach dem @-Zeichen einen Buchstaben hat. 
	Registrieren & Kundenkonto: 
		1. Vorname, Nachname, Strasse, Ortschaft: Prüft, ob mindestens 2 Zeichen eingegeben wurden. 
		2. Postleitzahl: Prüft, ob es sich um eine vierstellige Zahl handelt. 
	Registrieren & Passwort ändern: 
		1. Passwort & Passwort wiederholen: Prüft, ob mindestens eine Zahl, ein Gross- und Kleinbuchstabe und mindestens 6 oder mehr Zeichen enthalten sind und ob die Passwörter übereinstimmen.  
	Shop: 
		1. Nur Eingabe von Zahlen möglich (Ausnahme ist der Buchstabe e = Eulersche Zahl). 
		2. Eingabe von e und ‘null’ (leeres Feld) wird abgefangen. 
		3. Eingabe von Gleitkommazahlen werden auf ganze Zahlen abgerundet. 
		4. Ein- und Mehrzahl wird bei der Erfolgsmeldung berücksichtigt. 
		5. Hinzufügen (Wert>=1) oder Entfernen (Wert==0) eines Produktes wird bei der Erfolgsmeldung berücksichtigt.  
- Frontend: Ansprechendes Design mittels diverser CSS-Formatierungen. 
- Frontend: Webshop wurde im Responsive Design umgesetzt, so dass sich die Darstellung automatisch an die jeweilige Gerätegrösse anpasst. 
- Backend: Um die Sicherheit zu erhöhen, werden die Passwörter verschlüsselt in der Datenbank gespeichert (mittels Hash-Funktion) 
- Backend: Der JASMM Onlineshop ist über die Domain http://jasmm.ch verfügbar. Es wurde ein kompatibles Package (WAR File) vom Projekt erstellt, auf dem Tomcat Server konfiguriert und installiert.  
- Backend: Die Daten werden auf einer externen MySQL Datenbank abgespeichert. Die MySQL Datenbank läuft auf einem Webserver, somit hat man jederzeit und überall Zugriff auf die Daten. 