package co.elastic.apm.opbeans.modules.orderdetail.ui.list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import co.elastic.apm.opbeans.modules.orderdetail.data.OrderedProductSateItem

class OrderedProductListAdapter :
    ListAdapter<OrderedProductSateItem, OrderedProductViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderedProductViewHolder {
        return OrderedProductViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: OrderedProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.setData(product)
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<OrderedProductSateItem>() {

            override fun areItemsTheSame(
                oldItem: OrderedProductSateItem,
                newItem: OrderedProductSateItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: OrderedProductSateItem,
                newItem: OrderedProductSateItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}