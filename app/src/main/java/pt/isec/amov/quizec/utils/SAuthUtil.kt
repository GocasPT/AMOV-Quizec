package pt.isec.amov.quizec.utils

class SAuthUtil {
    companion object {
        private val auth by lazy { }

        // example of usage
        /*viewModel.dbClient.auth.signInWith(Email) {
                email = "batata@gmail.com"
                password = "1234"
            }*/

        /*
        val currentUser: FirebaseUser?
            get() = auth.currentUser

        fun createUserWithEmail(email: String, password: String,
                                onResult: (Throwable?) -> Unit) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { result ->
                    onResult(result.exception)
                }
        }

        fun signInWithEmail(email: String, password: String, onResult: (Throwable?) -> Unit) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { result ->
                    onResult(result.exception)
                }
        }

        fun signOut() {
            if (auth.currentUser != null) {
                auth.signOut()
            }
        }
         */
    }
}