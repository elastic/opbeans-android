package co.elastic.apm.opbeans.modules.account.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.elastic.apm.opbeans.R
import co.elastic.apm.opbeans.modules.orders.data.models.OrderStateItem

class AccountOrderViewHolder private constructor(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    private val orderId: TextView = itemView.findViewById(R.id.order_item_id)
    private val orderDate: TextView = itemView.findViewById(R.id.order_item_date)

    companion object {
        fun create(parent: ViewGroup): AccountOrderViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.account_order_item, parent, false)
            return AccountOrderViewHolder(view)
        }
    }

    fun setData(order: OrderStateItem) {
        orderId.text = order.displayId
        orderDate.text = order.date
    }
}