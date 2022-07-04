package co.elastic.apm.opbeans.modules.orders.ui.list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import co.elastic.apm.opbeans.modules.orders.data.models.OrderStateItem

class OrderListAdapter : ListAdapter<OrderStateItem, OrderViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.setData(getItem(position))
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<OrderStateItem>() {

            override fun areItemsTheSame(
                oldItem: OrderStateItem,
                newItem: OrderStateItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: OrderStateItem,
                newItem: OrderStateItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}