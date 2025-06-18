package com.iyr.ultrachango.domain.auth.models

import com.iyr.fbauthentication.common.AuthUser
import com.iyr.ultrachango.data.models.enums.Genders

import com.iyr.ultrachango.utils.datetime.DateTimeProvider
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class AppUser(
    var uid: String,
    var displayName: String? =  null,
    var firstName: String? =  null,
    var lastName: String? =  null,
    var email: String? =null,
    var phoneNumber: String? =  null,
    var birthDate: String? = null,
    var gender: String = Genders.UNKNOWN.name,
    var profilePicturePath: String? = null,
    val createdAt: Long = DateTimeProvider.Companion.getCurrentTimeMillis(),
    val updatedAt: Long = DateTimeProvider.Companion.getCurrentTimeMillis(),
    val isEmailVerified: Boolean?= false,
    val isPhoneVerified: Boolean? = false,
    val isOnline: Boolean = false,
    val lastSeenTimestamp: Long? = 0,
    val lastSyncTimestamp: Long? = 0,
    // Información pública adicional
  //  val bio: String? = null,
  //  val badges: List<String> = emptyList(),
    var providerId: String? =null,

    ) {
    val fullName: String
        get() = "$firstName ${lastName}".trim()

    val age: Int
        get() = if (birthDate != null) calculateAge(birthDate!!) else 0

    // Propiedad calculada que convierte automáticamente

    companion object {
        fun fromNativeUser(user: AuthUser): AppUser {
            return AppUser(
                uid = user.uid,
                email = user.email,
                displayName = user.displayName,
                firstName = user.displayName?.split(" ")?.firstOrNull() ?: "",
                lastName = user.displayName?.split(" ")?.drop(1)?.joinToString(" ") ?: "",
                birthDate = null, // Valor por defecto
                gender = Genders.UNKNOWN.name,

            )
        }

        private fun calculateAge(birthDate: LocalDate): Int {
            val today = DateTimeProvider.Companion.getCurrentDate()
            var age = today.year - birthDate.year
            if (today.month < birthDate.month ||
                (today.month == birthDate.month && today.dayOfMonth < birthDate.dayOfMonth)
            ) {
                age--
            }
            return age
        }

        private fun calculateAge(birthDate: String): Int {
            val birthDateParts = birthDate.split("-")
            val birthYear = birthDateParts[0].toInt()
            val birthMonth = birthDateParts[1].toInt()
            val birthDay = birthDateParts[2].toInt()

            val today = DateTimeProvider.Companion.getCurrentDate()
            var age = today.year - birthYear
            if (today.month.ordinal < birthMonth || (today.month.ordinal == birthMonth && today.dayOfMonth < birthDay)) {
                age--
            }
            return age
        }
    }
}