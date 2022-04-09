var userId = -1;

function createCustomer() {
	let usernameInput = document.querySelector('#username');
	let pwdInput = document.querySelector('#pwd');
	alert("Der eingegebene UserName lautet: " + usernameInput.value + ", Passwort: " + pwdInput.value);
	$.ajax({
		type: "POST",
		url: "/demo/createCustomer",
		data: JSON.stringify({ username: usernameInput.value, password: pwdInput.value }),
		success: response,
		dataType: 'json',
		contentType: 'application/json'
	});

}

function response(response) {
	if (response != -1) {
	userId = response;
	alert("Der Kunde wurde gespeichert. Die ID lautet: " + userId);
	} else {
		alert("Fehlgeschlagen");
	}
	
}

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