package app.bluetooth.mesh.features

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import app.bluetooth.domain.REQUEST_KEYS
import app.bluetooth.domain.SEND_BUNDLE_MSG
import app.bluetooth.mesh.bases.BaseFragment
import app.bluetooth.mesh.databinding.FragmentFirstBinding
import app.bluetooth.mesh.features.adapter.MeshAdapter
import app.bluetooth.mesh.features.adapter.SwipeToDeleteCallback
import live.ditto.DittoDocument
import live.ditto.DittoLiveQueryEvent
import timber.log.Timber

class FirstFragment : BaseFragment<FragmentFirstBinding>(FragmentFirstBinding::inflate) {

    private val productAdapter: MeshAdapter by lazy { MeshAdapter { document -> onClickItem(document) } }
    override fun setUpView() {
        super.setUpView()
        binding.apply {
            rvMeshNetwork.apply {
                adapter = productAdapter
            }
            val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val item = productAdapter.documents[viewHolder.adapterPosition]
                    (activity as MainActivity).dtManager.ditto.store.collection("products").findByID(item.id).remove()
                    dittoObservable()
                }
            }

            val itemTouchHelper = ItemTouchHelper(swipeHandler)
            itemTouchHelper.attachToRecyclerView(rvMeshNetwork)
        }
    }

    override fun onResume() {
        super.onResume()
        /** adding products listener **/
        requireActivity().supportFragmentManager
            .setFragmentResultListener(
                REQUEST_KEYS,
                viewLifecycleOwner
            ) { _, bundle ->
                when (bundle.getBoolean(SEND_BUNDLE_MSG)) {
                    true -> dittoObservable()
                    else -> { }
                }
            }
    }

    private fun dittoObservable() {
        (activity as MainActivity).dtManager.collectionManager = (activity as MainActivity).dtManager.ditto?.store?.collection("products")!!
        (activity as MainActivity).dtManager.collectionManager.findAll().observe { docs, events ->
            when (events) {
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
}
