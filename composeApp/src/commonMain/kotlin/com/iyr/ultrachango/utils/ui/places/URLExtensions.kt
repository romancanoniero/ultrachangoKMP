package com.iyr.ultrachango.utils.ui.places

import kotlinx.serialization.json.internal.FormatLanguage

class URLExtensions


fun String.toNominatim(
    format : String = "json",
    limit : Int? = null,
    addressdetails : Int = 1,
    nameDetails : Int = 1,
    language: String? = "es"
    ,
): String {

    val urlBuiler = StringBuilder()
    urlBuiler.append("https://nominatim.openstreetmap.org/search?")
    urlBuiler.append("format=").append(format)
    urlBuiler.append("&q=").append(this)
    limit?.let {
        urlBuiler.append("&limit=").append(it)
    }
    addressdetails.let {
        urlBuiler.append("&addressdetails=").append(it)
    }
    nameDetails.let {
        urlBuiler.append("&namedetails=").append(it)
    }
    language?.let {
        urlBuiler.append("&accept-language=").append(it)
    }
    return urlBuiler.toString()
}