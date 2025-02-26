package com.iyr.ultrachango.auth


expect class IFirebaseAuthRepository() {
    fun login(email: String, password: String, callback: (Map<String, Any?>?) -> Unit)
    // ... (resto de las funciones expect)
}


// Clase en código común que usa FirebaseAuthRepository
class AuthManager(private val authRepository: IFirebaseAuthRepository) {

    fun iniciarSesion(email: String, password: String) {
        authRepository.login(email, password) { resultado ->
            // Maneja el resultado del login (que viene de Swift o Android)
            when (resultado) {
                is Map<*, *> -> {
                    val uid = resultado["uid"] as? String
                    val email = resultado["email"] as? String
                    if (uid != null) {
                        println("Usuario logueado: uid=$uid, email=$email")
                        // Haz algo con el usuario logueado
                    } else {
                        println("Error al loguear: datos incorrectos")
                    }
                }
                null -> {
                    println("Error al loguear: resultado nulo")
                }
                else -> {
                    println("Resultado inesperado: $resultado")
                }
            }
        }
    }

    // ... (resto de las funciones)
}