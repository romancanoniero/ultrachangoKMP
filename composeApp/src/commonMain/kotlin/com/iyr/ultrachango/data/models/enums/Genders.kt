package com.iyr.ultrachango.data.models.enums

import org.jetbrains.compose.resources.StringResource
import ultrachango2.composeapp.generated.resources.Res
import ultrachango2.composeapp.generated.resources.gender_female
import ultrachango2.composeapp.generated.resources.gender_male
import ultrachango2.composeapp.generated.resources.gender_non_binary
import ultrachango2.composeapp.generated.resources.gender_other
import ultrachango2.composeapp.generated.resources.gender_unknown


enum class Genders(val descriptionResId: StringResource) {
    MALE(Res.string.gender_male),
    FEMALE(Res.string.gender_female),
    NON_BINARY(Res.string.gender_non_binary),
    OTHER(Res.string.gender_other),
    UNKNOWN(Res.string.gender_unknown),;

    companion object {
        fun fromString(value: String?): Genders {
            return entries.find { it.toString() == value?.lowercase() } ?: UNKNOWN
        }


        fun fromInt(value: Int): Genders {
            return values().getOrNull(value) ?: UNKNOWN
        }
    }

    // Para almacenar como Int
    fun toInt(): Int = ordinal

}

// Extensiones para Int
fun Int.toGender(): Genders = Genders.fromInt(this)
fun Genders.toInt(): Int = ordinal

// Extensiones para String
fun String?.toGender(): Genders = Genders.fromString(this)
fun Genders.toStorageString(): String = "value"