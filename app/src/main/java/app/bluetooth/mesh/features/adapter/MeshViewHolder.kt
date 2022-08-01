package app.bluetooth.mesh.features.adapter

import androidx.recyclerview.widget.RecyclerView
import app.bluetooth.mesh.databinding.ItemCheckerViewBinding
import app.bluetooth.utilities.extension.OnItemClickListener
import app.bluetooth.utilities.extension.visible
import live.ditto.DittoDocument

class MeshViewHolder(
    private val binding: ItemCheckerViewBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(document: DittoDocument, onClick: OnItemClickListener) {
        binding.apply {
            val price = document["price"].doubleValue.toString()
            val category = document["category"].stringValue
            tvFreePrice.visible()
            tvShop.text = document["name"].stringValue
            tvShopDesc.text = "Category: $category"
            tvFreePrice.text = "Price: $$price"
        }
        binding.root.rootView.setOnClickListener {
            onClick(document)
        }
    }
}
