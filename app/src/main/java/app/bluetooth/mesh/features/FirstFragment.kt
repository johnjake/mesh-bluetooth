package app.bluetooth.mesh.features

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import app.bluetooth.domain.REQUEST_KEYS
import app.bluetooth.domain.SEND_BUNDLE_MSG
import app.bluetooth.domain.data.Products
import app.bluetooth.mesh.bases.BaseFragment
import app.bluetooth.mesh.databinding.FragmentFirstBinding
import app.bluetooth.mesh.features.adapter.MeshAdapter
import app.bluetooth.mesh.features.adapter.SwipeToDeleteCallback
import app.bluetooth.utilities.extension.castToClass
import app.bluetooth.utilities.extension.castToMap
import app.bluetooth.utilities.manager.DittoManager
import dagger.hilt.android.AndroidEntryPoint
import live.ditto.Ditto
import live.ditto.DittoDocument
import live.ditto.DittoLiveQueryEvent
import live.ditto.transports.DittoSyncPermissions
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class FirstFragment : BaseFragment<FragmentFirstBinding>(FragmentFirstBinding::inflate) {

    private val productAdapter: MeshAdapter by lazy { MeshAdapter { document -> onClickItem(document) } }

    @Inject
    lateinit var dtManager: DittoManager
    var ditto: Ditto? = null

    override fun setUpObserver() {
        super.setUpObserver()
    }

    override fun setUpView() {
        super.setUpView()
        binding.apply {
            rvMeshNetwork.apply {
                adapter = productAdapter
            }
            val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val item = productAdapter.documents[viewHolder.adapterPosition]
                    dtManager.ditto.store.collection("products").findByID(item.id).remove()
                }
            }

            val itemTouchHelper = ItemTouchHelper(swipeHandler)
            itemTouchHelper.attachToRecyclerView(rvMeshNetwork)
        }
    }

    override fun onStart() {
        super.onStart()
        checkPermissions()
        runDitto()
        syncData()
        dittoObservable()
    }

    private fun runDitto() {
        ditto = dtManager.instance(requireContext())
    }

    private fun observerDittoManager() {
        dtManager.collectionManager = ditto?.store?.collection("products")!!
    }

    private fun syncData() {
        dtManager.startDitto()
    }

    override fun onResume() {
        super.onResume()
        /** adding products listener **/
        requireActivity().supportFragmentManager
            .setFragmentResultListener(
                REQUEST_KEYS,
                viewLifecycleOwner
            ) { _, bundle ->
                val json = bundle.getString(SEND_BUNDLE_MSG).orEmpty()
                val product = json.castToClass<Products>()
                if (product != null) {
                    val map = castToMap(product)
                    dtManager.collectionManager.upsert(map)
                }
            }
    }

    private fun dittoObservable() {
        observerDittoManager()
        dtManager.collectionManager.findAll().observe { docs, event ->
            when (event) {
                is DittoLiveQueryEvent.Update -> {
                    requireActivity().runOnUiThread {
                        productAdapter.updateData(docs)
                    }
                }
                is DittoLiveQueryEvent.Initial -> {
                    requireActivity().runOnUiThread {
                        productAdapter.setData(docs.toMutableList())
                    }
                }
            }
        }
    }

    private fun onClickItem(document: DittoDocument) {
        Timber.e("${document.id}")
    }

    private fun checkPermissions() {
        val activity = requireActivity()
        val missing = DittoSyncPermissions(activity).missingPermissions()
        if (missing.isNotEmpty()) {
            activity.requestPermissions(missing, 0)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Regardless of the outcome, tell Ditto that permissions maybe changed
        ditto?.refreshPermissions()
    }
}
