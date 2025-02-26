package com.iyr.ultrachango.auth2


/*
// iosMain
actual class FirebaseAuthProvider {
    actual suspend fun signInWithEmail(email: String, password: String): AuthResult {
        return try {
            val result = FirebaseAuth.auth().signInWithEmailPassword(email, password)
            val user = result?.user ?: return AuthResult.Error("No user found")
            AuthResult.Success(user.toAuthUser())
        } catch (e: Exception) {
            AuthResult.Error(e.localizedMessage ?: "Error desconocido")
        }
    }

    actual suspend fun signUpWithEmail(email: String, password: String): AuthResult {
        return try {
            val result = FirebaseAuth.auth().createUserWithEmailPassword(email, password)
            val user = result?.user ?: return AuthResult.Error("No user found")
            AuthResult.Success(user.toAuthUser())
        } catch (e: Exception) {
            AuthResult.Error(e.localizedMessage ?: "Error desconocido")
        }
    }

    actual suspend fun signInWithGoogle(): AuthResult {
        return AuthResult.Error("No implementado aún")
    }

    actual suspend fun signInWithFacebook(): AuthResult {
        return AuthResult.Error("No implementado aún")
    }

    actual suspend fun signInWithApple(): AuthResult {
        return AuthResult.Error("No implementado aún")
    }

    actual suspend fun signInWithPhone(phoneNumber: String): AuthResult {
        return AuthResult.Error("No implementado aún")
    }

    actual suspend fun signOut() {
        FirebaseAuth.auth().signOut()
    }

    actual suspend fun getCurrentUser(): AuthUser? {
        val user = FirebaseAuth.auth().currentUser
        return user?.toAuthUser()
    }

    private fun FIRUser.toAuthUser(): AuthUser {
        return AuthUser(
            uid = uid(),
            email = email(),
            displayName = displayName(),
            photoUrl = photoURL()?.absoluteString()
        )
    }
}
*/
/*
actual class FirebaseAuthProvider actual constructor() {
    actual suspend fun signInWithEmail(
        email: String,
        password: String
    ): AuthResult {


        TODO("Not yet implemented")
    }

    actual suspend fun signUpWithEmail(
        email: String,
        password: String
    ): AuthResult {
        TODO("Not yet implemented")
    }

    actual suspend fun signInWithGoogle(): AuthResult {
        TODO("Not yet implemented")
    }

    actual suspend fun signInWithFacebook(): AuthResult {
        TODO("Not yet implemented")
    }

    actual suspend fun signInWithApple(): AuthResult {
        TODO("Not yet implemented")
    }

    actual suspend fun signInWithPhone(phoneNumber: String): AuthResult {
        TODO("Not yet implemented")
    }

    actual suspend fun signOut() {
    }

    actual suspend fun getCurrentUser(): AuthUser? {
        TODO("Not yet implemented")
    }
}
*/