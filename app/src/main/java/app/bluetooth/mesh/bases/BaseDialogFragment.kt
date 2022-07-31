package app.bluetooth.mesh.bases

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.viewbinding.ViewBinding
import app.bluetooth.mesh.hilt.HiltDialogFragmentEntry

abstract class BaseDialogFragment<VB : ViewBinding>(
    private val setUpViewBinding: (LayoutInflater) -> VB
) : HiltDialogFragmentEntry() {

    lateinit var binding: VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = setUpViewBinding(inflater)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = context?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        /** for arguments **/
        return dialog!!
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObserver()
        setUpView()
        loadContent()
        addViewListeners()
        setupAdapter(view)
    }

    fun baseActivity() = (requireActivity() as BaseActivity<*>)

    open fun setUpObserver() {}
    open fun loadContent() {}
    open fun setUpView() {}
    open fun setupAdapter(view: View) {}
    open fun addViewListeners() {}
}
