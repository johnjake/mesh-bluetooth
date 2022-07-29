package app.bluetooth.utilities.utils

import androidx.recyclerview.widget.DiffUtil
import live.ditto.DittoDocument

class DittoDocumentUtils(
    private val oldList: List<DittoDocument>,
    private val newList: List<DittoDocument>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
