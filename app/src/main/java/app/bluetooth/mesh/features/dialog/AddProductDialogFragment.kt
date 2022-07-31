package app.bluetooth.mesh.features.dialog

import app.bluetooth.mesh.bases.BaseDialogFragment
import app.bluetooth.mesh.databinding.DialogAddProductBinding

class AddProductDialogFragment : BaseDialogFragment<DialogAddProductBinding>(DialogAddProductBinding::inflate) {
    override fun setUpView() {
        super.setUpView()
        binding.apply {
            imgClose.setOnClickListener {
                dismiss()
            }
        }
    }
}
