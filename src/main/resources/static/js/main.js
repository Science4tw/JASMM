var customerid = -1;

//Severin
//Control HTML
$(document).ready(function() {

	// initial das Panel für die Auftragsbearbeitung verstecken
	//$("#NavPanel").hide();
	$("#KontoPanel").hide();
	$("#ShopPanel").hide();
	$("#RegPanel").hide();
	$("#PasswortPanel").hide();
	$("#Shop").hide();


});


//Michèle
//Registrierung neuer Kunde - createCustomer()
function createCustomer() {
	let usernameInput = document.querySelector('#usernamereg'); // Severin geändert von username zu usernamereg
	let pwdInput = document.querySelector("#pwdreg"); //Severin geändert von pwd zu pwdreg
	let pwdInput2 = document.querySelector("#pwdreg2");
	let firstNameInput = document.querySelector("#vname");
	let lastNameInput = document.querySelector("#nname");
	let streetInput = document.querySelector('#strasse');
	let streetNrInput = document.querySelector('#hnummer');
	let zipCodeInput = document.querySelector('#plz');
	let cityInput = document.querySelector('#ort');
	$("#SuccessRegistration").fadeIn(); // André

	if (pwdInput.value === pwdInput2.value) {
		$.ajax({
			type: "POST",
			url: "/createCustomer",
			data: JSON.stringify({ username: usernameInput.value, password: pwdInput.value, firstName: firstNameInput.value, lastName: lastNameInput.value, street: streetInput.value, streetNr: streetNrInput.value, zipCode: zipCodeInput.value, city: cityInput.value }),
			success: responseRegister,
			dataType: 'json',
			contentType: 'application/json'
		});

	} else {
		$('#pwdreg').val(''); //André - Wenn Passwörter nicht übereinstimmen, Textfelder leeren.
		$('#pwdreg2').val(''); // André
		$("#SuccessRegistration").text("Die Eingabe stimmt nicht überein. Bitte versuchen Sie es nochmals.");  //André
		$("#SuccessRegistration").css('color', 'red'); //André
		$("#SuccessRegistration").fadeOut(5000); // André
	}

}


//Michèle
//Registrierung neuer Kunde - Verarbeitung der Server-Antwort
function responseRegister(response) {
	if (response == 0) {
		$('#usernamereg').val(''); // André
		$("#SuccessRegistration").text("Die E-Mail Adresse ist ungültig oder bereits vergeben. Bitte versuchen Sie es nochmals.") //André
		$("#SuccessRegistration").css('color', 'red'); //André
		$("#SuccessRegistration").fadeOut(5000); // André
	} else if (response == -2) {
		$('#plz').val(''); // André
		$("#SuccessRegistration").text("Ungültige Postleitzahl. Bitte versuchen Sie es nochmals.") //André
		$("#SuccessRegistration").css('color', 'red'); //André
		$("#SuccessRegistration").fadeOut(5000); // André

	} else {
		customerid = response;
		$("#SuccessRegistration").text("Ihr Benutzerkonto wurde erstellt. Sie können sich nun einloggen."); //André
		$("#SuccessRegistration").css('color', 'green'); //André
		$('#usernamereg').val(''); //André - Bei erfolgreichem Registrieren werden Eingaben gelöscht.
		$('#pwdreg').val(''); // André
		$('#pwdreg2').val(''); // André
		$('#vname').val(''); // André
		$('#nname').val(''); // André
		$('#strasse').val(''); // André
		$('#hnummer').val(''); // André
		$('#plz').val(''); // André
		$('#ort').val(''); // André

		//window.setTimeout('window.location = "/index.html"', 1000);	//Severin -> Weiterleitung mit Delay
	}

}

//Michèle
//Login des Kunden - loginCustomer()
function loginCustomer() {
	let username = document.querySelector('#username');
	let password = document.querySelector('#pwd');

	$.ajax({
		type: "POST",
		url: "/login",
		data: JSON.stringify({ username: username.value, password: password.value }),
		success: loginResponse,
		dataType: 'json',
		contentType: 'application/json',

	});
}

//Michèle
//Login des Kunden - Verarbeitung der Server-Antwort
function loginResponse(response) {
	var button1 = document.getElementById("LoginLogout") //Severin
	var button2 = document.getElementById("RegKon") //Severin
	var button3 = document.getElementById("Shop") //Severin
	$("#customerId").fadeIn();

	if (response == 0) {
		$("#customerId").empty();
		$('#pwd').val(''); //André - Bei der Fehlerhaften eingabe wird das Passwort und Benutzer geleert.
		$('#username').val(''); // André
		$("#customerId").text("Ihre Anmeldedaten sind nicht korrekt. Bitte versuchen Sie es nochmals."); //André
		$("#customerId").css('color', 'red'); //André
		$("#customerId").fadeOut(2500);

	} else {
		$("#customerId").text("Login erfolgreich. Sie können nun den Shop oder das Kundenkonto aufrufen.");
		$("#customerId").css('color', 'green'); //André
		customerid = response;
		$('#pwd').prop("disabled", true); //André - Nach dem Login werden die Textfelder disabled.
		$('#username').prop("disabled", true); // André
		$('#loginButton').prop('disabled', true);
		getCustomer();
		createOrder();
		$("#Shop").show();
		//$("#NavPanel").show(); //Severin
		//$("#ShopPanel").show(); //Severin
		//$("#LoginPanel").hide(); //Severin
		button1.innerHTML = "Logout"; //Severin
		button2.innerHTML = "Kundenkonto"; //Severin
		button3.innerHTML = "Shop"; // Severin
	}

}

//Michèle
//Nach erfolgreichem Login: Kundendaten aus DB holen
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
//Kundenkonto: Übermittlung der neuen Kundendaten; alle Daten überschreiben (ausser Kunden-ID und Benutzername)
function updateCustomerData() {
	//let pwdInputKto = document.querySelector("#pwdKto");
	let firstNameInputKto = document.querySelector("#vnameKto");
	let lastNameInputKto = document.querySelector("#nnameKto");
	let streetInputKto = document.querySelector('#strasseKto');
	let streetNrInputKto = document.querySelector('#hnummerKto');
	let zipCodeInputKto = document.querySelector('#plzKto');
	let cityInputKto = document.querySelector('#ortKto');
	$("#infoUpdateCustomerData").fadeIn(); // André

	$.ajax({
		type: "PUT",
		url: "/updateCustomer/" + customerid + "",
		data: JSON.stringify({ firstName: firstNameInputKto.value, lastName: lastNameInputKto.value, street: streetInputKto.value, streetNr: streetNrInputKto.value, zipCode: zipCodeInputKto.value, city: cityInputKto.value }),
		success: handleCustomerUpdateResponse,
		dataType: 'json',
		contentType: 'application/json',
	});
}

//Michèle
//Kundenkonto: Erfolgsmeldung bzgl. Änderung der Kundendaten 
function handleCustomerUpdateResponse(response) {
	if (response == true) {
		$("#infoUpdateCustomerData").text("Ihre Kundendaten wurden erfolgreich geändert.") //André
		$("#infoUpdateCustomerData").css('color', 'green'); //André
	} else {
		$('#plzKto').val(''); //André - Wenn PLZ nicht existiert, wird Feld geleert.
		$("#infoUpdateCustomerData").text("Änderung fehlgeschlagen. Bitte geben Sie eine gültige PLZ ein.") //André
		$("#infoUpdateCustomerData").css('color', 'red'); //André
	}
	$("#infoUpdateCustomerData").fadeOut(5000); // André
}

//Michèle
//Passwort ändern
function changePassword() {
	let oldPwdInputUser = document.querySelector("#oldPwd");
	let newPwd1InputUser = document.querySelector("#newPwd1");
	let newPwd2InputUser = document.querySelector("#newPwd2");
	$("#SuccessPasswordChange").fadeIn(); // André

	if (newPwd1InputUser.value === newPwd2InputUser.value) {

		$.ajax({
			type: "PUT",
			url: "/changePassword/" + customerid + "",
			data: JSON.stringify({ password: oldPwdInputUser.value, passwordToUpdate: newPwd1InputUser.value }),
			success: handleChangePwdResponse,
			dataType: 'json',
			contentType: 'application/json'
		});
	} else {
		$('#oldPwd').val(''); //André - Wenn Änderung fehlschlägt, alle drei Felder leeren
		$('#newPwd1').val(''); //André
		$('#newPwd2').val(''); //André
		$("#SuccessPasswordChange").text("Das Passwort stimmt nicht überein. Bitte versuchen Sie es nochmals.") //André
		$("#SuccessPasswordChange").css('color', 'red'); //André
	}
	$("#SuccessPasswordChange").fadeOut(7000); // André
}

//Michèle
//Passwort ändern: Erfolgsmeldung bzgl. Änderung
function handleChangePwdResponse(response) {
	if (response == true) {
		$('#oldPwd').val(''); //André - Wenn Änderung fehlschlägt, alle drei Felder leeren
		$('#newPwd1').val(''); //André
		$('#newPwd2').val('');//André
		$("#SuccessPasswordChange").text("Passwort wurde erfolgreich geändert.")//André
		$("#SuccessPasswordChange").css('color', 'green');//André

	} else {
		$('#oldPwd').val('');//André
		$('#newPwd1').val('');//André
		$('#newPwd2').val('');//André
		$("#SuccessPasswordChange").text("Aktuelles Passwort ist falsch. Bitte versuchen Sie es nochmals.")//André
		$("#SuccessPasswordChange").css('color', 'red');//André
	}
	$("#SuccessPasswordChange").fadeOut(7000); // André
}

//Michèle
//Logout: Klick auf Logout
function logoutCustomer() {

	$.ajax({
		type: "POST",
		url: "/logout",
		data: JSON.stringify({ customerid: customerid }),
		success: logoutResponse,
		dataType: 'json',
		contentType: 'application/json',

	});
	$('#loginButton').prop('disabled', false); // André
	window.setTimeout('window.location = "/index.html"', 1000); //Severin -> Weiterleitung mit Delay
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


// TEIL MATTHIAS (START)

// Anlegen einer neugen Bestellung
function createOrder() {

	let label1 = document.getElementById("p1txt");
	let label2 = document.getElementById("p2txt");
	let label3 = document.getElementById("p3txt");
	let label4 = document.getElementById("p4txt");

	label1.innerHTML = (""); // Severin
	label2.innerHTML = (""); // Severin
	label3.innerHTML = (""); // Severin
	label4.innerHTML = (""); // Severin


	$.ajax({
		type: "POST",
		url: "/createOrder/",
		data: JSON.stringify({ customerid: customerid }),
		success: responseCreateOrder,
		dataType: 'json',
		contentType: 'application/json'
	});

}

// Antwort von createOrder(), wo wir die orderid speichern
function responseCreateOrder(response) {

	orderid = response;
	
	// André - habe den Alert rausgenommen, die Funktion funktioniert
	//alert("Die Order für den Kunden mit der ID " + customerid + " wurde gespeichert. Die OrderID lautet: " + orderid);

}

// let articleid = -1;
// let amount = -1;

function failedAmount() {

	$("#SuccessBasket").text("Ungültige Eingabe. Bitte versuchen Sie es nochmals.") //André
	$("#SuccessBasket").css('color', 'red'); //André
	$("#SuccessBasket").fadeOut(4000); // André - Meldung wird nach 4 sek. ausgeblendet. 

}

function responseAddArticleToOrder(response) {

	$("#SuccessBasket").fadeIn(); // André
	if (response == true) {
		$("#SuccessBasket").text(amountResult + " zum Warenkorb hinzugefügt.")//André
		$("#SuccessBasket").css('color', 'green');//André
	} else {
		$("#SuccessBasket").text("Hinzufügen " + amountResult + " fehlgeschlagen. Bitte versuchen Sie es nochmals.")//André
		$("#SuccessBasket").css('color', 'red');//André
	}

	$("#SuccessBasket").fadeOut(4000); // André - Meldung wird nach 4 sek. ausgeblendet. 
}


// Artikel 1 mit Menge zu einer Bestellung hinzufügen
function addArticleToOrder1() {

	let articleid = 1;//document.getElementById("addToOrder1"); Direkt hier Wert zuweisen
	let article1amount = document.getElementById("orderitem1");
	let label = document.getElementById("p1txt");  //Severin

	// Liest den Wert aus für die Artikel id und die Menge
	//articleid = articleid.value zu 1 geändert, kann man löschen
	amount = article1amount.value;
	amountResult = amount == 1 ? "Uhr" : "Uhren" //André

	$("#SuccessBasket").fadeIn(); // André

	// Von André ausgeklammert:
	//alert("Article ID : " + articleid + "---Order ID : " + orderid + "----Amount : " + amount);

	if (amount > 0) { // André 

		label.innerHTML = (amount + " Stk."); // Severin // von André verschoben

		$.ajax({
			type: "PUT",
			url: "/order/" + orderid + "/addArticle",
			data: JSON.stringify({ articleid: articleid, amount: amount, orderid: orderid, customerid: customerid }),
			success: responseAddArticleToOrder,
			dataType: 'json',
			contentType: 'application/json'
		});


		console.log(JSON.stringify({ articleid: articleid, amount: amount, orderid: orderid, customerid: customerid }));

	} else { // André
		failedAmount(); // André
	}
}



// Artikel 2 mit Menge zu einer Bestellung hinzufügen
function addArticleToOrder2() {

	let articleid2 = 2;//document.getElementById("addToOrder2"); // Direkt hier Wert zuweisen
	let articleamount2 = document.getElementById("orderitem2");
	let label = document.getElementById("p2txt"); //Severin

	// Liest den Wert aus für die Artikel id und die Menge
	//articleid2 = everin articleid2.value  SEVERIN kann man löschen
	articleamount2 = articleamount2.value;
	amountResult = articleamount2 == 1 ? "Rucksack" : "Rucksäcke" //André
	$("#SuccessBasket").fadeIn(); // André

	//André - ausgeklammert
	//alert("Article ID : " + articleid2 + "---Order ID : " + orderid + "----Amount : " + articleamount2);
	if (articleamount2 > 0) { // André 

		label.innerHTML = (articleamount2 + " Stk."); // Severin // von André verschoben

		$.ajax({
			type: "PUT",
			url: "/order/" + orderid + "/addArticle",
			data: JSON.stringify({ articleid: articleid2, amount: articleamount2, orderid: orderid, customerid: customerid }),
			success: responseAddArticleToOrder,
			dataType: 'json',
			contentType: 'application/json'
		});

		console.log(JSON.stringify({ articleid: articleid2, amount: articleamount2, orderid: orderid, customerid: customerid }));

	} else { // André
		failedAmount(); // André
	}
}

// Artikel 3 mit Menge zu einer Bestellung hinzufügen
function addArticleToOrder3() {

	let articleid3 = 3; //document.getElementById("addToOrder3"); // Direkt hier Wert zuweisen
	let articleamount3 = document.getElementById("orderitem3");
	let label = document.getElementById("p3txt");  // Severin

	// Liest den Wert aus für die Artikel id und die Menge
	//articleid3 = articleid3.value SEVERIN kann man löschen
	articleamount3 = articleamount3.value;
	amountResult = articleamount3 == 1 ? "Schuhe" : "Schuhe" //André
	$("#SuccessBasket").fadeIn(); // André

	// André - ausgeklammert
	//alert("Article ID : " + articleid3 + "---Order ID : " + orderid + "----Amount : " + articleamount3);
	if (articleamount3 > 0) { // André 

		label.innerHTML = (articleamount3 + " Stk."); // Severin// von André verschoben
		$.ajax({
			type: "PUT",
			url: "/order/" + orderid + "/addArticle",
			data: JSON.stringify({ articleid: articleid3, amount: articleamount3, orderid: orderid, customerid: customerid }),
			success: responseAddArticleToOrder,
			dataType: 'json',
			contentType: 'application/json'
		});

		console.log(JSON.stringify({ articleid: articleid3, amount: articleamount3, orderid: orderid, customerid: customerid }));

	} else { // André
		failedAmount(); // André
	}
}

// Artikel 4 mit Menge zu einer Bestellung hinzufügen
function addArticleToOrder4() {

	let articleid4 = 4;//document.getElementById("addToOrder4");
	let articleamount4 = document.getElementById("orderitem4");
	let label = document.getElementById("p4txt"); // Severin

	// Liest den Wert aus für die Artikel id und die Menge
	//articleid4 = articleid4.value SEVERIN kann man löschen
	articleamount4 = articleamount4.value;
	amountResult = articleamount4 == 1 ? "Brille" : "Brillen" //André
	$("#SuccessBasket").fadeIn(); // André

	// André - ausgeklammert:
	//alert("Article ID : " + articleid4 + "---Order ID : " + orderid + "----Amount : " + articleamount4);
	if (articleamount4 > 0) { // André 

		label.innerHTML = (articleamount4 + " Stk."); // Severin // von André verschoben

		$.ajax({
			type: "PUT",
			url: "/order/" + orderid + "/addArticle",
			data: JSON.stringify({ articleid: articleid4, amount: articleamount4, orderid: orderid, customerid: customerid }),
			success: responseAddArticleToOrder,
			dataType: 'json',
			contentType: 'application/json'
		});

		console.log(JSON.stringify({ articleid: articleid4, amount: articleamount4, orderid: orderid, customerid: customerid }));
	} else { // André
		failedAmount(); // André
	}
}
// TEIL MATTHIAS (ENDE)


//Severin Versand berechnen Button
//function calcship() {
	//let lbtot = document.getElementById("total"); // Severin
	//lbtot.innerHTML = "CHF" + 100.00; // Severin

//}

// TEIL Julia (START)

function calculateCostOfOrder() {
	
	$.ajax({
		type: "GET",
		url: "/order/" + orderid + "/calculateCostOfOrder/",
		success: handleShippingCostResponse,
		dataType: 'json',
		contentType: 'application/json',

	});
	
	//$.ajax({
		//type: "PUT",
		//url: "/order/" + orderid + "/calculateCostOfOrder/",
		//data: JSON.stringify({ orderid: orderid }),
		//success: responseCalculateCostOfOrder,
		//dataType: 'json',
		//contentType: 'application/json'
	//});
	
}


function handleShippingCostResponse(response) { //responseCalculateCostOfOrder

	if (response == -1) {
		let lbtot = document.getElementById("total");
		lbtot.innerHTML = "Bestellmenge zu gross. Bitte Warenkorb reduzieren";
	} else {
		let lbtot = document.getElementById("total");
		lbtot.innerHTML = "CHF " + parseFloat(response).toFixed(2); // Severin 2.Dezimal	
	}
}

// TEIL Julia (Ende)

