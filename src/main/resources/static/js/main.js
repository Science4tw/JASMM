var customerid = -1;

//Michèle
//Registrierung neuer Kunde - createCustomer()
function createCustomer() {
	let usernameInput = document.querySelector('#username');
	let pwdInput = document.querySelector("#pwd");
	let firstNameInput = document.querySelector("#vname");
	let lastNameInput = document.querySelector("#nname");
	let streetInput = document.querySelector('#strasse');
	let streetNrInput = document.querySelector('#hnummer');
	let zipCodeInput = document.querySelector('#plz');
	let cityInput = document.querySelector('#ort'); 
	
	$.ajax({
		type: "POST",
		url: "/demo/createCustomer",
		data: JSON.stringify({ username: usernameInput.value, password: pwdInput.value, firstName: firstNameInput.value, lastName: lastNameInput.value, street: streetInput.value, streetNr: streetNrInput.value, zipCode: zipCodeInput.value, city: cityInput.value}),
		success: responseRegister,
		dataType: 'json',
		contentType: 'application/json'
	});

}

//Michèle
//Registrierung neuer Kunde - Verarbeitung der Server-Antwort
function responseRegister(response) {
	if (response != 0) {
	customerid = response;
	alert("Kundenregistrierung erfolgreich. Die Kunden-ID lautet: " + customerid);
	} else {
		alert("Registrierung fehlgeschlagen. Bitte waehlen Sie einen anderen Benutzernamen.");
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
	if (response == 0) {
		$("#customerId").empty();
		alert("Login fehlgeschlagen.");
	} else {
		$("#customerId").text("Login erfolgreich. Hallo Kunde mit ID " + response + ". Sie koennen nun Bestellungen erfassen und auf Ihr Kundenkonto zugreifen.");
		customerid = response;
		$("#username").empty();
		$("#pwd").empty();
		getCustomer();
	}

}

//Michèle
//Nach erfolgreichem Login: Kundendaten aus DB holen
function getCustomer() {
	$.ajax({
		type: "GET",
		url: "/demo/getCustomer/"+customerid+"",
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
function updateCustomerData(){	
	let pwdInputKto = document.querySelector("#pwdKto");
	let firstNameInputKto = document.querySelector("#vnameKto");
	let lastNameInputKto = document.querySelector("#nnameKto");
	let streetInputKto = document.querySelector('#strasseKto');
	let streetNrInputKto = document.querySelector('#hnummerKto');
	let zipCodeInputKto = document.querySelector('#plzKto');
	let cityInputKto = document.querySelector('#ortKto');
		
	$.ajax({
		type: "PUT",
		url: "/demo/updateCustomer/"+customerid+"",
		data: JSON.stringify({ password: pwdInputKto.value, firstName: firstNameInputKto.value, lastName: lastNameInputKto.value, street: streetInputKto.value, streetNr: streetNrInputKto.value, zipCode: zipCodeInputKto.value,  city: cityInputKto.value}),
		success: handleCustomerUpdateResponse,
		dataType: 'json',
		contentType: 'application/json',
	});	
}

//Michèle
//Kundenkonto: Erfolgsmeldung bzgl. Änderung der Kundendaten 
function handleCustomerUpdateResponse(response) {
	if (response == true) {
		alert("Kundendaten erfolgreich geaendert in der DB.");
	} else {
		alert("Aenderung fehlgeschlagen.")
	}
}

//Michèle
//Logout
function logoutCustomer() {
	customerid = -1;
	alert("Logout erfolgreich. Kunden-ID wurde auf -1 gesetzt.")
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
	
	let articleid = document.getElementById("addToOrder1");
	let article1amount = document.getElementById("orderitem1");
	
	// Liest den Wert aus für die Artikel id und die Menge
	articleid = articleid.value
	amount = article1amount.value;
	
	alert("Article ID : " + articleid + "---Order ID : " + orderid + "----Amount : " + amount);
	
	$.ajax({
		type: "PUT",
  		url: "/api/order/"+orderid+"/addArticle",
  		data: JSON.stringify({ articleid : articleid , amount: amount , orderid: orderid }),
  		success: responseAddArticleToOrder, 
  		dataType: 'json',
  		contentType: 'application/json'
	});
	
	console.log(JSON.stringify({ articleid : articleid , amount: amount , orderid: orderid , customerid: customerid}));
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
	
	let articleid2 = document.getElementById("addToOrder2");
	let articleamount2 = document.getElementById("orderitem2");
	
	// Liest den Wert aus für die Artikel id und die Menge
	articleid2 = articleid2.value
	articleamount2 = articleamount2.value;
	
	alert("Article ID : " + articleid2 + "---Order ID : " + orderid + "----Amount : " + articleamount2);
	
	$.ajax({
		type: "PUT",
  		url: "/api/order/"+orderid+"/addArticle",
  		data: JSON.stringify({ articleid : articleid2 , amount: articleamount2 , orderid: orderid }),
  		success: responseAddArticleToOrder, 
  		dataType: 'json',
  		contentType: 'application/json'
	});
	
	console.log(JSON.stringify({ articleid : articleid2 , amount: articleamount2 , orderid: orderid , customerid: customerid}));
}

// Artikel 3 mit Menge zu einer Bestellung hinzufügen
function addArticleToOrder3() {
	
	let articleid3 = document.getElementById("addToOrder3");
	let articleamount3 = document.getElementById("orderitem3");
	
	// Liest den Wert aus für die Artikel id und die Menge
	articleid3 = articleid3.value
	articleamount3 = articleamount3.value;
	
	alert("Article ID : " + articleid3 + "---Order ID : " + orderid + "----Amount : " + articleamount3);
	
	$.ajax({
		type: "PUT",
  		url: "/api/order/"+orderid+"/addArticle",
  		data: JSON.stringify({ articleid : articleid3 , amount: articleamount3 , orderid: orderid }),
  		success: responseAddArticleToOrder, 
  		dataType: 'json',
  		contentType: 'application/json'
	});
	
	console.log(JSON.stringify({ articleid : articleid3 , amount: articleamount3 , orderid: orderid , customerid: customerid}));
}

// Artikel 4 mit Menge zu einer Bestellung hinzufügen
function addArticleToOrder4() {

	let articleid4 = document.getElementById("addToOrder4");
	let articleamount4 = document.getElementById("orderitem4");
	
	// Liest den Wert aus für die Artikel id und die Menge
	articleid4 = articleid4.value
	articleamount4 = articleamount4.value;
	
	alert("Article ID : " + articleid4 + "---Order ID : " + orderid + "----Amount : " + articleamount4);
	
	$.ajax({
		type: "PUT",
  		url: "/api/order/"+orderid+"/addArticle",
  		data: JSON.stringify({ articleid : articleid4 , amount: articleamount4 , orderid: orderid }),
  		success: responseAddArticleToOrder, 
  		dataType: 'json',
  		contentType: 'application/json'
	});
	
	console.log(JSON.stringify({ articleid : articleid4 , amount: articleamount4 , orderid: orderid , customerid: customerid}));
}
// TEIL MATTHIAS (ENDE)


// TEIL ANDRÉ (START)
function checkEmail(str) {
    var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    if(!re.test(str)) {
    alert("Bitte geben Sie eine gueltige E-Mailadresse ein!");
	document.getElementById("username").value="";
	}
    
}
	
// TEIL ANDRÉ (ENDE)


/*// Alter Code von Sevi

// Put DOM elements into variables
const myForm = document.querySelector('#my-form');
const usernameInput = document.querySelector('#username');
const pwdInput = document.querySelector('#pwd');
const msg = document.querySelector('.msg');
const userList = document.querySelector('#users');

// Listen for form submit
myForm.addEventListener('submit', onSubmit);

function onSubmit(e) {
  e.preventDefault();
  
  if(usernameInput.value === '' || pwdInput.value === '') {
    // alert('Please enter all fields');
    msg.classList.add('error');
    msg.innerHTML = 'Please enter all fields';

    // Remove error after 3 seconds
    setTimeout(() => msg.remove(), 3000);
  } else {
    // Create new list item with user
    const li = document.createElement('li');

    // Add text node with input values
    li.appendChild(document.createTextNode(`${usernameInput.value}: ${pwdInput.value}`));

    // Add HTML
    // li.innerHTML = `<strong>${nameInput.value}</strong>e: ${emailInput.value}`;

    // Append to ul
    userList.appendChild(li);

    // Clear fields
    usernameInput.value = '';
    pwdInput.value = '';
  }
}*/