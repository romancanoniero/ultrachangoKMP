package com.iyr.ultrachango.utils.ui.places.google.model

import kotlinx.serialization.Serializable


@Serializable
data class PlusCode(
    val compound_code: String,
    val global_code: String
)