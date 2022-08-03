package app.bluetooth.mesh.features

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import app.bluetooth.domain.REQUEST_KEYS
import app.bluetooth.domain.SEND_BUNDLE_MSG
import app.bluetooth.mesh.bases.BaseFragment
import app.bluetooth.mesh.databinding.FragmentFirstBinding
import app.bluetooth.mesh.features.adapter.MeshAdapter
import app.bluetooth.mesh.features.adapter.SwipeToDeleteCallback
import app.bluetooth.mesh.features.product.ProductState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import live.ditto.DittoDocument
import live.ditto.transports.DittoSyncPermissions
import timber.log.Timber

@AndroidEntryPoint
class FirstFragment : BaseFragment<FragmentFirstBinding>(FragmentFirstBinding::inflate) {

    private val productAdapter: MeshAdapter by lazy { MeshAdapter { document -> onClickItem(document) } }
    private val viewModel: MainViewModel by viewModels()

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
        Timber.e("Update -> $data")
        productAdapter.updateData(data)
    }

    private fun bindToList(data: List<DittoDocument>) {
        Timber.e("Initial -> $data")
        productAdapter.setData(data.toMutableList())
    }

    override fun onStart() {
        super.onStart()
        checkPermissions()
        observerDittoManager()
        dittoObservable()
    }

    private fun observerDittoManager() {
        viewModel.storeDittoNode()
    }

    override fun onResume() {
        super.onResume()
        requireActivity().supportFragmentManager
            .setFragmentResultListener(REQUEST_KEYS, viewLifecycleOwner) { _, bundle ->
                val documentId = bundle.getString(SEND_BUNDLE_MSG).orEmpty()
                if (documentId.isNotEmpty()) {
                    Timber.e("***  Insert data $documentId ***")
                    dittoObservable()
                }
            }
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
        viewModel.refreshPermission()
    }
}
