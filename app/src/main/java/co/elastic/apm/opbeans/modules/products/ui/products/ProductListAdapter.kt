package co.elastic.apm.opbeans.modules.products.ui.products

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import co.elastic.apm.opbeans.app.data.models.Product

class ProductListAdapter() : ListAdapter<Product, ProductViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.setData(getItem(position))
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Product>() {

            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }
        }
    }
}