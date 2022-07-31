package app.bluetooth.mesh.bases

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import app.bluetooth.mesh.hilt.HiltFragmentEntry
import app.bluetooth.utilities.extension.popupBackStack

abstract class BaseFragment<VB : ViewBinding>(
    private val setUpViewBinding: (LayoutInflater) -> VB
) : HiltFragmentEntry() {

    lateinit var binding: VB
    lateinit var contexts: Context

    var customBackPressCallback: OnBackPressedCallback? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = setUpViewBinding(inflater)
        contexts = binding.root.context
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObserver()
        setUpView()
        loadContent()
        setupAdapter(binding.root)
        addViewListeners()
    }

    fun baseActivity() = (requireActivity() as BaseActivity<*>)

    open fun isSoftInputModeAdjustResize(): Boolean {
        return false
    }

    open fun navigateBack() {
        popupBackStack()
    }

    fun LifecycleOwner.handleOnBackPressed(isCustomBackDisable: Boolean = false, backPressedAction: () -> Unit) {
        /**
         * Reference for this custom back
         * https://developer.android.com/guide/navigation/navigation-custom-back
         **/
        customBackPressCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle the back button event
                backPressedAction()
            }
        }
        baseActivity().onBackPressedDispatcher.addCallback(
            this,
            customBackPressCallback as OnBackPressedCallback
        )
        if (isCustomBackDisable) {
            customBackPressCallback?.remove()
        }
    }

    open fun setUpObserver() {}
    open fun setUpView() {}
    open fun loadContent() {}
    open fun setupAdapter(view: View) {}
    open fun addViewListeners() {}
}
