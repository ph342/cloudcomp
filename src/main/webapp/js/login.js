	var db = firebase.firestore();
	// Email/ Pwd register
	var account = document.getElementById("account");
	var pwd = document.getElementById("pwd");
	var registerSmtBtn = document.getElementById("registerSmtBtn");
	registerSmtBtn.addEventListener("click", function(){
			console.log(account.value);
	    firebase.auth().createUserWithEmailAndPassword(account.value, pwd.value).catch(function(error) {
	    // Handle Errors here.
	    var errorCode = error.code;
	    var errorMsg = error.message;
	    console.log(errorMsg);
	  });
	},false);

	// login
	var accountL = document.getElementById("username");
	var pwdL = document.getElementById("password");
	var loginSmtBtn = document.getElementById("loginSmtBtn");
	loginSmtBtn.addEventListener("click",function(){
		console.log(accountL.value);
		firebase.auth().signInWithEmailAndPassword(accountL.value, pwdL.value).catch(function(error) {
	  	// Handle Errors here.
	  	var errorCode = error.code;
	  	var errorMessage = error.message;
	  	console.log(errorMessage);
	  })
	},false);

	// sign out
	var signoutSmtBtn = document.getElementById("signoutSmtBtn");
	signoutSmtBtn.addEventListener("click",function(){
		firebase.auth().signOut().then(function() {
			console.log("User sign out!");
		}, function(error) {
	  	console.log("User sign out error!");
		})
	},false);

	// Email verify
	var verifyBtn = document.getElementById("verifyBtn");
	verifyBtn.addEventListener("click",function(){
	  user.sendEmailVerification().then(function() {
	    console.log("send verify email");
	  }, function(error) {
	   	console.error("email wrong");
	  });
	},false);

	// change pwd
	var chgPwd = document.getElementById("chgPwd");
	var chgPwdBtn = document.getElementById("chgPwdBtn");
	chgPwdBtn.addEventListener("click",function(){
		firebase.auth().sendPasswordResetEmail(chgPwd.value).then(function() {
	    // Email sent.
	    console.log("send change password email");
	    chgPwd.value = "";
	  }, function(error) {
	    // An error happened.
	    console.error("change password error",error);
	  });
	},false);

	// check current sign in status
	var user;
	firebase.auth().onAuthStateChanged(function(user) {
	  if (user) {
	  	user = user;
	    console.log("User is logined", user)
	  } else {
	  	user = null;
	    console.log("User is not logined yet.");
	  }
	});

