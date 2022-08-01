package app.bluetooth.domain

import android.Manifest

/**
 * https://github.com/getditto/docs/issues/182
 * **/
const val REQUEST_CODE_PERMISSIONS = 10
const val REQUEST_KEYS = "add.success"
const val SEND_BUNDLE_MSG = "bundle.message"
val REQUIRED_PERMISSIONS = arrayOf(
    Manifest.permission.BLUETOOTH,
    Manifest.permission.BLUETOOTH_ADMIN,
    Manifest.permission.BLUETOOTH_SCAN,
    Manifest.permission.BLUETOOTH_ADVERTISE,
    Manifest.permission.BLUETOOTH_CONNECT,
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.CHANGE_NETWORK_STATE
)
