package com.iyr.ultrachango.utils.permissions

import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusDenied
import platform.AVFoundation.AVAuthorizationStatusNotDetermined
import platform.AVFoundation.AVAuthorizationStatusRestricted
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeAudio
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType
import platform.Contacts.CNAuthorizationStatusAuthorized
import platform.Contacts.CNAuthorizationStatusDenied
import platform.Contacts.CNAuthorizationStatusNotDetermined
import platform.Contacts.CNAuthorizationStatusRestricted
import platform.Contacts.CNContactStore
import platform.Contacts.CNEntityType
import platform.CoreLocation.CLLocationManager
import platform.EventKit.EKAuthorizationStatusAuthorized
import platform.EventKit.EKAuthorizationStatusDenied
import platform.EventKit.EKAuthorizationStatusNotDetermined
import platform.EventKit.EKAuthorizationStatusRestricted
import platform.EventKit.EKEntityType
import platform.EventKit.EKEventStore
import platform.Foundation.NSURL
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusDenied
import platform.Photos.PHAuthorizationStatusNotDetermined
import platform.Photos.PHAuthorizationStatusRestricted
import platform.Photos.PHPhotoLibrary
import platform.UIKit.UIApplication
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNAuthorizationStatusDenied
import platform.UserNotifications.UNAuthorizationStatusEphemeral
import platform.UserNotifications.UNAuthorizationStatusNotDetermined
import platform.UserNotifications.UNAuthorizationStatusProvisional
import platform.UserNotifications.UNUserNotificationCenter

// iosMain/kotlin/com/iyr/ultrachango/permissions/IOSPermissionsController.kt
actual class PermissionsController {


    private val locationManager = CLLocationManager()
    private val photoLibrary = PHPhotoLibrary()
    private val contactStore = CNContactStore()
    private val eventStore = EKEventStore()
    private val notificationCenter = UNUserNotificationCenter.currentNotificationCenter()


    companion object {
        const val AUTHORIZED_WHEN_IN_USE = 3
        const val AUTHORIZED_ALLWAYS = 3
        const val AUTHORIZATION_DENIED = 2
        const val AUTHORIZATION_RESTRICTED = 1
        const val AUTHORIZATION_NOT_DETERMINED = 0
    }

    actual suspend fun isPermissionGranted(permission: Permission): Boolean {
        return when (permission) {
            Permission.CAMERA -> {
                val status = AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)
                status == AVAuthorizationStatusAuthorized
            }

            Permission.LOCATION -> {
                val status = locationManager.authorizationStatus()

                status == AUTHORIZED_WHEN_IN_USE || status == AUTHORIZED_ALLWAYS

            }

            Permission.STORAGE -> {
                val status = PHPhotoLibrary.authorizationStatus()
                status == PHAuthorizationStatusAuthorized
            }

            Permission.MICROPHONE -> {
                val status = AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeAudio)
                status == AVAuthorizationStatusAuthorized
            }

            Permission.CONTACTS -> {
                val status =
                    CNContactStore.authorizationStatusForEntityType(CNEntityType.CNEntityTypeContacts)
                status == CNAuthorizationStatusAuthorized
            }

            Permission.CALENDAR -> {
                val status =
                    EKEventStore.authorizationStatusForEntityType(EKEntityType.EKEntityTypeEvent)
                status == EKAuthorizationStatusAuthorized
            }

            Permission.NOTIFICATIONS -> {
                var isGranted = false
                notificationCenter.getNotificationSettingsWithCompletionHandler { settings ->
                    isGranted = settings?.authorizationStatus == UNAuthorizationStatusAuthorized
                }
                isGranted
            }
        }
    }

    actual suspend fun getPermissionState(permission: Permission): PermissionState {
        return when (permission) {
            Permission.CAMERA -> {
                when (AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)) {
                    AVAuthorizationStatusAuthorized -> PermissionState.GRANTED
                    AVAuthorizationStatusDenied -> PermissionState.DENIED
                    AVAuthorizationStatusRestricted -> PermissionState.RESTRICTED
                    AVAuthorizationStatusNotDetermined -> PermissionState.NOT_DETERMINED
                    else -> PermissionState.NOT_DETERMINED
                }
            }

            Permission.LOCATION -> {
                when (CLLocationManager.authorizationStatus()) {
                    AUTHORIZED_WHEN_IN_USE,
                    AUTHORIZED_ALLWAYS -> PermissionState.GRANTED

                    AUTHORIZATION_DENIED -> PermissionState.DENIED
                    AUTHORIZATION_RESTRICTED -> PermissionState.RESTRICTED
                    AUTHORIZATION_NOT_DETERMINED -> PermissionState.NOT_DETERMINED
                    else -> PermissionState.NOT_DETERMINED
                }
            }

            Permission.STORAGE -> {
                when (PHPhotoLibrary.authorizationStatus()) {
                    PHAuthorizationStatusAuthorized -> PermissionState.GRANTED
                    PHAuthorizationStatusDenied -> PermissionState.DENIED
                    PHAuthorizationStatusRestricted -> PermissionState.RESTRICTED
                    PHAuthorizationStatusNotDetermined -> PermissionState.NOT_DETERMINED
                    else -> PermissionState.NOT_DETERMINED
                }
            }

            Permission.MICROPHONE -> {
                when (AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeAudio)) {
                    AVAuthorizationStatusAuthorized -> PermissionState.GRANTED
                    AVAuthorizationStatusDenied -> PermissionState.DENIED
                    AVAuthorizationStatusRestricted -> PermissionState.RESTRICTED
                    AVAuthorizationStatusNotDetermined -> PermissionState.NOT_DETERMINED
                    else -> PermissionState.NOT_DETERMINED
                }
            }

            Permission.CONTACTS -> {
                when (CNContactStore.authorizationStatusForEntityType(CNEntityType.CNEntityTypeContacts)) {
                    CNAuthorizationStatusAuthorized -> PermissionState.GRANTED
                    CNAuthorizationStatusDenied -> PermissionState.DENIED
                    CNAuthorizationStatusRestricted -> PermissionState.RESTRICTED
                    CNAuthorizationStatusNotDetermined -> PermissionState.NOT_DETERMINED
                    else -> PermissionState.NOT_DETERMINED
                }
            }

            Permission.CALENDAR -> {
                when (EKEventStore.authorizationStatusForEntityType(EKEntityType.EKEntityTypeEvent)) {
                    EKAuthorizationStatusAuthorized -> PermissionState.GRANTED
                    EKAuthorizationStatusDenied -> PermissionState.DENIED
                    EKAuthorizationStatusRestricted -> PermissionState.RESTRICTED
                    EKAuthorizationStatusNotDetermined -> PermissionState.NOT_DETERMINED
                    else -> PermissionState.NOT_DETERMINED
                }
            }

            Permission.NOTIFICATIONS -> {
                var state = PermissionState.NOT_DETERMINED
                notificationCenter.getNotificationSettingsWithCompletionHandler { settings ->

                    state = when (settings?.authorizationStatus) {
                        UNAuthorizationStatusAuthorized -> PermissionState.GRANTED
                        UNAuthorizationStatusDenied -> PermissionState.DENIED
                        UNAuthorizationStatusNotDetermined -> PermissionState.NOT_DETERMINED
                        UNAuthorizationStatusProvisional -> PermissionState.GRANTED
                        UNAuthorizationStatusEphemeral -> PermissionState.GRANTED
                        else -> PermissionState.NOT_DETERMINED
                    }
                }
                state
            }
        }
    }

    actual suspend fun requestPermission(permission: Permission): PermissionResult {
        return try {
            when (permission) {
                Permission.CAMERA -> {
                    AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { granted ->
                        if (granted) PermissionResult.Granted else PermissionResult.Denied
                    }
                }

                Permission.LOCATION -> {
                    locationManager.requestWhenInUseAuthorization()
                    PermissionResult.Granted
                }

                Permission.STORAGE -> {
                    PHPhotoLibrary.requestAuthorization { status ->
                        when (status) {
                            PHAuthorizationStatusAuthorized -> PermissionResult.Granted
                            else -> PermissionResult.Denied
                        }
                    }
                }

                Permission.MICROPHONE -> {
                    AVCaptureDevice.requestAccessForMediaType(AVMediaTypeAudio) { granted ->
                        if (granted) PermissionResult.Granted else PermissionResult.Denied
                    }
                }

                Permission.CONTACTS -> {
                    contactStore.requestAccessForEntityType(CNEntityType.CNEntityTypeContacts) { granted, _ ->
                        if (granted) PermissionResult.Granted else PermissionResult.Denied
                    }
                }

                Permission.CALENDAR -> {
                    eventStore.requestAccessToEntityType(EKEntityType.EKEntityTypeEvent) { granted, _ ->
                        if (granted) PermissionResult.Granted else PermissionResult.Denied
                    }
                }

                Permission.NOTIFICATIONS -> {

                    notificationCenter.requestAuthorizationWithOptions(
                        options = UNAuthorizationOptionAlert or
                                UNAuthorizationOptionBadge or
                                UNAuthorizationOptionSound
                    ) { granted, _ ->
                        if (granted) PermissionResult.Granted else PermissionResult.Denied
                    }
                }
            }
            PermissionResult.Granted
        } catch (e: Exception) {
            PermissionResult.Error(e)
        }
    }

    actual suspend fun openAppSettings() {
        val settingsUrl = NSURL(string = "app-settings:")
        UIApplication.sharedApplication.openURL(settingsUrl)
    }

    actual fun shouldShowRequestPermissionRationale(permission: Permission): Boolean {

        return when (permission) {
            Permission.CAMERA -> AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo) == AVAuthorizationStatusDenied
            Permission.LOCATION -> CLLocationManager.authorizationStatus() == AUTHORIZATION_DENIED
            Permission.STORAGE -> PHPhotoLibrary.authorizationStatus() == PHAuthorizationStatusDenied
            Permission.MICROPHONE -> AVCaptureDevice.authorizationStatusForMediaType(
                AVMediaTypeAudio
            ) == AVAuthorizationStatusDenied

            Permission.CONTACTS -> CNContactStore.authorizationStatusForEntityType(CNEntityType.CNEntityTypeContacts) == CNAuthorizationStatusDenied
            Permission.CALENDAR -> EKEventStore.authorizationStatusForEntityType(EKEntityType.EKEntityTypeEvent) == EKAuthorizationStatusDenied
            Permission.NOTIFICATIONS -> {
                var isDenied = false
                notificationCenter.getNotificationSettingsWithCompletionHandler { settings ->
                    isDenied = settings?.authorizationStatus == UNAuthorizationStatusDenied
                }
                isDenied
            }
        }
    }
}