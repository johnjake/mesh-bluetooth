package app.bluetooth.mesh.features

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import app.bluetooth.mesh.R
import app.bluetooth.mesh.bases.BaseActivity
import app.bluetooth.mesh.databinding.ActivityMainBinding
import app.bluetooth.mesh.features.adapter.MeshAdapter
import app.bluetooth.mesh.features.adapter.SwipeToDeleteCallback
import app.bluetooth.mesh.features.dialog.NewTaskDialogFragment
import app.bluetooth.utilities.extension.setDivider
import live.ditto.Ditto
import live.ditto.DittoCollection
import live.ditto.DittoDocument
import live.ditto.DittoIdentity
import live.ditto.DittoLiveQuery
import live.ditto.DittoLiveQueryEvent
import live.ditto.android.DefaultAndroidDittoDependencies
import live.ditto.transports.DittoSyncPermissions

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate), NewTaskDialogFragment.NewTaskDialogListener {

    private lateinit var ditto: Ditto
    private var collection: DittoCollection? = null
    private var liveQuery: DittoLiveQuery? = null
    private val meshAdapter: MeshAdapter by lazy {
        MeshAdapter(
            onItemClick = this::onClickDocument
        )
    }

    override fun setUpView() {
        super.setUpView()
        binding.apply {
            rvMeshNetwork.apply {
                adapter = meshAdapter
            }
            rvMeshNetwork.setDivider(R.drawable.recycler_view_divider)
        }

        val dependency = DefaultAndroidDittoDependencies(this)
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
        checkLocationPermission()
        checkDittoPermission()
        setupTaskList()
    }

    private fun setupTaskList() {
        collection = this.ditto!!.store.collection("tasks")
        liveQuery = collection!!.findAll().observe { docs, event ->
            when (event) {
                is DittoLiveQueryEvent.Update -> {
                    runOnUiThread {
                        meshAdapter.updateData(docs)
                    }
                }
                is DittoLiveQueryEvent.Initial -> {
                    runOnUiThread {
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
        newFragment.show(supportFragmentManager, "newTask")
    }

    private fun checkDittoPermission() {
        val missing = DittoSyncPermissions(this).missingPermissions()
        if (missing.isNotEmpty()) {
            this.requestPermissions(missing, 0)
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
        }
    }
}
