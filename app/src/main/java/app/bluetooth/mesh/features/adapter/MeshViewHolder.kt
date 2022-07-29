package app.bluetooth.mesh.features.adapter

import androidx.recyclerview.widget.RecyclerView
import app.bluetooth.mesh.databinding.ItemCheckerViewBinding
import app.bluetooth.utilities.extension.OnItemClickListener
import live.ditto.DittoDocument

class MeshViewHolder(
    private val binding: ItemCheckerViewBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(document: DittoDocument, onClick: OnItemClickListener) {
        binding.apply {
            taskTextView.text = document["body"].stringValue
            taskCheckBox.isChecked = document["isCompleted"].booleanValue
        }
        binding.root.rootView.setOnClickListener {
            onClick(document)
        }
    }
}
