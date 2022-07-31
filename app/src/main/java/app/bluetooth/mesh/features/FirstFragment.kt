package app.bluetooth.mesh.features

import app.bluetooth.mesh.bases.BaseFragment
import app.bluetooth.mesh.databinding.FragmentFirstBinding
import app.bluetooth.mesh.features.adapter.MeshAdapter
import live.ditto.DittoDocument

class FirstFragment : BaseFragment<FragmentFirstBinding>(FragmentFirstBinding::inflate) {

    private val productAdapter: MeshAdapter by lazy { MeshAdapter { document -> onClickItem(document) } }

    override fun setUpView() {
        super.setUpView()
        binding.apply {
            rvMeshNetwork.apply {
                adapter = productAdapter
            }
        }
    }

    private fun onClickItem(document: DittoDocument) {
        TODO("Not yet implemented")
    }
}
