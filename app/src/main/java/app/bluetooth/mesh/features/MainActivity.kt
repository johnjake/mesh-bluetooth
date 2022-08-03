package app.bluetooth.mesh.features

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.BLUETOOTH_ADMIN
import android.Manifest.permission.BLUETOOTH_ADVERTISE
import android.Manifest.permission.BLUETOOTH_CONNECT
import android.Manifest.permission.BLUETOOTH_SCAN
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.navigation.findNavController
import app.bluetooth.domain.REQUEST_CODE_PERMISSIONS
import app.bluetooth.mesh.R
import app.bluetooth.mesh.bases.BaseActivity
import app.bluetooth.mesh.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    // TODO: use viewModel for DittoManager
    override fun setUpView() {
        super.setUpView()
        val navControl = findNavController(R.id.nav_host_fragment)
        binding.fabAddItem.setOnClickListener {
            navControl.navigateUp()
            navControl.navigate(R.id.action_dialog_add_product)
        }
        checkBluetooth()
        checkLocationPermission()
    }

    private fun setBluetoothEnable() {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        if (!bluetoothManager.adapter.isEnabled) {
            // bluetoothManager.adapter.enable()
        }
    }

    private fun checkLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    requestPermissions(
                        arrayOf(
                            BLUETOOTH_SCAN,
                            BLUETOOTH_CONNECT,
                            BLUETOOTH_ADVERTISE,
                            BLUETOOTH_ADMIN
                        ),
                        REQUEST_CODE_PERMISSIONS
                    )
                } else {
                    requestPermissions(
                        arrayOf(
                            ACCESS_FINE_LOCATION,
                            ACCESS_COARSE_LOCATION,
                            BLUETOOTH_ADMIN
                        ),
                        REQUEST_CODE_PERMISSIONS
                    )
                }
            } else {
                requestPermissions(
                    arrayOf(
                        ACCESS_FINE_LOCATION,
                        ACCESS_COARSE_LOCATION,
                        BLUETOOTH_ADMIN
                    ),
                    REQUEST_CODE_PERMISSIONS
                )
            }
        }
    }
    private fun PackageManager.missingSystemFeature(name: String): Boolean = !hasSystemFeature(name)

    private fun checkBluetooth() {
        packageManager.takeIf { it.missingSystemFeature(PackageManager.FEATURE_BLUETOOTH) }?.also {
            Toast.makeText(this, R.string.bluetooth_not_supported, Toast.LENGTH_SHORT).show()
            finish()
        }
        packageManager.takeIf { it.missingSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE) }?.also {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
