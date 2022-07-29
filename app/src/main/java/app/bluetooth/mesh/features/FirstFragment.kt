package app.bluetooth.mesh.features

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import app.bluetooth.mesh.R
import app.bluetooth.mesh.bases.BaseFragment
import app.bluetooth.mesh.databinding.FragmentFirstBinding
import app.bluetooth.mesh.features.adapter.MeshAdapter
import app.bluetooth.mesh.features.adapter.SwipeToDeleteCallback
import app.bluetooth.mesh.features.dialog.NewTaskDialogFragment
import live.ditto.Ditto
import live.ditto.DittoCollection
import live.ditto.DittoDocument
import live.ditto.DittoIdentity
import live.ditto.DittoLiveQuery
import live.ditto.DittoLiveQueryEvent
import live.ditto.android.DefaultAndroidDittoDependencies
import live.ditto.transports.DittoSyncPermissions

class FirstFragment : BaseFragment<FragmentFirstBinding>(FragmentFirstBinding::inflate), NewTaskDialogFragment.NewTaskDialogListener {

    private val meshAdapter: MeshAdapter by lazy {
        MeshAdapter(
            onItemClick = this::onClickDocument
        )
    }

    private lateinit var ditto: Ditto
    private var collection: DittoCollection? = null
    private var liveQuery: DittoLiveQuery? = null

    override fun setUpView() {
        super.setUpView()
        binding.apply {
            rvMeshNetwork.apply {
                adapter = meshAdapter
            }
        }

        val dependency = DefaultAndroidDittoDependencies(requireContext())
        val dittoInstance = Ditto(
            dependency,
            DittoIdentity.OnlinePlayground(
                dependency,
                "f2b5f038-6d00-433a-9176-6e84011da136",
                "545717fe-6ffc-4e9f-ab47-7b500430a6ce",
                enableDittoCloudSync = true
            )
        )
        ditto = dittoInstance
        dittoInstance.startSync()
        recycleSwift()
    }

    private fun recycleSwift() {
        val recyclerView = binding.rvMeshNetwork
        val swipeHandler = object : SwipeToDeleteCallback(binding.root.context) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val task = meshAdapter.currentList[viewHolder.adapterPosition]
                ditto.store.collection("tasks").findByID(task.id).remove()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        binding.fab.setOnClickListener { _ ->
            showNewTaskUI()
        }
        setupTaskList()
        checkLocationPermission()
        checkDittoPermission()
    }

    private fun setupTaskList() {
        val recyclerView = binding.rvMeshNetwork
        // We will create a long-running live query to keep UI up-to-date
        collection = this.ditto!!.store.collection("tasks")

        // We use observe to create a live query with a subscription to sync this query with other devices
        liveQuery = collection!!.findAll().observe { docs, event ->

            when (event) {
                is DittoLiveQueryEvent.Update -> {
                    requireActivity().runOnUiThread {
                        meshAdapter.updateData(docs)
                        // adapter.inserts(event.insertions)
                        // adapter.deletes(event.deletions)
                        // adapter.updates(event.updates)
                        // adapter.moves(event.moves)
                    }
                }
                is DittoLiveQueryEvent.Initial -> {
                    requireActivity().runOnUiThread {
                        meshAdapter.setData(docs = docs.toMutableList())
                    }
                }
            }
        }
    }

    private fun onClickDocument(document: DittoDocument) {
        ditto.store.collection("tasks").findByID(document.id).update { newTask ->
            newTask!!["isCompleted"].set(!newTask["isCompleted"].booleanValue)
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

    private fun showNewTaskUI() {
        val newFragment = NewTaskDialogFragment.newInstance(R.string.add_new_task_dialog_title)
        newFragment.show(requireActivity().supportFragmentManager, "newTask")
    }

    private fun checkDittoPermission() {
        val missing = DittoSyncPermissions(contexts).missingPermissions()
        if (missing.isNotEmpty()) {
            this.requestPermissions(missing, 0)
        }
    }

    private fun checkLocationPermission() {
        // On Android, parts of Bluetooth LE and WiFi Direct require location permission
        // Ditto will operate without it but data sync may be impossible in certain scenarios
        if (ContextCompat.checkSelfPermission(contexts, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // For this app we will prompt the user for this permission every time if it is missing
            // We ignore the result - Ditto will automatically notice when the permission is granted
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
        }
    }
}
