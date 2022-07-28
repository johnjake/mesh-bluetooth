package app.bluetooth.mesh.features

import androidx.fragment.app.DialogFragment
import app.bluetooth.mesh.R
import app.bluetooth.mesh.bases.BaseFragment
import app.bluetooth.mesh.databinding.FragmentFirstBinding
import app.bluetooth.mesh.features.adapter.MeshAdapter
import app.bluetooth.mesh.features.dialog.NewTaskDialogFragment
import app.bluetooth.utilities.extension.setDivider
import live.ditto.Ditto
import live.ditto.DittoCollection
import live.ditto.DittoLiveQuery

class FirstFragment : BaseFragment<FragmentFirstBinding>(FragmentFirstBinding::inflate), NewTaskDialogFragment.NewTaskDialogListener {

    private val meshAdapter: MeshAdapter by lazy { MeshAdapter() }
    private val ditto: Ditto? = null
    private val collection: DittoCollection? = null
    private val liveQuery: DittoLiveQuery? = null

    override fun setUpView() {
        super.setUpView()
        binding.apply {
            rvMeshNetwork.apply {
                setHasFixedSize(true)
                adapter = meshAdapter
            }
            rvMeshNetwork.setDivider(R.drawable.recycler_view_divider)
        }
    }

    override fun onDialogSave(dialog: DialogFragment, task: String) {
        collection?.upsert(
            mapOf(
                "body" to task,
                "isCompleted" to false
            )
        )
    }

    override fun onDialogCancel(dialog: DialogFragment) { }

    fun showNewTaskUI() {
        val newFragment = NewTaskDialogFragment.newInstance(R.string.add_new_task_dialog_title)
        newFragment.show(requireActivity().supportFragmentManager,"newTask")
    }
}
