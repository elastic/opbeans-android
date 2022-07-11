package co.elastic.apm.opbeans.modules.account.ui.list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import co.elastic.apm.opbeans.modules.orders.data.models.OrderStateItem

class AccountOrderListAdapter(private val onItemClick: (Int) -> Unit) :
    ListAdapter<OrderStateItem, AccountOrderViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountOrderViewHolder {
        return AccountOrderViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: AccountOrderViewHolder, position: Int) {
        val order = getItem(position)
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