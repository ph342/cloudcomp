/**
 * 
 */

function login() {
	var username = document.getElementById("username");
	var pass = document.getElementById("password");

	if (username.value == "") {
		alert("Input UserName");
	} else if (pass.value == "") {
		alert("Insert Password");
	} else if (username.value == "admin" && pass.value == "1234") {
		window.location.href = "itemlist.html";
	} else {
		alert("UserName and Password are not match！");
	}
}
