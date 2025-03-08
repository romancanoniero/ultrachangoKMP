package com.iyr.ultrachango.utils.auth_by_cursor.models

import com.iyr.ultrachango.data.models.enums.Genders
import com.iyr.ultrachango.utils.auth_by_cursor.auth.NativeUser
import com.iyr.ultrachango.utils.datetime.DateTimeProvider
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class AppUser(
    var uid: String,
    var providerId: String? =null,
    var email: String?,
    var phoneNumber: String?? =  null,
    var displayName: String?,
    var firstName: String? =  null,
    var familyName: String? =  null,
    var birthDate: String? = null,
    var gender: Genders = Genders.UNKNOWN,
    var profilePictureUrl: String? = null,
    val createdAt: Long = DateTimeProvider.getCurrentTimeMillis(),
    val updatedAt: Long = DateTimeProvider.getCurrentTimeMillis(),
    val isEmailVerified: Boolean?= false,
    val isPhoneVerified: Boolean? = false
) {
    val fullName: String
        get() = "$firstName $familyName".trim()

    val age: Int
        get() = if (birthDate != null) calculateAge(birthDate!!) else 0

    companion object {
        fun fromNativeUser(user: NativeUser): AppUser {
            return AppUser(
                uid = user.uid,
                email = user.email,
                displayName = user.displayName,
                firstName = user.displayName?.split(" ")?.firstOrNull() ?: "",
                familyName = user.displayName?.split(" ")?.drop(1)?.joinToString(" ") ?: "",
                birthDate = null, // Valor por defecto
                gender = Genders.UNKNOWN,

            )
        }

        private fun calculateAge(birthDate: LocalDate): Int {
            val today = DateTimeProvider.getCurrentDate()
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

            val today = DateTimeProvider.getCurrentDate()
            var age = today.year - birthYear
            if (today.month.ordinal < birthMonth || (today.month.ordinal == birthMonth && today.dayOfMonth < birthDay)) {
                age--
            }
            return age
        }
    }
}
