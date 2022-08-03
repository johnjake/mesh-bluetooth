package app.bluetooth.mesh.features.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import app.bluetooth.mesh.databinding.ItemCheckerViewBinding
import app.bluetooth.utilities.extension.OnItemClickListener
import app.bluetooth.utilities.utils.DittoDocumentUtils
import live.ditto.DittoDocument
import live.ditto.DittoLiveQueryMove

class MeshAdapter(
    private val onItemClick: OnItemClickListener
) : ListAdapter<DittoDocument, MeshViewHolder>(diffCallBack) {

    var documents: MutableList<DittoDocument> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeshViewHolder {
        val binding = ItemCheckerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MeshViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MeshViewHolder, position: Int) {
        val currentItem = getItem(position)
        currentItem?.let { holder.bind(it, onItemClick) }
    }

    fun setData(docs: MutableList<DittoDocument>, commitCallback: Runnable? = null) {
        documents = docs
        submitList(docs) {
            commitCallback?.run()
        }
    }

    fun updateData(newList: List<DittoDocument>) {
        val diffItem = DittoDocumentUtils(documents, newList)
        val diffResult = DiffUtil.calculateDiff(diffItem, true)
        documents.clear()
        documents.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount() = this.documents.size

    fun inserts(indexes: List<Int>): Int {
        for (index in indexes) {
            this.notifyItemRangeInserted(index, 1)
        }
        return this.documents.size
    }

    fun deletes(indexes: List<Int>): Int {
        for (index in indexes) {
            this.notifyItemRangeRemoved(index, 1)
        }
        return this.documents.size
    }

    fun updates(indexes: List<Int>): Int {
        for (index in indexes) {
            this.notifyItemRangeChanged(index, 1)
        }
        return this.documents.size
    }

    fun moves(moves: List<DittoLiveQueryMove>) {
        for (move in moves) {
            this.notifyItemMoved(move.from, move.to)
        }
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
