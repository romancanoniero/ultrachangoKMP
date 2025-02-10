package com.iyr.ultrachango.data.models.enums

import org.jetbrains.compose.resources.StringResource
import ultrachango2.composeapp.generated.resources.Res
import ultrachango2.composeapp.generated.resources.gender_female
import ultrachango2.composeapp.generated.resources.gender_male
import ultrachango2.composeapp.generated.resources.gender_other


enum class Genders(val descriptionResId: StringResource) {
    MALE(Res.string.gender_male),
    FEMALE(Res.string.gender_female),
    OTHER(Res.string.gender_other)
}