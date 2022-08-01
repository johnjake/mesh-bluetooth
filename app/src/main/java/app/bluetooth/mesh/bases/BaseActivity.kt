package app.bluetooth.mesh.bases

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import app.bluetooth.domain.REQUEST_CODE_PERMISSIONS
import app.bluetooth.domain.REQUIRED_PERMISSIONS
import app.bluetooth.mesh.hilt.HiltActivityEntry
import app.bluetooth.utilities.extension.toast

abstract class BaseActivity<T : ViewBinding>(
    private val setUpViewBinding: (LayoutInflater) -> T
) : HiltActivityEntry() {
    lateinit var binding: T

    private var meshUnit: Unit? = null
    var requestPermission: Unit? get() = meshUnit
        set(value) {
            meshUnit = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = setUpViewBinding(layoutInflater)
        setContentView(binding.root)
        setUpObserver()
        setUpView()
        setUpMeshPermission()
    }

    private fun requestAndroidPermission() {
        ActivityCompat.requestPermissions(
            this,
            REQUIRED_PERMISSIONS,
            REQUEST_CODE_PERMISSIONS
        )
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                meshUnit
            } else {
                toast("Access denied")
            }
        }
    }

    private fun setUpMeshPermission() {
        if (allPermissionsGranted()) {
            meshUnit
        } else {
            requestAndroidPermission()
        }
    }

    open fun setUpObserver() {}
    open fun setUpView() {}
}
