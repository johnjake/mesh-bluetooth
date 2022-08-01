package app.bluetooth.mesh.features

import androidx.navigation.findNavController
import app.bluetooth.mesh.R
import app.bluetooth.mesh.bases.BaseActivity
import app.bluetooth.mesh.databinding.ActivityMainBinding
import app.bluetooth.utilities.manager.DittoManager
import dagger.hilt.android.AndroidEntryPoint
import live.ditto.Ditto
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    @Inject
    lateinit var dtManager: DittoManager
    var ditto: Ditto? = null
    // TODO: use viewModel for DittoManager

    override fun setUpView() {
        super.setUpView()
        val navControl = findNavController(R.id.nav_host_fragment)
        binding.fabAddItem.setOnClickListener {
            navControl.navigateUp()
            navControl.navigate(R.id.action_dialog_add_product)
        }
    }

    override fun setUpObserver() {
        super.setUpObserver()
        ditto = dtManager.instance(this)
        observerDittoManager()
        initializedDittoManager()
    }

    private fun observerDittoManager() {
        dtManager.collectionManager = ditto?.store?.collection("products")!!
    }

    private fun initializedDittoManager() {
        dtManager.startDitto()
    }
}
