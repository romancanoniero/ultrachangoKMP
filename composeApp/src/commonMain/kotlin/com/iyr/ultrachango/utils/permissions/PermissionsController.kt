package com.iyr.ultrachango.utils.permissions

expect class PermissionsController {
    suspend fun isPermissionGranted(permission: Permission): Boolean
    suspend fun getPermissionState(permission: Permission): PermissionState
    suspend fun requestPermission(permission: Permission): PermissionResult
    suspend fun openAppSettings()
    fun shouldShowRequestPermissionRationale(permission: Permission): Boolean
}


suspend fun PermissionsController.checkAndRequestLocationPermission(
    permissionsController: PermissionsController,
    onGranted: suspend () -> Unit,
    onDenied: suspend () -> Unit,
    onShowRationale: suspend () -> Unit
) {
    when {
        permissionsController.isPermissionGranted(Permission.LOCATION) -> {
            onGranted()
        }
        permissionsController.shouldShowRequestPermissionRationale(Permission.LOCATION) -> {
            onShowRationale()
        }
        else -> {
            when (permissionsController.requestPermission(Permission.LOCATION)) {
                is PermissionResult.Granted -> onGranted()
                else -> onDenied()
            }
        }
    }
}