package com.iyr.ultrachango.domain.auth.models

import com.iyr.ultrachango.data.preferences.UserPreferences

class UserProfile (
    val userId: String,
    val preferences: UserPreferences,)