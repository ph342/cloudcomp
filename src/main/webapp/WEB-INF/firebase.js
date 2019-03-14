/**
 * 
 */

// Initialize Firebase
var config = {
	apiKey : "AIzaSyDs2-dJqGcEulxb56LkGV7NuOmKpNujZSc",
	authDomain : "cloudcwk.firebaseapp.com",
	databaseURL : "https://cloudcwk.firebaseio.com",
	projectId : "cloudcwk",
	storageBucket : "cloudcwk.appspot.com",
	messagingSenderId : "69057237839"
};
firebase.initializeApp(config);

var db = firebase.firestore();