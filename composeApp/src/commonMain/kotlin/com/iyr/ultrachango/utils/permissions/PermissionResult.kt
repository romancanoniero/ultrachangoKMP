package com.iyr.ultrachango.utils.permissions

sealed class PermissionResult {
    object Granted : PermissionResult()
    object Denied : PermissionResult()
    object DeniedAlways : PermissionResult()
    object NotDetermined : PermissionResult()
    object Restricted : PermissionResult()
    data class Error(val exception: Exception) : PermissionResult()
}