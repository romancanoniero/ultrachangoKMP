package com.iyr.ultrachango.utils.firebase

import kotlinx.coroutines.CoroutineScope

interface GoogleAuth {
   suspend fun  signIn(onResult: (FirebaseAuthResult) -> Unit, scope: CoroutineScope)
}

// Declaramos la función que usaremos en `commonMain`
/*
expect fun getGoogleAuth(

): GoogleAuth
*/

//   webClientId = "1077576417175-8b3deus3foi11547ikbjr3plhoi52b6f.apps.googleusercontent.com"