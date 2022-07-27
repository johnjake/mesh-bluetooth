package app.bluetooth.mesh.bases

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import app.bluetooth.mesh.hilt.HiltFragmentEntry
import app.bluetooth.utilities.popupBackStack

abstract class BaseFragment<VB : ViewBinding>(
    private val setUpViewBinding: (LayoutInflater) -> VB
) : HiltFragmentEntry() {

    lateinit var binding: VB

    var customBackPressCallback: OnBackPressedCallback? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = setUpViewBinding(inflater)
        return binding.root
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
