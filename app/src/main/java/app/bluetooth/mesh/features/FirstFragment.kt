package app.bluetooth.mesh.features

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import app.bluetooth.mesh.bases.BaseFragment
import app.bluetooth.mesh.databinding.FragmentFirstBinding
import app.bluetooth.mesh.features.adapter.MeshAdapter
import app.bluetooth.mesh.features.adapter.SwipeToDeleteCallback
import app.bluetooth.mesh.features.product.ProductState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import live.ditto.Ditto
import live.ditto.DittoDocument
import live.ditto.transports.DittoSyncPermissions
import timber.log.Timber

@AndroidEntryPoint
class FirstFragment : BaseFragment<FragmentFirstBinding>(FragmentFirstBinding::inflate) {

    private val productAdapter: MeshAdapter by lazy { MeshAdapter { document -> onClickItem(document) } }
    private val viewModel: MainViewModel by viewModels()

    var ditto: Ditto? = null

    override fun setUpView() {
        super.setUpView()
        binding.apply {
            rvMeshNetwork.apply {
                adapter = productAdapter
            }
            val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val item = productAdapter.documents[viewHolder.adapterPosition]
                    viewModel.deleteNode(item.id)
                    dittoObservable()
                }
            }

            val itemTouchHelper = ItemTouchHelper(swipeHandler)
            itemTouchHelper.attachToRecyclerView(rvMeshNetwork)
        }
    }

    override fun setUpObserver() {
        super.setUpObserver()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.productState.collectLatest { state ->
                when (state) {
                    is ProductState.OnDittoList -> bindToList(state.data)
                    is ProductState.UpdateDocument -> bindUpdatedList(state.data)
                }
            }
        }
    }

    private fun bindUpdatedList(data: List<DittoDocument>) {
        Timber.e("###################### $data")
        productAdapter.updateData(data)
    }

    private fun bindToList(data: List<DittoDocument>) {
        Timber.e("#################### $data")
        productAdapter.setData(data.toMutableList())
    }

    override fun onStart() {
        super.onStart()
        checkPermissions()
        runDitto()
        syncData()
        observerDittoManager()
        dittoObservable()
    }

    private fun runDitto() {
        ditto = viewModel.instance()
    }

    private fun observerDittoManager() {
        ditto?.let { viewModel.storeDittoNode(it) }
    }

    private fun syncData() {
        viewModel.startSync()
    }

    override fun onResume() {
        super.onResume()
        /** adding products listener **/
        dittoObservable()
    }

    private fun dittoObservable() {
        viewModel.observeDittoManager()
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
        ditto?.let { viewModel.refreshPermission(it) }
    }
}
