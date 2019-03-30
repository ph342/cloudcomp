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

function login() {
	// if necessary, login

	return new Promise(
        function(resolve, reject) {

            // check if logged in
            var user = firebase.auth().currentUser;

            if (user) {
                resolve(user);
            } else {
                // No user is signed in.
                var provider = new firebase.auth.GoogleAuthProvider();
                firebase.auth().signInWithPopup(provider)
                  .then(function(result) {
                    resolve(result.user);
                }).catch(function(error) {
                    window.location.hash = '#welcome';
                    reject(error.message);
                });
            }
        });
}