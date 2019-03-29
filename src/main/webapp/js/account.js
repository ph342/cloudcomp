function login() {
    return new Promise(
        function(resolve, reject) {

            // check if logged in
            var user = firebase.auth().currentUser;

            if (user) {
                resolve(user);
            } else {
                // No user is signed in.
                var provider = new firebase.auth.GoogleAuthProvider();
                firebase.auth().signInWithPopup(provider).then(function(result) {
                    // This gives you a Google Access Token. You can use it to
                    // access the Google API.
                    var token = result.credential.accessToken;
                    // The signed-in user info.
                    user = result.user;
                    resolve(user);

                }).catch(function(error) {
                    // Handle Errors here.
                    user = null;
                    window.location.hash = '#welcome';
                    reject(error.message);
                });
            }
        });
}