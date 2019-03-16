/**
 * 
 */

function login() {
	var username = document.getElementById("username");
	var pass = document.getElementById("password");

	if (username.value == "") {
		resultOutput.style.color = '#0f0'
		resultOutput.innerText = 'Please insert username'
	} else if (pass.value == "") {
		resultOutput.style.color = '#0f0'
		resultOutput.innerText = 'Please insert password'
	} else if (username.value == "admin" && pass.value == "1234") {
		window.location.href = "itemlist.html";
	} else {
		resultOutput.style.color = '#0f0'
		resultOutput.innerText = 'username and paswword do not match'
	}
}
