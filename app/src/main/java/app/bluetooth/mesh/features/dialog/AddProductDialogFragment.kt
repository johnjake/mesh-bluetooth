package app.bluetooth.mesh.features.dialog

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import app.bluetooth.domain.REQUEST_KEYS
import app.bluetooth.domain.SEND_BUNDLE_MSG
import app.bluetooth.domain.data.Products
import app.bluetooth.mesh.bases.BaseDialogFragment
import app.bluetooth.mesh.databinding.DialogAddProductBinding
import app.bluetooth.utilities.extension.castToJson
import app.bluetooth.utilities.extension.toast
import live.ditto.DittoDocumentID

class AddProductDialogFragment : BaseDialogFragment<DialogAddProductBinding>(DialogAddProductBinding::inflate) {

    private val viewModel: DialogViewModel by viewModels()
    private fun handleSuccess(data: DittoDocumentID) {
        requireActivity().toast(data[0].toString())
    }

    private fun handleFailed(error: String) {
        TODO("Not yet implemented")
    }

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
                val json = product.castToJson()
                sendBundle(product = json)
                dismiss()
            }
        }
    }

    private fun sendBundle(product: String) {
        requireActivity().supportFragmentManager.setFragmentResult(
            REQUEST_KEYS,
            bundleOf(
                SEND_BUNDLE_MSG to product
            )
        )
    }
}
