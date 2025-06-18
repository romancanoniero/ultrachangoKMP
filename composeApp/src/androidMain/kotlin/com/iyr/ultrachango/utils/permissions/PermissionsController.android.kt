package com.iyr.ultrachango.utils.permissions

import AppContext
import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.core.content.ContextCompat

// androidMain/kotlin/com/iyr/ultrachango/permissions/AndroidPermissionsController.kt
actual class PermissionsController {

    private val context = AppContext.context
    private val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

    actual suspend fun isPermissionGranted(permission: Permission): Boolean {

        return when (permission) {
            Permission.CAMERA -> ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
            Permission.LOCATION -> {
                val fineLocation = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                val coarseLocation = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                fineLocation || coarseLocation
            }
            Permission.STORAGE -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    Environment.isExternalStorageManager()
                } else {
                    val read = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                    val write = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                    read && write
                }
            }
            Permission.MICROPHONE -> ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
            Permission.CONTACTS -> ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
            Permission.CALENDAR -> ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CALENDAR
            ) == PackageManager.PERMISSION_GRANTED
            Permission.NOTIFICATIONS -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                } else {
                    true // Las notificaciones no requieren permiso explícito en versiones anteriores
                }
            }
        }
    }

    actual suspend fun getPermissionState(permission: Permission): PermissionState {
        android.Manifest.permission.ACCESS_FINE_LOCATION
        return when (permission) {
            Permission.CAMERA -> getPermissionStateFor(Permission.CAMERA)
            Permission.LOCATION -> {
                val fineLocation = getPermissionStateFor(Permission.LOCATION)
                val coarseLocation = getPermissionStateFor(Permission.LOCATION)
                when {
                    fineLocation == PermissionState.GRANTED || coarseLocation == PermissionState.GRANTED ->
                        PermissionState.GRANTED
                    fineLocation == PermissionState.DENIED_ALWAYS || coarseLocation == PermissionState.DENIED_ALWAYS ->
                        PermissionState.DENIED_ALWAYS
                    fineLocation == PermissionState.DENIED || coarseLocation == PermissionState.DENIED ->
                        PermissionState.DENIED
                    else -> PermissionState.NOT_DETERMINED
                }
            }
            // ... implementaciones similares para otros permisos
            Permission.STORAGE -> TODO()
            Permission.MICROPHONE -> TODO()
            Permission.CONTACTS -> TODO()
            Permission.CALENDAR -> TODO()
            Permission.NOTIFICATIONS -> TODO()
        }
    }

    private fun getPermissionStateFor(permission: Permission): PermissionState {
        return when {
            ContextCompat.checkSelfPermission(context, permission.toString()) == PackageManager.PERMISSION_GRANTED ->
                PermissionState.GRANTED
            shouldShowRequestPermissionRationale(permission) -> PermissionState.DENIED
            !shouldShowRequestPermissionRationale(permission) -> PermissionState.DENIED_ALWAYS
            else -> PermissionState.NOT_DETERMINED
        }
    }

    actual suspend fun requestPermission(permission: Permission): PermissionResult {
        return try {
            val activity = getCurrentActivity() ?: throw Exception("No activity found")
            val permissionString = when (permission) {
                Permission.CAMERA -> Manifest.permission.CAMERA
                Permission.LOCATION -> Manifest.permission.ACCESS_FINE_LOCATION
                Permission.STORAGE -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE
                    } else {
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }
                }
                // ... otros permisos
                Permission.MICROPHONE -> TODO()
                Permission.CONTACTS -> TODO()
                Permission.CALENDAR -> TODO()
                Permission.NOTIFICATIONS -> TODO()
            }

            activity.requestPermissions(arrayOf(permissionString), PERMISSION_REQUEST_CODE)
            PermissionResult.Granted
        } catch (e: Exception) {
            PermissionResult.Error(e)
        }
    }

    actual suspend fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    actual fun shouldShowRequestPermissionRationale(permission: Permission): Boolean {
        val activity = getCurrentActivity() ?: return false
        return when (permission) {
            Permission.CAMERA -> activity.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)
            Permission.LOCATION -> activity.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
            // ... otros permisos
            Permission.STORAGE -> TODO()
            Permission.MICROPHONE -> TODO()
            Permission.CONTACTS -> TODO()
            Permission.CALENDAR -> TODO()
            Permission.NOTIFICATIONS -> TODO()
        }
    }

    private fun getCurrentActivity(): Activity? {
        val activityThread = Class.forName("android.app.ActivityThread")
        val currentActivityThread = activityThread.getMethod("currentActivityThread").invoke(null)
        val mActivities = activityThread.getDeclaredField("mActivities").apply { isAccessible = true }
        val activities = mActivities.get(currentActivityThread) as Map<*, *>

        for (activityRecord in activities.values) {
            val activityRecordClass = activityRecord?.javaClass
            val activityField = activityRecordClass?.getDeclaredField("activity")?.apply { isAccessible = true }
            val activity = activityField?.get(activityRecord) as? Activity
            if (activity != null && !activity.isFinishing) {
                return activity
            }
        }
        return null
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
    }
}