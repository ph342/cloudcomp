function showBasket(){
	// function to display the account shopping basket
	
	// check if logged in
	var user = firebase.auth().currentUser;

	if (!user) {
	  // No user is signed in.
		var provider = new firebase.auth.GoogleAuthProvider();
		firebase.auth().signInWithPopup(provider).then(function(result) {
			  // This gives you a Google Access Token. You can use it to
				// access the Google API.
			  var token = result.credential.accessToken;
			  // The signed-in user info.
			  user = result.user;
		
		}).catch(function(error) {
			// Handle Errors here.
			user = undefined;
			window.location.hash = '#welcome';
			alert(error.message);
			});
	}

if (!user){
	alert('didnt log in');
	return;
}

$.ajax({
	  type: "POST",
	  url: "/Baskets/"+user.uid,
	  dataType: dataType,
	  success: function( data ) {
			$('#center_content').html(basketToHtml(data));
	  }.fail(function() {
		    alert( "Couldn't get basket" );
	  })
	});
}

function showOrders(){
	// display order history of user
}