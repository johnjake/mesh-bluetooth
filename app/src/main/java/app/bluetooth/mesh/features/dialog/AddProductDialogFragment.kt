package app.bluetooth.mesh.features.dialog

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import app.bluetooth.domain.REQUEST_KEYS
import app.bluetooth.domain.SEND_BUNDLE_MSG
import app.bluetooth.domain.data.Products
import app.bluetooth.mesh.bases.BaseDialogFragment
import app.bluetooth.mesh.databinding.DialogAddProductBinding
import app.bluetooth.mesh.features.product.ProductState
import kotlinx.coroutines.flow.collectLatest

class AddProductDialogFragment : BaseDialogFragment<DialogAddProductBinding>(DialogAddProductBinding::inflate) {

    private val viewModel: DialogViewModel by viewModels()
    override fun setUpView() {
        super.setUpView()
        binding.apply {
            imgClose.setOnClickListener {
                dismiss()
            }
            btnSaveApply.setOnClickListener {
                val product = Products(
                    category = etCategory.text.toString(),
                    name = etName.text.toString(),
                    imageUrl = etImageURL.text.toString(),
                    price = etPrice.text.toString().toDouble(),
                    description = etDescription.text.toString()
                )
                viewModel.insertProduct(product)
            }
        }
    }

    override fun setUpObserver() {
        super.setUpObserver()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.productState.collectLatest { state ->
                when (state) {
                    is ProductState.OnInsertSuccess -> handlesSuccess(state.documentId)
                    is ProductState.OnFailed -> {}
                    is ProductState.OnDittoList -> {}
                    is ProductState.UpdateDocument -> {}
                    is ProductState.ShowLoader -> { }
                    is ProductState.HideLoader -> { }
                }
            }
        }
    }

    private fun handlesSuccess(data: String) {
        if (data.isNotEmpty()) {
            sendBundle(data)
            dismiss()
        }
    }

    private fun sendBundle(documentId: String) {
        requireActivity().supportFragmentManager.setFragmentResult(
            REQUEST_KEYS,
            bundleOf(
                SEND_BUNDLE_MSG to documentId
            )
        )
    }
}
