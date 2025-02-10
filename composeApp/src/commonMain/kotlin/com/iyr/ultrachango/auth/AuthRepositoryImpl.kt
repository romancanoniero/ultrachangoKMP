package com.iyr.ultrachango.auth

import com.iyr.ultrachango.data.api.cloud.users.CloudUsersService
import com.iyr.ultrachango.data.database.repositories.UserRepositoryImpl
import com.iyr.ultrachango.data.database.repositories.storeUserLocally
import com.iyr.ultrachango.data.models.User
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AuthRepositoryImpl(
    private val apiClient: CloudUsersService,
    private val settings: Settings,
    private val userRepository: UserRepositoryImpl,
    val auth: FirebaseAuth,
    val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
) : AuthRepository {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> get() = _currentUser


    override val currentUserId: String
        get() = auth.currentUser?.uid.toString()

    override val isAuthenticated: Boolean
        get() =
            auth.currentUser != null && auth.currentUser?.isAnonymous == false

    /*
        val currentUser: Flow<User?> =
            auth.authStateChanged.map { it?.let { getUser() } ?:  User()}
    */
    private suspend fun launchWithAwait(block: suspend () -> Unit) {
        scope.async {
            block()
        }.await()
    }

    override suspend fun authenticate(email: String, password: String) {
        launchWithAwait {
            try {
                val authResult = auth.signInWithEmailAndPassword(email, password)
                /*
                                val settings: Settings = Settings()
                                settings.putString("user_id", Firebase.auth.currentUser?.uid ?: "")
                                settings.putString("auth_token", Firebase.auth.currentUser?.getIdToken(false) ?: "")
                */
                saveSession(
                    Firebase.auth.currentUser?.uid ?: "",
                    Firebase.auth.currentUser?.getIdToken(false) ?: ""
                )

            } catch (exception: Exception) {
                // Handle exception
                try {
                    val authResult = auth.createUserWithEmailAndPassword(email, password)
                    val settings: Settings = Settings()
                    settings.putString("user_id", Firebase.auth.currentUser?.uid ?: "")
                    settings.putString(
                        "auth_token",
                        Firebase.auth.currentUser?.getIdToken(false) ?: ""
                    )

                } catch (exception: Exception) {
                    val error = exception
                }


            }


            //    var result2 =  auth.createUserWithEmailAndPassword(email, password)

            var ppp = 2
        }
    }

    override suspend fun createUser(email: String, password: String) {
        val result = launchWithAwait {
            auth.createUserWithEmailAndPassword(email, password)
        }
    }

    override suspend fun createUser(phoneNumber: String) {
        val result = launchWithAwait {


        }
    }


    override suspend fun signOut() {

        if (auth.currentUser?.isAnonymous == true) {
            auth.currentUser?.delete()
        }

        auth.signOut()
        settings.remove("user_id")
        settings.remove("auth_token")
        //create  new user anonymous session
    }


    override fun isUserLoggedIn(): Boolean {
        return settings.getStringOrNull("user_id") != null
    }

    override fun getAuthToken(): String? {
        return settings.getStringOrNull("auth_token")
    }


    override fun getUserKey(): String {
        return auth.currentUser?.uid ?: throw Exception("User not logged in")
    }

    override fun saveUser(user: User) {
        settings.putString("user", Json.encodeToString(user))
    }

    private suspend fun syncUser(userId: String): User? {
        val userFromServer = apiClient.getUser(userId)
        userFromServer?.let {
            settings.storeUserLocally(it) }
        return userFromServer
    }


    override suspend fun updateUser(user: User) {
        userRepository.updateUser(user) // Actualiza en el servidor
        settings.storeUserLocally(user) // Actualiza localmente
    }
/*
    override fun getUser(): Flow<User?> = callbackFlow {
       val userKey = Firebase.auth.currentUser?.uid
        userKey?.let {
            scope.launch {
                val user = userRepository.getUser(it)
                close()
            }
            awaitClose { /* Optional cleanup code */ }
        }
    }
*/

    fun fetchCurrentUser(
        callback: (user: User?) -> Unit
    ){

        var result: User? = null
        runBlocking {
            var userInSharedPrefs: User? = null
            try {
                userInSharedPrefs =
                    Json.decodeFromString(settings.getStringOrNull("user").toString()) as User?
            } catch (e: Exception) {
                userInSharedPrefs = null
            }
            val userKey = Firebase.auth.currentUser?.uid!!

            try {
                result = syncUser(userKey)
            }
            catch (ex : Exception)
            {
                result =  userInSharedPrefs ?: User(userKey)
            }
        }
        _currentUser.value = result
        result?.let {
            storeUser(it)
        }
        callback(result)
    }

    override fun getCurrentUser(): User? {
            return _currentUser.value
    }


    override fun storeUser(user: User) {
        var EntityAsJson = Json.encodeToString(user)
        settings.set("user", EntityAsJson);
    }

    override fun saveSession(
        userId: String,
        token: String,
        userName: String?
    ) {
        settings.putString("user_id", userId)
        settings.putString("auth_token", token)
        userName?.let {
            settings.putString("user_name", it)
        }
    }


    override fun clearSession() {
        settings.remove("user_id")
        settings.remove("auth_token")
        settings.remove("user")
    }
}