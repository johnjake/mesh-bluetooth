package app.bluetooth.mesh.features.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import app.bluetooth.mesh.databinding.ItemCheckerViewBinding
import live.ditto.DittoDocument

class MeshAdapter : ListAdapter<DittoDocument, MeshViewHolder>(diffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeshViewHolder {
        val binding = ItemCheckerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MeshViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MeshViewHolder, position: Int) {
        val currentItem = getItem(position)
        currentItem?.let { holder.bind(it) }
    }

    companion object {
        private val diffCallBack = object : DiffUtil.ItemCallback<DittoDocument>() {
            override fun areItemsTheSame(oldItem: DittoDocument, newItem: DittoDocument): Boolean =
                oldItem["_id"].stringValue == newItem["_id"].stringValue

            override fun areContentsTheSame(
                oldItem: DittoDocument,
                newItem: DittoDocument
            ): Boolean =
                oldItem == newItem
        }
    }
}
