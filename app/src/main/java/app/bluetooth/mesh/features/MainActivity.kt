package app.bluetooth.mesh.features

import androidx.navigation.findNavController
import app.bluetooth.mesh.R
import app.bluetooth.mesh.bases.BaseActivity
import app.bluetooth.mesh.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    override fun setUpView() {
        super.setUpView()
        val navControl = findNavController(R.id.nav_host_fragment)
        binding.fabAddItem.setOnClickListener {
            navControl.navigateUp()
            navControl.navigate(R.id.action_dialog_add_product)
        }
    }
}
