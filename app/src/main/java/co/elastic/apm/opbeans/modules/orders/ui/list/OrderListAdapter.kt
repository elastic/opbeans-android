package co.elastic.apm.opbeans.modules.orders.ui.list

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import co.elastic.apm.opbeans.modules.orders.data.models.OrderStateItem

class OrderListAdapter(private val onItemClick: (Int) -> Unit) :
    PagingDataAdapter<OrderStateItem, OrderViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = getItem(position)!!
        holder.setData(order)
        holder.itemView.setOnClickListener {
            onItemClick.invoke(order.id)
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<OrderStateItem>() {

            override fun areItemsTheSame(
                oldItem: OrderStateItem,
                newItem: OrderStateItem
            ): Boolean {
                return oldItem.displayId == newItem.displayId
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