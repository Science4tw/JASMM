var customerid = -1;

//Severin
//Control HTML
$(document).ready(function() {

	// initial das Panel für die Auftragsbearbeitung verstecken
	$("#NavPanel").hide();
	$("#KontoPanel").hide();
	$("#ShopPanel").hide();
	$("#RegPanel").hide();

});


//Michèle
//Registrierung neuer Kunde - createCustomer()
function createCustomer() {
	let usernameInput = document.querySelector('#usernamereg'); // Severin geändert von username zu usernamereg
	let pwdInput = document.querySelector("#pwdreg"); //Severin geändert von pwd zu pwdreg
	let firstNameInput = document.querySelector("#vname");
	let lastNameInput = document.querySelector("#nname");
	let streetInput = document.querySelector('#strasse');
	let streetNrInput = document.querySelector('#hnummer');
	let zipCodeInput = document.querySelector('#plz');
	let cityInput = document.querySelector('#ort');

	$.ajax({
		type: "POST",
		url: "/demo/createCustomer",
		data: JSON.stringify({ username: usernameInput.value, password: pwdInput.value, firstName: firstNameInput.value, lastName: lastNameInput.value, street: streetInput.value, streetNr: streetNrInput.value, zipCode: zipCodeInput.value, city: cityInput.value }),
		success: responseRegister,
		dataType: 'json',
		contentType: 'application/json'
	});

}

//Michèle
//Registrierung neuer Kunde - Verarbeitung der Server-Antwort
function responseRegister(response) {
	if (response == 0) {
		alert("Registrierung fehlgeschlagen. Bitte waehlen Sie einen anderen Benutzernamen.");
	} else if (response == -2) {
		alert("Registrierung fehlgeschlagen. Bitt erfassen Sie eine gueltige PLZ.")
	} else {
		customerid = response;
		alert("Kundenregistrierung erfolgreich. Die Kunden-ID lautet: " + customerid);
		window.setTimeout('window.location = "/index.html"', 1000);	//Severin -> Weiterleitung mit Delay
	}

}

//Michèle
//Login des Kunden - loginCustomer()
function loginCustomer() {
	let username = document.querySelector('#username');
	let password = document.querySelector('#pwd');

	$.ajax({
		type: "POST",
		url: "/demo/login",
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
	if (response == 0) {
		$("#customerId").empty();
		alert("Login fehlgeschlagen.");
	} else {
		$("#customerId").text("Login erfolgreich. Hallo Kunde mit ID " + response + ". Sie koennen nun Bestellungen erfassen und auf Ihr Kundenkonto zugreifen.");
		customerid = response;
		$("#username").empty();
		$("#pwd").empty();
		getCustomer();
		createOrder();
		$("#NavPanel").show(); //Severin
		$("#ShopPanel").show(); //Severin
		$("#LoginPanel").hide(); //Severin
		button1.innerHTML = "Shop"; //Severin
		button2.innerHTML = "Kundenkonto"; //Severin
	}

}

//Michèle
//Nach erfolgreichem Login: Kundendaten aus DB holen
function getCustomer() {
	$.ajax({
		type: "GET",
		url: "/demo/getCustomer/" + customerid + "",
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
	let pwdInputKto = document.querySelector("#pwdKto");
	let firstNameInputKto = document.querySelector("#vnameKto");
	let lastNameInputKto = document.querySelector("#nnameKto");
	let streetInputKto = document.querySelector('#strasseKto');
	let streetNrInputKto = document.querySelector('#hnummerKto');
	let zipCodeInputKto = document.querySelector('#plzKto');
	let cityInputKto = document.querySelector('#ortKto');

	$.ajax({
		type: "PUT",
		url: "/demo/updateCustomer/" + customerid + "",
		data: JSON.stringify({ password: pwdInputKto.value, firstName: firstNameInputKto.value, lastName: lastNameInputKto.value, street: streetInputKto.value, streetNr: streetNrInputKto.value, zipCode: zipCodeInputKto.value, city: cityInputKto.value }),
		success: handleCustomerUpdateResponse,
		dataType: 'json',
		contentType: 'application/json',
	});
}

//Michèle
//Kundenkonto: Erfolgsmeldung bzgl. Änderung der Kundendaten 
function handleCustomerUpdateResponse(response) {
	if (response == true) {
		alert("Kundendaten erfolgreich geaendert in der DB");
	} else {
		alert("Aenderung fehlgeschlagen. Bitte geben Sie eine gueltige PLZ ein.")
	}
}

//Michèle
//Logout
function logoutCustomer() {
	customerid = -1;
	alert("Logout erfolgreich. Kunden-ID wurde auf -1 gesetzt.")
	window.setTimeout('window.location = "/index.html"', 1000); //Severin -> Weiterleitung mit Delay
}

//Severin
//Panel Switcher
function switchRegKon() {
	let text = document.getElementById("RegKon").innerHTML;
	var button1 = document.getElementById("LoginLogout") //Severin
	if (text == "Registrieren") {
		$("#RegPanel").show();
		$("#LoginPanel").hide();
		$("#KontoPanel").hide();
		$("#ShopPanel").hide();

	}
	else {
		$("#RegPanel").hide();
		$("#LoginPanel").hide();
		$("#KontoPanel").show();
		button1.innerHTML = "Shop"; //Severin
		$("#ShopPanel").hide();


	}
}

//Severin
//Shop Switcher
function switchShopKon() {
	let text = document.getElementById("LoginLogout").innerHTML;
	if (text == "Logout") {
		$("#RegPanel").hide();
		$("#LoginPanel").show();
		$("#KontoPanel").hide();
		$("#ShopPanel").hide();

	} else if (text == "Shop") {
		$("#RegPanel").hide();
		$("#KontoPanel").hide();
		$("#ShopPanel").show();
		$("#LoginPanel").hide();
	}
	else {
		$("#RegPanel").hide();
		$("#KontoPanel").hide();
		$("#ShopPanel").hide();
		$("#LoginPanel").show();
	}
}




// TEIL MATTHIAS (START)

// Anlegen einer neugen Bestellung
function createOrder() {


	$.ajax({
		type: "POST",
		url: "/api/createOrder/",
		data: JSON.stringify({ customerid: customerid }),
		success: responseCreateOrder,
		dataType: 'json',
		contentType: 'application/json'
	});

}

// Antwort von createOrder(), wo wir die orderid speichern
function responseCreateOrder(response) {

	orderid = response;

	// customerid = response;
	alert("Die Order für den Kunden mit der ID " + customerid + " wurde gespeichert. Die OrderID lautet: " + orderid);

}

// let articleid = -1;
// let amount = -1;


// Artikel 1 mit Menge zu einer Bestellung hinzufügen
function addArticleToOrder1() {

	let articleid = 1;//document.getElementById("addToOrder1"); Direkt hier Wert zuweisen
	let article1amount = document.getElementById("orderitem1");
	let label = document.getElementById("p1txt");  //Severin

	// Liest den Wert aus für die Artikel id und die Menge
	//articleid = articleid.value zu 1 geändert, kann man löschen
	amount = article1amount.value;
	label.innerHTML = (amount + " Stk."); // Severin

	alert("Article ID : " + articleid + "---Order ID : " + orderid + "----Amount : " + amount);

	$.ajax({
		type: "PUT",
		url: "/api/order/" + orderid + "/addArticle",
		data: JSON.stringify({ articleid: articleid, amount: amount, orderid: orderid }),
		success: responseAddArticleToOrder,
		dataType: 'json',
		contentType: 'application/json'
	});

	console.log(JSON.stringify({ articleid: articleid, amount: amount, orderid: orderid, customerid: customerid }));
}

function responseAddArticleToOrder(response) {

	if (response == true) {
		alert("Hinzufügen erfolgreich. Die OrderID lautet: " + orderid)
	} else {
		alert("Fehlgeschlagen. Die OrderID lautet: " + orderid)
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
	label.innerHTML = (articleamount2 + " Stk."); // Severin

	alert("Article ID : " + articleid2 + "---Order ID : " + orderid + "----Amount : " + articleamount2);

	$.ajax({
		type: "PUT",
		url: "/api/order/" + orderid + "/addArticle",
		data: JSON.stringify({ articleid: articleid2, amount: articleamount2, orderid: orderid }),
		success: responseAddArticleToOrder,
		dataType: 'json',
		contentType: 'application/json'
	});

	console.log(JSON.stringify({ articleid: articleid2, amount: articleamount2, orderid: orderid, customerid: customerid }));
}

// Artikel 3 mit Menge zu einer Bestellung hinzufügen
function addArticleToOrder3() {

	let articleid3 = 3; //document.getElementById("addToOrder3"); // Direkt hier Wert zuweisen
	let articleamount3 = document.getElementById("orderitem3");
	let label = document.getElementById("p3txt");  // Severin

	// Liest den Wert aus für die Artikel id und die Menge
	//articleid3 = articleid3.value SEVERIN kann man löschen
	articleamount3 = articleamount3.value;
	label.innerHTML = (articleamount3 + " Stk."); // Severin

	alert("Article ID : " + articleid3 + "---Order ID : " + orderid + "----Amount : " + articleamount3);

	$.ajax({
		type: "PUT",
		url: "/api/order/" + orderid + "/addArticle",
		data: JSON.stringify({ articleid: articleid3, amount: articleamount3, orderid: orderid }),
		success: responseAddArticleToOrder,
		dataType: 'json',
		contentType: 'application/json'
	});

	console.log(JSON.stringify({ articleid: articleid3, amount: articleamount3, orderid: orderid, customerid: customerid }));
}

// Artikel 4 mit Menge zu einer Bestellung hinzufügen
function addArticleToOrder4() {

	let articleid4 = 4;//document.getElementById("addToOrder4");
	let articleamount4 = document.getElementById("orderitem4");
	let label = document.getElementById("p4txt"); // Severin

	// Liest den Wert aus für die Artikel id und die Menge
	//articleid4 = articleid4.value SEVERIN kann man löschen
	articleamount4 = articleamount4.value;
	label.innerHTML = (articleamount4 + " Stk."); // Severin

	alert("Article ID : " + articleid4 + "---Order ID : " + orderid + "----Amount : " + articleamount4);

	$.ajax({
		type: "PUT",
		url: "/api/order/" + orderid + "/addArticle",
		data: JSON.stringify({ articleid: articleid4, amount: articleamount4, orderid: orderid }),
		success: responseAddArticleToOrder,
		dataType: 'json',
		contentType: 'application/json'
	});

	console.log(JSON.stringify({ articleid: articleid4, amount: articleamount4, orderid: orderid, customerid: customerid }));
}
// TEIL MATTHIAS (ENDE)


//Severin Versand berechnen Button
function calcship() {
	let lbtot = document.getElementById("total"); // Severin
	lbtot.innerHTML = "CHF" + 100.00; // Severin

}

function validateUsername() {
	let regexEmail = /^([a-zA-Z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/;
	let erroremail = true;
	
	let formemail = document.getElementsByName('usernamereg')[0];
	formemail.onfocus = function() {
		this.setAttribute('style', 'background: white');
	}
	formemail.onblur = function() {
		if (this.value.match(regexEmail)) {
			this.setAttribute('style', 'background: PaleGreen');
			document.querySelector('.msg.formemail').innerHTML = '';
			document.querySelector('.msg.formemail').setAttribute('style', 'display:none');
			erroremail = false;
		} else {
			this.setAttribute('style', 'background:seashell');
			document.querySelector('.msg.formemail').innerHTML = 'Bitte gültige Emailadresse eingeben!';
			document.querySelector('.msg.formemail').setAttribute('style', 'color:red', 'display:block');
			document.getElementById('usernamereg').value = "";
			erroremail = true;
		}
	}
}

function validatepwd() {
	let regexPw = /^([a-zA-Z1-9äöüßÄÖÜ!"§$%&()=?`]{6,})+$/;
	let errorpw = true;
	
	let formpw = document.getElementsByName('pwdreg')[0];
	formpw.onfocus = function() {
		this.setAttribute('style', 'background: white');
	}
	formpw.onblur = function() {
		if (this.value.match(regexPw)) {
			this.setAttribute('style', 'background: PaleGreen');
			document.querySelector('.msg.formpw').innerHTML = '';
			document.querySelector('.msg.formpw').setAttribute('style', 'display:none');
			errorpw = false;
		} else {
			this.setAttribute('style', 'background:seashell');
			document.querySelector('.msg.formpw').innerHTML = 'Passwort muss mindestens 6 Zeichen lang sein!';
			document.querySelector('.msg.formpw').setAttribute('style', 'color:red', 'display:block');
			document.getElementById('pwdreg').value = "";
			errorpw = true;
		}
	}
}

function validatename() {
	let regexName = /^([ \u00c0-\u01ffa-zA-Z\.' \-]{2,})+$/;
	let errorname = true;

	let formname = document.getElementsByName('vname')[0];
	formname.onfocus = function() {
		this.setAttribute('style', 'background: white');
	}
	formname.onblur = function() {
		if (this.value.match(regexName)) {
			this.setAttribute('style', 'background: PaleGreen');
			document.querySelector('.msg.formname').innerHTML = '';
			document.querySelector('.msg.formname').setAttribute('style', 'display:none');
			errorname = false;
		} else {
			this.setAttribute('style', 'background:seashell');
			document.querySelector('.msg.formname').innerHTML = 'Bitte Vornamen eingeben!';
			document.querySelector('.msg.formname').setAttribute('style', 'color:red', 'display:block');
			document.getElementById('vname').value = "";
			errorname = true;
		}
	}
}

function validatelastname() {
	let regexLastName = /^([ \u00c0-\u01ffa-zA-Z\.' \-]{2,})+$/;
	let errorlastname = true;

	let formlastname = document.getElementsByName('nname')[0];
	formlastname.onfocus = function() {
		this.setAttribute('style', 'background: white');
	}
	formlastname.onblur = function() {
		if (this.value.match(regexLastName)) {
			this.setAttribute('style', 'background: PaleGreen');
			document.querySelector('.msg.formlastname').innerHTML = '';
			document.querySelector('.msg.formlastname').setAttribute('style', 'display:none');
			errorlastname = false;
		} else {
			this.setAttribute('style', 'background:seashell');
			document.querySelector('.msg.formlastname').innerHTML = 'Bitte Nachnamen eingeben!';
			document.querySelector('.msg.formlastname').setAttribute('style', 'color:red', 'display:block');
			document.getElementById('nname').value = "";
			errorlastname = true;
		}
	}
}

function validatestreet() {
	let regexStreet = /^([ \u00c0-\u01ffa-zA-Z\.' \-]{3,})+$/;
	let errorstreet = true;

	let formstreet = document.getElementsByName('strasse')[0];
	formstreet.onfocus = function() {
		this.setAttribute('style', 'background: white');
	}
	formstreet.onblur = function() {
		if (this.value.match(regexStreet)) {
			this.setAttribute('style', 'background: PaleGreen');
			document.querySelector('.msg.formstreet').innerHTML = '';
			document.querySelector('.msg.formstreet').setAttribute('style', 'display:none');
			errorstreet = false;
		} else {
			this.setAttribute('style', 'background:seashell');
			document.querySelector('.msg.formstreet').innerHTML = 'Bitte Strassenname eingeben!';
			document.querySelector('.msg.formstreet').setAttribute('style', 'color:red', 'display:block');
			document.getElementById('strasse').value = "";
			errorstreet = true;
		}
	}
}

function validatehnr() {
	let regexNumber = /^([a-zA-Z1-9]{1,4})$/;
	let errorhnumber = true;

	let formhnumber = document.getElementsByName('hnummer')[0];
	formhnumber.onfocus = function() {
		this.setAttribute('style', 'background: white');
	}
	formhnumber.onblur = function() {
		if (this.value.match(regexNumber)) {
			this.setAttribute('style', 'background: PaleGreen');
			document.querySelector('.msg.formhnumber').innerHTML = '';
			document.querySelector('.msg.formhnumber').setAttribute('style', 'display:none');
			errorhnumber = false;
		} else {
			this.setAttribute('style', 'background:seashell');
			document.querySelector('.msg.formhnumber').innerHTML = 'Bitte Hausnummer eingeben!';
			document.querySelector('.msg.formhnumber').setAttribute('style', 'color:red', 'display:block');
			document.getElementById('hnummer').value = "";
			errorhnumber = true;
		}
	}
}

function validateplz() {
	let regexZip = /^([0-9]{4})$/;
	let errorzip = true;

	let formzip = document.getElementsByName('plz')[0];
	formzip.onfocus = function() {
		this.setAttribute('style', 'background: white');
	}
	formzip.onblur = function() {
		if (this.value.match(regexZip)) {
			this.setAttribute('style', 'background: PaleGreen');
			document.querySelector('.msg.formzip').innerHTML = '';
			document.querySelector('.msg.formzip').setAttribute('style', 'display:none');
			errorzip = false;
		} else {
			this.setAttribute('style', 'background:seashell');
			document.querySelector('.msg.formzip').innerHTML = 'Vierstellige PLZ eingeben!';
			document.querySelector('.msg.formzip').setAttribute('style', 'color:red', 'display:block');
			document.getElementById('plz').value = "";
			errorzip = true;
		}
	}
}

function validateplace() {
	let regexPlace = /^([a-zA-ZäöüßÄÖÜ)]{2,})+$/;
	let errorplace = true;

	let formplace = document.getElementsByName('ort')[0];
	formplace.onfocus = function() {
		this.setAttribute('style', 'background: white');
	}
	formplace.onblur = function() {
		if (this.value.match(regexPlace)) {
			this.setAttribute('style', 'background: PaleGreen');
			document.querySelector('.msg.formplace').innerHTML = '';
			document.querySelector('.msg.formplace').setAttribute('style', 'display:none');
			errorplace = false;
		} else {
			this.setAttribute('style', 'background:seashell');
			document.querySelector('.msg.formplace').innerHTML = 'Bitte Ortschaft eingeben!';
			document.querySelector('.msg.formplace').setAttribute('style', 'color:red', 'display:block');
			document.getElementById('ort').value = "";
			errorplace = true;
		}
	}
}




