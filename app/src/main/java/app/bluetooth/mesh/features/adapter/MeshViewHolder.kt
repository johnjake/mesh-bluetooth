package app.bluetooth.mesh.features.adapter

import androidx.recyclerview.widget.RecyclerView
import app.bluetooth.mesh.databinding.ItemCheckerViewBinding
import live.ditto.DittoDocument

class MeshViewHolder(
    private val binding: ItemCheckerViewBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(document: DittoDocument) {
        binding.apply {
            taskTextView.text = document["body"].stringValue
            taskCheckBox.isChecked = document["isCompleted"].booleanValue
        }
    }
}
