var customerid = -1; // customerid für Logout

// Severin
// Control HTML
$(document).ready(function() {

	// Initial alle Panel verstecken
	$("#KontoPanel").hide();
	$("#ShopPanel").hide();
	$("#RegPanel").hide();
	$("#PasswortPanel").hide();
	$("#Shop").hide();


});


// Michèle & André
// Registrierung neuer Kunde - createCustomer()
function createCustomer() {
	let usernameInput = document.querySelector('#usernamereg'); // Feld aus HTML lesen
	let pwdInput = document.querySelector("#pwdreg"); 
	let pwdInput2 = document.querySelector("#pwdreg2");
	let firstNameInput = document.querySelector("#vname");
	let lastNameInput = document.querySelector("#nname");
	let streetInput = document.querySelector('#strasse');
	let streetNrInput = document.querySelector('#hnummer');
	let zipCodeInput = document.querySelector('#plz');
	let cityInput = document.querySelector('#ort');
	$("#SuccessRegistration").fadeIn(); // Einblenden Meldung

	if (pwdInput.value === pwdInput2.value) { // Wenn Passwort Feld 1 mit Passwort Feld 2 überreinstimmt
		$.ajax({
			type: "POST", // Erstellen eines neuen Kunden
			url: "/createCustomer",
			data: JSON.stringify({ username: usernameInput.value, password: pwdInput.value, firstName: firstNameInput.value, lastName: lastNameInput.value, street: streetInput.value, streetNr: streetNrInput.value, zipCode: zipCodeInput.value, city: cityInput.value }),
			success: responseRegister, 
			dataType: 'json',
			contentType: 'application/json'
		});

	} else {
		$('#pwdreg').val(''); // Wenn Passwörter nicht übereinstimmen, Textfelder leeren.
		$('#pwdreg2').val('');
		$("#SuccessRegistration").text("Die Passworteingabe stimmt nicht überein. Bitte versuchen Sie es nochmals.");  // Meldung ausgeben
		$("#SuccessRegistration").css('color', 'red');
		$("#SuccessRegistration").fadeOut(5000); 
	}

}


// André
// Registrierung neuer Kunde - Verarbeitung der Server-Antwort
function responseRegister(response) {
	if (response == 0) {
		$('#usernamereg').val(''); // Username leeren
		$("#SuccessRegistration").text("Die E-Mail-Adresse ist bereits vergeben. Bitte versuchen Sie es nochmals.") // Fehlermeldung
		$("#SuccessRegistration").css('color', 'red'); // Änderung der Farbe
		$("#SuccessRegistration").fadeOut(5000); // Nachricht nach 5 Sek. ausblenden
	} else if (response == -2) {
		$('#plz').val(''); // PLZ leeren
		$("#SuccessRegistration").text("Ungültige Postleitzahl. Bitte versuchen Sie es nochmals.") // Fehlermeldung
		$("#SuccessRegistration").css('color', 'red');
		$("#SuccessRegistration").fadeOut(5000);

	} else {
		customerid = response;
		$("#SuccessRegistration").text("Ihr Benutzerkonto wurde erstellt. Sie können sich nun einloggen."); //Erfolgsmeldung
		$("#SuccessRegistration").css('color', 'green'); 
		$('#usernamereg').val(''); // Bei erfolgreichem Registrieren werden Eingaben gelöscht.
		$('#pwdreg').val(''); 
		$('#pwdreg2').val(''); 
		$('#vname').val(''); 
		$('#nname').val(''); 
		$('#strasse').val(''); 
		$('#hnummer').val(''); 
		$('#plz').val(''); 
		$('#ort').val(''); 
	}

}

//Michèle
//Login des Kunden - loginCustomer()
function loginCustomer() {
	let username = document.querySelector('#username');
	let password = document.querySelector('#pwd'); 

	$.ajax({
		type: "POST", //sicherer als GET
		url: "/login",
		data: JSON.stringify({ username: username.value, password: password.value }),
		success: loginResponse,
		dataType: 'json',
		contentType: 'application/json',

	});
}

//André & Severin
//Login des Kunden - Verarbeitung der Server-Antwort
function loginResponse(response) {
	var button1 = document.getElementById("LoginLogout")
	var button2 = document.getElementById("RegKon") 
	var button3 = document.getElementById("Shop") 
	$("#customerId").fadeIn(); // Nachricht einblenden

	if (response == 0) {
		$("#customerId").empty();
		$('#pwd').val(''); // Bei der Fehlerhaften eingabe wird das Passwort und Benutzer geleert.
		$('#username').val('');
		$("#customerId").text("Ihre Anmeldedaten sind nicht korrekt. Bitte versuchen Sie es nochmals."); // Fehlermeldung
		$("#customerId").css('color', 'red'); 
		$("#customerId").fadeOut(2500); // Meldung nach 2.5 Sek. ausblenden.

	} else {
		$("#customerId").text("Login erfolgreich. Sie können nun den Shop oder das Kundenkonto aufrufen."); // Erfolgsmeldung
		$("#customerId").css('color', 'green'); 
		customerid = response; // customer ID wird von -1 auf 1 gesetzt.
		$('#pwd').prop("disabled", true); // Nach dem Login werden die Textfelder disabled.
		$('#username').prop("disabled", true);
		$('#loginButton').prop('disabled', true); // Button nicht mehr klickbar
		getCustomer();	// Kundendaten werden ins Kundenkontopannel eingefühgt
		createOrder(); // Bestellung wird angelegt
		$("#Shop").show(); // Button Shop einblenden
		button1.innerHTML = "Logout"; // Menübutton unbenennen
		button2.innerHTML = "Kundenkonto";
		button3.innerHTML = "Shop";
	}

}

//Michèle
//Nach erfolgreichem Login: Kundendaten aus DB holen - getCustomer()
function getCustomer() {
	$.ajax({
		type: "GET",
		url: "/getCustomer/" + customerid + "",
		success: handleCustomerDataResponse,
		dataType: 'json',
		contentType: 'application/json',

	});

}

//Michèle
//Nach erfolgreichem Login: Kundendaten im Kundenkonto anzeigen
function handleCustomerDataResponse(customer) { 
	$("#customeridKto").val(customer['customerid']);
	$("#usernameKto").val(customer['username']);
	$("#pwdKto").val(customer['password']);
	$("#vnameKto").val(customer['firstName']);
	$("#nnameKto").val(customer['lastName']);
	$("#strasseKto").val(customer['street']);
	$("#hnummerKto").val(customer['streetNr']);
	$("#plzKto").val(customer['zipCode']);
	$("#ortKto").val(customer['city']);

}

//Michèle
//Kundenkonto: Übermittlung der neuen Kundendaten; alle Daten überschreiben (ausser Kunden-ID und Benutzername) - updateCustomerData()
function updateCustomerData() {
	let firstNameInputKto = document.querySelector("#vnameKto");
	let lastNameInputKto = document.querySelector("#nnameKto");
	let streetInputKto = document.querySelector('#strasseKto');
	let streetNrInputKto = document.querySelector('#hnummerKto');
	let zipCodeInputKto = document.querySelector('#plzKto');
	let cityInputKto = document.querySelector('#ortKto');
	$("#infoUpdateCustomerData").fadeIn(); // Erfolgsmeldung anzeigen

	$.ajax({
		type: "PUT", //Mutieren eines Kunden
		url: "/updateCustomer/" + customerid + "",
		data: JSON.stringify({ firstName: firstNameInputKto.value, lastName: lastNameInputKto.value, street: streetInputKto.value, streetNr: streetNrInputKto.value, zipCode: zipCodeInputKto.value, city: cityInputKto.value }),
		success: handleCustomerUpdateResponse,
		dataType: 'json',
		contentType: 'application/json',
	});
}

//André
//Kundenkonto: Erfolgs- od. Fehlermeldung bzgl. Änderung der Kundendaten 
function handleCustomerUpdateResponse(response) {
	if (response == true) {
		$("#infoUpdateCustomerData").text("Ihre Kundendaten wurden erfolgreich geändert.")
		$("#infoUpdateCustomerData").css('color', 'green');
	} else {
		$('#plzKto').val(''); // Wenn PLZ nicht existiert, wird Feld geleert.
		$("#infoUpdateCustomerData").text("Änderung fehlgeschlagen. Bitte geben Sie eine gültige PLZ ein.") 
		$("#infoUpdateCustomerData").css('color', 'red'); 
	}
	$("#infoUpdateCustomerData").fadeOut(5000); // Ausblenden nach 5 Sek.
}

//Michèle & André
//Passwort ändern - changePassword()
function changePassword() {
	let oldPwdInputUser = document.querySelector("#oldPwd");
	let newPwd1InputUser = document.querySelector("#newPwd1");
	let newPwd2InputUser = document.querySelector("#newPwd2");
	$("#SuccessPasswordChange").fadeIn(); // Meldung anzeigen

	if (newPwd1InputUser.value === newPwd2InputUser.value) { // Fehleingabe abfangen von Passwort

		$.ajax({
			type: "PUT", //Mutieren eines Kunden (sicherer als GET)
			url: "/changePassword/" + customerid + "",
			data: JSON.stringify({ password: oldPwdInputUser.value, passwordToUpdate: newPwd1InputUser.value }),
			success: handleChangePwdResponse,
			dataType: 'json',
			contentType: 'application/json'
		});
	} else {
		$('#oldPwd').val(''); // Wenn Änderung fehlschlägt, alle drei Felder leeren
		$('#newPwd1').val(''); 
		$('#newPwd2').val(''); 
		$("#SuccessPasswordChange").text("Die Passworteingabe stimmt nicht überein. Bitte versuchen Sie es nochmals.") // Fehlermeldung
		$("#SuccessPasswordChange").css('color', 'red');
	}
	$("#SuccessPasswordChange").fadeOut(7000); // Ausblenden nach 7 Sek. 
}

//André
//Passwort ändern: Erfolgs- bzw. Fehlermeldung bzgl. Änderung
function handleChangePwdResponse(response) {
	if (response == true) {
		$('#oldPwd').val(''); //Wenn Änderung erfolgreich, alle drei Felder leeren
		$('#newPwd1').val('');
		$('#newPwd2').val('');
		$("#SuccessPasswordChange").text("Passwort wurde erfolgreich geändert.")
		$("#SuccessPasswordChange").css('color', 'green');

	} else {
		$('#oldPwd').val('');//Wenn Änderung fehlschlägt, alle drei Felder leeren
		$('#newPwd1').val('');
		$('#newPwd2').val('');
		$("#SuccessPasswordChange").text("Aktuelles Passwort ist falsch. Bitte versuchen Sie es nochmals.")
		$("#SuccessPasswordChange").css('color', 'red');
	}
	$("#SuccessPasswordChange").fadeOut(7000); // Ausblenden nach 7 Sek.
}

//Michèle
//Logout: Klick auf Logout - logoutCustomer()
function logoutCustomer() {

	$.ajax({
		type: "POST",
		url: "/logout",
		data: JSON.stringify({ customerid: customerid }),
		success: logoutResponse,
		dataType: 'json',
		contentType: 'application/json',

	});
	$('#loginButton').prop('disabled', false); // LoginButton wieder klickbar
	window.setTimeout('window.location = "/index.html"', 1000); // Weiterleitung mit Delay
}

//Michèle
//Logout: 
function logoutResponse() {
	customerid = -1;
}

//Severin
//Panel Switcher
function switchRegKon() {
	let text = document.getElementById("RegKon").innerHTML;
	if (text == "Registrieren") {
		$("#RegPanel").show();
		$("#LoginPanel").hide();
		$("#KontoPanel").hide();
		$("#ShopPanel").hide();
		$("#PasswortPanel").hide();

	}
	else {
		$("#RegPanel").hide();
		$("#LoginPanel").hide();
		$("#KontoPanel").show();
		$("#PasswortPanel").show();
		$("#ShopPanel").hide();
	}
}

//Severin
//Panel Switcher
function switchShop() {
	let text = document.getElementById("Shop").innerHTML;
	if (text == "Shop") {
		$("#RegPanel").hide();
		$("#LoginPanel").hide();
		$("#KontoPanel").hide();
		$("#ShopPanel").show();
		$("#PasswortPanel").hide();
	}
}

//Severin
//LoginLogout Switcher
function switchLog() {
	let text = document.getElementById("LoginLogout").innerHTML;
	if (text == "Logout") {
		logoutCustomer();
	} else {
		$("#RegPanel").hide();
		$("#LoginPanel").show();
		$("#KontoPanel").hide();
		$("#PasswortPanel").hide();
		$("#ShopPanel").hide();
	}
}

//Matthias, Severin & André 
//Anlegen einer neugen Bestellung
function createOrder() {

	let label1 = document.getElementById("p1txt");
	let label2 = document.getElementById("p2txt");
	let label3 = document.getElementById("p3txt");
	let label4 = document.getElementById("p4txt");

	label1.innerHTML = ("0 Stk."); 
	label2.innerHTML = ("0 Stk.");
	label3.innerHTML = ("0 Stk."); 
	label4.innerHTML = ("0 Stk.");

	$('#createOrder').prop('disabled', true); // Button disabled

	$.ajax({
		type: "POST",
		url: "/createOrder/",
		data: JSON.stringify({ customerid: customerid }),
		success: responseCreateOrder,
		dataType: 'json',
		contentType: 'application/json'
	});

	if ($("#createOrder").click(function() { // Erst wenn der Button geklickt werden konnte, Bestellung absenden. 
		$(this).data('clicked', true); // Button geklickt = true
		submitOrderSuccessful(); // Setzt Labels zurück, da Bestellung abgesendet und neue Order angelegt.
	}));
}

//Antwort von createOrder(), wo wir die orderid speichern
function responseCreateOrder(response) {

	orderid = response;

}


//André - Meldung bei Falscheingabe einer Menge
function failedAmount() {

	$("#SuccessBasket").text("Ungültige Eingabe. Bitte versuchen Sie es nochmals.");
	$("#SuccessBasket").css('color', 'red'); 
	$("#SuccessBasket").fadeOut(4000); // Meldung wird nach 4 sek. ausgeblendet. 
}

//André 
function responseAddArticleToOrder(response) {

	$("#SuccessBasket").fadeIn(); // Einblenden
	if (response == true) {
		$("#SuccessBasket").text(amountResult + " dem Warenkorb " + amountResult2) // Erfolgsmeldung, hinzugefügt oder entfernt
		$("#SuccessBasket").css('color', 'green');
	} else {
		$("#SuccessBasket").text("Hinzufügen " + amountResult + " fehlgeschlagen. Bitte versuchen Sie es nochmals.")
	}
	$("#SuccessBasket").fadeOut(4000); // Meldung wird nach 4 sek. ausgeblendet. 
}

//André
function submitOrderSuccessful() {
	$("#SuccessBasket").text(""); //Textfeld wird zurückgesetzt

	$('#orderitem1').val('1'); //Labels und total zurücksetzen
	$('#orderitem2').val('1');
	$('#orderitem3').val('1');
	$('#orderitem4').val('1');
	$('#total').text('');

	$("#SuccessSubmit").text("Bestellung erfolgreich versendet. Sie können nun eine neue Bestellung erfassen.");
	$("#SuccessSubmit").css('color', 'green');
	$("#SuccessSubmit").fadeOut(10000); // Erfolgsmeldung nach 10 Sek. ausblenden.
}


//Severin & André 
//Artikel 1 mit Menge zu einer Bestellung hinzufügen
function addArticleToOrder1() {

	let articleid = 1;
	let article1amount = document.getElementById("orderitem1");
	let label = document.getElementById("p1txt"); 
	
	amount = article1amount.value;
	amountResult = amount == 1 ? "Uhr" : "Uhren" // Mehr- oder Einzahl
	amountResult2 = amount == 0 ? "entfernt." : "hinzugefügt."; // Bei 0 gleich true, wird Text auf entfernt gesetzt. Bei false auf hinzuzfügen. 
	$("#SuccessBasket").fadeIn();

	if (amount >= 0 && amount !== "") { // Prüft ob Menge im Feld grösser gleich 0 und nicht leer ist.

		label.innerHTML = (amount + " Stk.");

		$.ajax({
			type: "PUT",
			url: "/order/" + orderid + "/addArticle",
			data: JSON.stringify({ articleid: articleid, amount: amount, orderid: orderid, customerid: customerid }),
			success: responseAddArticleToOrder,
			dataType: 'json',
			contentType: 'application/json'
		});
	} else { 
		failedAmount(); // Fehlermeldung
	}
}


//Severin & André 
//Artikel 2 mit Menge zu einer Bestellung hinzufügen
function addArticleToOrder2() {

	let articleid2 = 2;
	let articleamount2 = document.getElementById("orderitem2");
	let label = document.getElementById("p2txt"); //Severin

	articleamount2 = articleamount2.value;
	amountResult = articleamount2 == 1 ? "Rucksack" : "Rucksäcke" 
	amountResult2 = articleamount2 == 0 ? "entfernt." : "hinzugefügt.";
	$("#SuccessBasket").fadeIn();

	if (articleamount2 >= 0 && articleamount2 !== "") {

		label.innerHTML = (articleamount2 + " Stk."); 

		$.ajax({
			type: "PUT",
			url: "/order/" + orderid + "/addArticle",
			data: JSON.stringify({ articleid: articleid2, amount: articleamount2, orderid: orderid, customerid: customerid }),
			success: responseAddArticleToOrder,
			dataType: 'json',
			contentType: 'application/json'
		});
	} else {
		failedAmount();
	}
}

//Severin & André 
//Artikel 3 mit Menge zu einer Bestellung hinzufügen
function addArticleToOrder3() {

	let articleid3 = 3; 
	let articleamount3 = document.getElementById("orderitem3");
	let label = document.getElementById("p3txt"); 

	articleamount3 = articleamount3.value;
	amountResult = articleamount3 == 1 ? "Schuhe" : "Schuhe" 
	amountResult2 = articleamount3 == 0 ? "entfernt." : "hinzugefügt.";
	$("#SuccessBasket").fadeIn(); 

	if (articleamount3 >= 0 && articleamount3 !== "") { 

		label.innerHTML = (articleamount3 + " Stk."); 
		$.ajax({
			type: "PUT",
			url: "/order/" + orderid + "/addArticle",
			data: JSON.stringify({ articleid: articleid3, amount: articleamount3, orderid: orderid, customerid: customerid }),
			success: responseAddArticleToOrder,
			dataType: 'json',
			contentType: 'application/json'
		});

	} else { 
		failedAmount();
	}
}

//Severin & André 
//Artikel 4 mit Menge zu einer Bestellung hinzufügen
function addArticleToOrder4() {

	let articleid4 = 4;
	let articleamount4 = document.getElementById("orderitem4");
	let label = document.getElementById("p4txt"); 

	articleamount4 = articleamount4.value;
	amountResult = articleamount4 == 1 ? "Brille" : "Brillen";
	amountResult2 = articleamount4 == 0 ? "entfernt." : "hinzugefügt.";
	$("#SuccessBasket").fadeIn(); 

	if (articleamount4 >= 0 && articleamount4 !== "") {

		label.innerHTML = (articleamount4 + " Stk.");

		$.ajax({
			type: "PUT",
			url: "/order/" + orderid + "/addArticle",
			data: JSON.stringify({ articleid: articleid4, amount: articleamount4, orderid: orderid, customerid: customerid }),
			success: responseAddArticleToOrder,
			dataType: 'json',
			contentType: 'application/json'
		});
	} else { 
		failedAmount();
	}
}

//André
function calculateCostOfOrder() {

	$("#total").text("");
	$("#SuccessSubmit").text("");
	$("#SuccessSubmit").fadeIn();
	$("#FailedCalculateOrder").text("");
	$("#FailedCalculateOrder").fadeIn(); 

	if (
		$('#p1txt').text() == '0 Stk.' && 
		$('#p2txt').text() == '0 Stk.' &&
		$('#p3txt').text() == '0 Stk.' &&
		$('#p4txt').text() == '0 Stk.') { // Wenn Menge bei allen Produkten auf 0 - Fehlermeldung ausgeben. 
		failedCalculateOrderMessage()

	} else {
		$.ajax({
			type: "GET",
			url: "/order/" + orderid + "/calculateCostOfOrder/",
			success: handleShippingCostResponse,
			dataType: 'json',
			contentType: 'application/json',

		});
		$("#FailedCalculateOrder").text("")
		$('#createOrder').prop('disabled', false); // Button klickbar
	}
}

//André & Severin
function failedCalculateOrderMessage() {
	
	$("#FailedCalculateOrder").text("Keine Produkte im Warenkorb. Bitte versuchen Sie es nochmals.")
	$("#FailedCalculateOrder").css('color', 'red');
	$('#createOrder').prop('disabled', true);
	let lbtot = document.getElementById("total"); 
	lbtot.innerHTML = ""; //Feld total leeren
	$("#FailedCalculateOrder").fadeOut(4000);
}

// Severin & André
function handleShippingCostResponse(response) {
	
	if (response == -1) { // Wenn Anzahl hoch kommt Wert -1 vom Server
		let lbtot = document.getElementById("total");  // Greift auf Element Label Total zu
		lbtot.innerHTML = "Bestellmenge zu gross. Bitte Warenkorb reduzieren"; // Text wird auf Label Total angezeigt
	} else {
		let lbtot = document.getElementById("total"); // Greift auf Element Label Total zu
		lbtot.innerHTML = "CHF " + parseFloat(response).toFixed(2); // Wert aus respone wird auf Label Total mit zwei Dezimalenzahlen angezeigt 
	}
	$("#total").delay(2000); // Verzögerung, damit User sieht es hat wieder berechnet. 
}

//André
 function checkNumber(element) {
	
        let value;
        value = parseInt(element.value); // Gleitkommazahl wird zu einem Integer umgewandelt. 
        if(isNaN(value)) value = 0; // Prüft ob value (is not a Number = isNaN) eine Nummer ist. Wenn nicht, wird es auf 0 gesetzt. 
        element.value = value; // Wert wird dem element übergeben. 
}