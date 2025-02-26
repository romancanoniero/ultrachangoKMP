package com.iyr.ultrachango.auth

import com.iyr.ultrachango.data.api.cloud.auth.CloudAuthService
import com.iyr.ultrachango.data.api.cloud.users.CloudUsersService
import com.iyr.ultrachango.data.database.repositories.UserRepositoryImpl

import com.iyr.ultrachango.data.models.User
import com.iyr.ultrachango.data.models.enums.AuthenticationMethods
import com.iyr.ultrachango.getUserLocally
import com.iyr.ultrachango.setAuthToken
import com.iyr.ultrachango.storeUserLocally
import com.iyr.ultrachango.utils.firebase.FirebaseAuthRepository
import com.iyr.ultrachango.utils.firebase.FirebaseAuthResult
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


data class LocalAuthResult(
    val success: Boolean,
    val userId: String? = null,
    val errorMessage: String? = null,
    val user: AuthenticatedUser? = null
) {
    constructor(firebaseAuthResult: FirebaseAuthResult) : this(
        success = firebaseAuthResult.success,
        userId = firebaseAuthResult.user?.uid,
        errorMessage = firebaseAuthResult.errorMessage,
        user = firebaseAuthResult.toAuthenticatedUser()
    )
}

@Serializable
data class AuthenticatedUser(
    var uid: String?,
    var displayName: String? = "",
    var firstName: String?,
    var lastName: String?,
    var phoneNumber: String?,
    var email: String?,
    var photoUrl: String?,
    var gender: Int?,
    var birthDate: String?,
    var method: String?,
    var isAnonymous: Boolean? = false,


    ) {


    constructor() : this(null, null, null, null, null, null, null, null, null, null, false)

    constructor(uid: String) : this(
        uid,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        false
    )


    /*
        var uid: String = ""
        var providerId: String? = null
        var displayName: String? = null
        var firstName: String? = null
        var lastName: String? = null
        var phoneNumber: String? = null
        var email: String? = null
        var photoUrl: String? = null
        var gender: Genders? = null
        val birthDate: String? = null

        var isAnonymous: Boolean = false
        // var providerData: MutableList<out UserInfo>

     */
}


class AuthRepository(
    private val apiAuth: CloudAuthService,
    private val apiClient: CloudUsersService,
    private val firebaseAuthRepository: FirebaseAuthRepository,
    private val settings: Settings,
    private val userRepository: UserRepositoryImpl,
    val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default),

    ) {

    init {
        
        
    }

    var user: User? = null

    private val _currentUser = MutableStateFlow<AuthenticatedUser?>(null)
    val currentUser: StateFlow<AuthenticatedUser?> get() = _currentUser


    val currentUserId: String
        get() = firebaseAuthRepository.currentUser?.uid.toString()

    val isLoggedIn: Boolean
        get() = firebaseAuthRepository.isLoggedIn


    val isAuthenticated: Boolean
        get() = true
    //  firebaseAuthRepository.currentUser != null && firebaseAuthRepository.currentUser?.isAnonymous == false

    fun getAuthToken(refresh: Boolean): String? = firebaseAuthRepository.getAuthToken(refresh)


    init {
        //    _currentUser.value = firebaseAuthRepository.currentUser?.toAuthenticatedUser()
        /*
                fetchCurrentUser(
                    callback = { user ->
                        user?.let {
                            _currentUser.value = it
                        }
                    }
                )
        */
    }


    private suspend fun launchWithAwait(block: suspend () -> Unit) {
        scope.async {
            block()
        }.await()
    }

    /*
        suspend fun authenticate(email: String, password: String) {
            launchWithAwait {
                try {
                    val authResult = firebaseAuthRepository.signInWithEmail(email, password)
                    saveSession(
                        firebaseAuthRepository.auth.currentUser?.uid ?: "",
                        firebaseAuthRepository.auth.currentUser?.getIdToken(false) ?: "",
                        userName = "......"
                    )
                } catch (exception: Exception) {
                    // Handle exception
                    try {
                        val authResult = createUser(email, password)
                        val settings: Settings = Settings()
                        settings.putString("user_id", currentUserId ?: "")
                        settings.putString(
                            "auth_token",
                            firebaseAuthRepository.auth.currentUser?.getIdToken(false) ?: ""
                        )
                    } catch (exception: Exception) {
                        val error = exception
                    }
                }
            }
        }
    */


    suspend fun signInWithEmail(email: String, password: String): LocalAuthResult {
        return try {
            val result = firebaseAuthRepository.signInWithEmail(email, password)
            if (result.success) {
                result.user?.let { it ->
                    storeUser(it.toAuthenticatedUser())
                }
                result.authToken?.let { token ->
                    storeAuthToken(token)
                }
            } else {
                throw Exception(result.errorMessage)
            }
            LocalAuthResult(
                success = true, user = result.user?.toAuthenticatedUser()
            )
        } catch (e: Exception) {
            LocalAuthResult(success = false, errorMessage = e.message)
        }
    }

    /**
     * Crea un Usuario con email y password
     */
    suspend fun signUpWithEmail(
        firstName: String?,
        lastName: String?,
        authenticationMethod: String,
        email: String,
        password: String,
        onFailure: (Exception) -> Unit,
        onResult: (AuthenticatedUser) -> Unit,

        ) {

        scope.launch(Dispatchers.IO) {
            // crear el usuario en el autenticador y luego en el servidor
            try {
                var newUser = firebaseAuthRepository.signUpWithEmail(email, password)

                if (newUser.success) {
                    val token = newUser.authToken
                    settings.setAuthToken(token!!)

                    val newAuthenticatedUser = AuthenticatedUser()
                    newUser.user?.let { it ->
                        newAuthenticatedUser.uid = it.uid
                        newAuthenticatedUser.email = email
                        newAuthenticatedUser.displayName = firstName
                        newAuthenticatedUser.firstName = lastName
                        newAuthenticatedUser.lastName = firstName
                        newAuthenticatedUser.isAnonymous = false
                        newAuthenticatedUser.method = authenticationMethod
                    }

                    updateUser(newAuthenticatedUser)
                    onResult(newAuthenticatedUser)
                } else {
                    throw Exception(newUser.errorMessage)
                }
                //           newUser = firebaseAuthRepository.signUpWithEmail(email, password)
                //          auth.createUserWithEmailAndPassword(email, password)
            } catch (e: Exception) {

                onFailure(e)
            }
        }

    }

    suspend fun signUpWithEmail(phoneNumber: String) {
        val result = launchWithAwait {
        }
    }

    suspend fun signInWithGoogle(
        onSuccess: (LocalAuthResult) -> Unit,
        onFailure: (Exception) -> Unit,
        scope: CoroutineScope
    ) {
        var scope = scope
        try {
            firebaseAuthRepository.signInWithGoogle(scope = scope,
                onResult = { response ->
                    var result: LocalAuthResult? = null

                    val token = response.authToken
                    settings.setAuthToken(token!!)

                    val userKey = response.user?.uid
                    userKey?.let {
                        runBlocking {

                            val existingUser = apiAuth.getAuthenticatedUser(userKey)
                            if (existingUser == null) {
                                // si el usuario no existe en el servidor significa que es un nuevo usuario
                                val newAuthenticatedUser = AuthenticatedUser()
                                response.user?.let { it ->
                                    newAuthenticatedUser.uid = it.uid
                                    newAuthenticatedUser.displayName = it.displayName
                                    newAuthenticatedUser.email = it.email
                                    newAuthenticatedUser.phoneNumber = it.phoneNumber
                                    newAuthenticatedUser.photoUrl = it.photoUrl
                                    newAuthenticatedUser.method = AuthenticationMethods.GOOGLE.name
                                    newAuthenticatedUser.isAnonymous = false
                                }
                                updateUser(newAuthenticatedUser)
                                result =
                                    LocalAuthResult(success = true, user = newAuthenticatedUser)
                            } else {
                                result = LocalAuthResult(success = true, user = existingUser)
                                settings.storeUserLocally(existingUser)
                            }
                            onSuccess(result!!)
                        }
                    } ?: run {
                        onFailure(Exception("User not found"))
                    }


                })
        } catch (e: Exception) {
            LocalAuthResult(success = false, errorMessage = e.message)
        }
    }

    suspend fun signInWithPhoneNumber(
        phoneNumber: String,
        onSuccess: (LocalAuthResult) -> Unit,
        onFailure: (Exception) -> Unit,
        scope: CoroutineScope
    )  {

            var toReturn: LocalAuthResult? = null
            //    scope.launch {

            val pp = firebaseAuthRepository.signInWithPhone(
                phoneNumber,
                onSuccess = { result ->
                    if (result.success) {
                        val token = result.authToken
                        settings.setAuthToken(token!!)
                        val userKey = result.user?.uid
                        userKey?.let {
                            runBlocking {
                                val existingUser = apiAuth.getAuthenticatedUser(userKey)
                                if (existingUser == null) {
                                    // si el usuario no existe en el servidor significa que es un nuevo usuario
                                    val newAuthenticatedUser = AuthenticatedUser()
                                    result.user?.let { it ->
                                        newAuthenticatedUser.uid = it.uid
                                        newAuthenticatedUser.displayName = it.displayName
                                        newAuthenticatedUser.email = it.email
                                        newAuthenticatedUser.phoneNumber = it.phoneNumber
                                        newAuthenticatedUser.photoUrl = it.photoUrl
                                        newAuthenticatedUser.method =
                                            AuthenticationMethods.PHONE_NUMBER.name
                                        newAuthenticatedUser.isAnonymous = false
                                    }
                                    updateUser(newAuthenticatedUser)
                                    settings.storeUserLocally(newAuthenticatedUser)
                                    toReturn =
                                        LocalAuthResult(success = true, user = newAuthenticatedUser)
                                } else {
                                    settings.storeUserLocally(existingUser)
                                    toReturn = LocalAuthResult(success = true, user = existingUser)

                                }
                                onSuccess(toReturn!!)
                            }
                                ?: run {
                                    onFailure(Exception("User not found"))
                                }
                        }
                    }
                },
                scope = scope,
            )
            //   return null
            //  }


    }


    suspend fun signOut() {
        firebaseAuthRepository.signOut()
        settings.remove("user_id")
        settings.remove("auth_token")
        //create  new user anonymous session


    }


    fun isUserLoggedIn(): Boolean {
        return settings.getStringOrNull("user_id") != null
    }


    fun getUserKey(): String {
        return firebaseAuthRepository.currentUserId ?: throw Exception("User not logged in")
    }

    fun saveUser(user: User) {
        settings.putString("user", Json.encodeToString(user))
    }

    private suspend fun syncUser(userId: String): AuthenticatedUser? {
        val userFromServer = apiAuth.getAuthenticatedUser(userId)
        userFromServer?.let {
            settings.storeUserLocally(it)
        }
        return userFromServer
    }


    suspend fun updateUser(user: AuthenticatedUser, image: ByteArray? = null) {
        try {
            apiAuth.updateUser(user, image) // Actualiza en el servidor
            settings.storeUserLocally(user)
        } catch (exception: Exception) {
            throw exception
        }
    }


    //-------------


    suspend fun signInWithFacebook(): LocalAuthResult {
        val result = firebaseAuthRepository.signInWithFacebook()
        // Implementar usando el SDK de Facebook para Firebase
        return LocalAuthResult(success = false, errorMessage = "Not implemented")
    }

    suspend fun signInWithApple(): LocalAuthResult {
        return try {
            val result = firebaseAuthRepository.signInWithApple()
            //   val result = auth.startActivityForSignInWithProvider(activity, provider).await()
            //   MyAuthResult(success = true, userId = result.user?.uid)
            LocalAuthResult(success = true, userId = "result.user?.uid")
        } catch (e: Exception) {
            LocalAuthResult(success = false, errorMessage = e.message)
        }
    }

    //: LocalAuthResult
    suspend fun signInWithInstagram() {
        // return firebaseAuthRepository.signInWithInstagram()
    }

    //: LocalAuthResult
    suspend fun signInWithPhone(phoneNumber: String) {
        //  return firebaseAuthRepository.signInWithPhone(phoneNumber)
    }

    //-------------


    fun fetchCurrentUser(
        forceRefresh: Boolean = false, callback: (user: AuthenticatedUser?) -> Unit
    ) {

        val userKey = firebaseAuthRepository.currentUserId
        var result: AuthenticatedUser? = null
        scope.launch(Dispatchers.IO) {
            if (!forceRefresh) {
                // Busca el usuario almacenado , si no esta almacenado lo busca en el servidor
                val userInSharedPrefs: AuthenticatedUser? = settings.getUserLocally()
                userKey?.let {
                    result = userInSharedPrefs
                }
                if (result != null) {
                    _currentUser.value = result

                } else {
                    userKey?.let {
                        try {
                            result = syncUser(userKey!!)
                            callback(result)

                        } catch (ex: Exception) {
                            result = userInSharedPrefs
                            callback(result)
                        }
                    }
                }
            } else {
                userKey?.let {
                    try {
                        result = syncUser(userKey!!)
                    } catch (ex: Exception) {
                        result = null
                    }
                }
            }

            _currentUser.value = result
            result?.let {
                storeUser(it)
            }
            callback(result)


        }


    }

    fun getCurrentUser(): AuthenticatedUser? {
        return settings.getUserLocally()
    }


    fun storeUser(user: AuthenticatedUser) {
        var EntityAsJson = Json.encodeToString(user)
        settings.set("user", EntityAsJson);
    }

    fun saveSession(
        userId: String, token: String, userName: String?
    ) {
        settings.putString("user_id", userId)
        settings.putString("auth_token", token)
        userName?.let {
            settings.putString("user_name", it)
        }
    }


    fun storeAuthToken(
        token: String,
    ) {
        settings.putString("auth_token", token)
    }


    fun getAuthToken(): String? = firebaseAuthRepository.getAuthToken(false)

    fun clearSession() {
        settings.remove("user_id")
        settings.remove("auth_token")
        settings.remove("user")
    }

    fun logout() {
        runBlocking {
            clearSession()
            firebaseAuthRepository.signOut()
        }
    }

    fun onOTPCodeEntered(code: String,
                         onSuccess: (LocalAuthResult) -> Unit,
                         onFailure: (Exception) -> Unit,) {
        firebaseAuthRepository.onOTPCodeEntered(code,
            onSuccess = { result ->
                if (result.success) {
                    val token = result.authToken
                    settings.setAuthToken(token!!)
                    val userKey = result.user?.uid
                    userKey?.let {
                        runBlocking {
                            val existingUser = apiAuth.getAuthenticatedUser(userKey)
                            if (existingUser == null) {
                                // si el usuario no existe en el servidor significa que es un nuevo usuario
                                val newAuthenticatedUser = AuthenticatedUser()
                                result.user?.let { it ->
                                    newAuthenticatedUser.uid = it.uid
                                    newAuthenticatedUser.displayName = it.displayName
                                    newAuthenticatedUser.email = it.email
                                    newAuthenticatedUser.phoneNumber = it.phoneNumber
                                    newAuthenticatedUser.photoUrl = it.photoUrl
                                    newAuthenticatedUser.method =
                                        AuthenticationMethods.PHONE_NUMBER.name
                                    newAuthenticatedUser.isAnonymous = false
                                }
                                updateUser(newAuthenticatedUser)
                                onSuccess(LocalAuthResult(
                                    success = true,
                                    user = newAuthenticatedUser)
                                )
                            } else {
                                settings.storeUserLocally(existingUser)
                                onSuccess(LocalAuthResult(
                                    success = true,
                                    user = existingUser)
                                )
                            }

                        }
                    }
                }
            },
            onFailure = { exception ->
                println("onFailure")
            }
        )
    }

}

