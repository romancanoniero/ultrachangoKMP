package com.iyr.ultrachango.utils.ui.places.google.model

import kotlinx.serialization.Serializable


@Serializable
data class Viewport(
    val northeast: Northeast,
    val southwest: Southwest
)